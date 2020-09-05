package lru

import (
	"gocache/list"
	"log"
)

//
// Cache is a LRU cache.
// Currently not safe for concurrent access.
//
type Cache struct {
	maxBytes int64 // 最大可使用内存
	curBytes int64 // 当前使用的内存
	ll       *list.List
	keyMap   map[string]*list.Node

	OnEvicted func(key string, value Value)
}

//
// entry is stored in element of list
//
type entry struct {
	key   string
	value Value
}

//
// Value can return how many bytes it takes
//
type Value interface {
	Len() int
}

//
// Len return the size of the LRU cache
//
func (c *Cache) Len() int {
	return c.ll.Size
}

//
// New is the constructor of cache
//
func New(maxBytes int64, onEvicted func(string, Value)) *Cache {
	return &Cache{
		maxBytes:  maxBytes,
		ll:        list.New(),
		keyMap:    map[string]*list.Node{},
		OnEvicted: onEvicted,
	}
}

//
// Get looks up corresponding value of key
// and put that entry into the tail of the list
//
func (c *Cache) Get(key string) (value Value, ok bool) {
	if nodep, ok := c.keyMap[key]; ok {
		en := c.ll.Remove(nodep).(*entry)
		nodep := c.ll.PushBack(en)
		c.keyMap[key] = nodep
		return en.value, true
	}
	return nil, false
}

//
// Evict drop least recently used item(i.e. the head of the list)
//
func (c *Cache) Evict() {
	log.Println("Evict!!!!!!!!!!")
	nodep := c.ll.Begin()
	if nodep != nil {
		c.ll.Remove(nodep)
		en := nodep.Item.(*entry)
		delete(c.keyMap, en.key)
		c.curBytes -= int64(en.value.Len()) + int64(len(en.key))
		if c.OnEvicted != nil {
			c.OnEvicted(en.key, en.value)
		}
	}
}

//
// Put entry to the cache, also move to tail of the LRU list
//
func (c *Cache) Put(key string, value Value) {
	// check if key exists
	if nodep, ok := c.keyMap[key]; ok {
		oldEn := c.ll.Remove(nodep).(*entry)
		nodep := c.ll.PushBack(&entry{key, value})
		c.keyMap[key] = nodep
		c.curBytes += int64(value.Len()) - int64(oldEn.value.Len())
	} else {
		nodep := c.ll.PushBack(&entry{key, value})
		c.keyMap[key] = nodep
		c.curBytes += int64(len(key)) + int64(value.Len())
	}
	// if curBytes > memBytes, evict tail of the cache
	for c.maxBytes != 0 && c.curBytes > c.maxBytes {
		c.Evict()
	}
}
