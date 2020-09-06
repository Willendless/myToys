package gocache

import (
	"fmt"
	pb "gocache/gocachepb"
	"gocache/singleflight"
	"log"
	"sync"
)

//
// Getter is a interface for
// user-defined data obtain callback function
//
type Getter interface {
	Get(key string) ([]byte, error)
}

//
// GetterFunc is a function type implement Getter interface
//
type GetterFunc func(key string) ([]byte, error)

//
// Get return user-defined data needed to be cached
//
func (f GetterFunc) Get(key string) ([]byte, error) {
	return f(key)
}

// PeerPicker defines a type which can pick peer
type PeerPicker interface {
	PickPeer(key string) (*HTTPClient, bool)
}

//
// Group is a cache namespace and associated data
//
type Group struct {
	name       string
	getter     Getter
	mainCache  cache
	peerPicker PeerPicker
	// use loader to guarantee key only fetched once
	loader *singleflight.Group
}

var (
	mu     sync.RWMutex
	groups = make(map[string]*Group)
)

//
// RegisterPeerPicker register a PeerPicker for peer choosing
//
func (g *Group) RegisterPeerPicker(peerPicker PeerPicker) {
	if g.peerPicker != nil {
		panic("RegisterPeerPicker called more than once")
	}
	g.peerPicker = peerPicker
}

//
// NewGroup create a new instance of Group
//
func NewGroup(name string, cacheBytes int64, getter Getter) *Group {
	if getter == nil {
		panic("getter func should not be nil")
	}
	mu.Lock()
	defer mu.Unlock()
	g := &Group{
		name:      name,
		getter:    getter,
		mainCache: cache{cacheBytes: cacheBytes},
		loader:    &singleflight.Group{},
	}
	groups[name] = g
	return g
}

//
// GetGroup returns the named group if have created
// otherwise nil
//
func GetGroup(name string) *Group {
	mu.RLock()
	defer mu.RUnlock()
	return groups[name]
}

//
// Get function for a group
//
func (g *Group) Get(key string) (ByteView, error) {
	if key == "" {
		return ByteView{}, fmt.Errorf("key is required")
	}
	if v, ok := g.mainCache.get(key); ok {
		log.Printf("[GoCache] get hit, %v get (%v,%v)", g.name, key, v)
		return v, nil
	}
	return g.load(key)
}

func (g *Group) load(key string) (ByteView, error) {
	view, err := g.loader.Do(key, func() (interface{}, error) {
		if g.peerPicker != nil {
			if peer, ok := g.peerPicker.PickPeer(key); ok {
				if value, err := g.getFromPeer(peer, key); err == nil {
					return value, err
				} else {
					log.Println("[GoCache] Failed to get from peer", err)
				}
			}
		}
		return g.getLocally(key)
	})
	if err == nil {
		return view.(ByteView), nil
	}
	return ByteView{}, err
}

func (g *Group) getFromPeer(peer *HTTPClient, key string) (ByteView, error) {
	request := &pb.Request{
		Group: g.name,
		Key:   key,
	}
	response := &pb.Response{}
	err := peer.Get(request, response)
	if err != nil {
		return ByteView{}, err
	}
	return ByteView{b: response.GetValue()}, nil
}

func (g *Group) getLocally(key string) (ByteView, error) {
	bytes, err := g.getter.Get(key)
	if err != nil {
		return ByteView{}, err
	}
	value := ByteView{b: cloneBytes(bytes)}
	g.populateCache(key, value)
	return value, nil
}

func (g *Group) populateCache(key string, value ByteView) {
	g.mainCache.add(key, value)
}
