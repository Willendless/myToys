package gocache

import (
	"fmt"
	"gocache/consistenthash"
	"io/ioutil"
	"log"
	"net/http"
	"net/url"
	"strings"
	"sync"
)

const defaultBasePath = "/_gocache/"
const defaultReplicas = 50

//
// HTTPPool implements PeerPicker for a pool of HTTP peers.
//
type HTTPPool struct {
	self        string // base url, e.g. "https://example.net:8000"
	basePath    string // default: '/_gocache'
	mu          sync.Mutex
	peers       *consistenthash.HashRing
	HTTPClients map[string]*HTTPClient
}

//
// NewHTTPPool initializes an HTTP pool of peers
//
func NewHTTPPool(self string) *HTTPPool {
	return &HTTPPool{
		self:     self,
		basePath: defaultBasePath,
	}
}

//
// Log info with server name
//
func (p *HTTPPool) Log(format string, v ...interface{}) {
	log.Printf("[Server %s] %s", p.self, fmt.Sprintf(format, v...))
}

//
// ServeHTTP handles all http requests
//
func (p *HTTPPool) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	if !strings.HasPrefix(r.URL.Path, p.basePath) {
		panic("HTTPPool serving unexpected path: " + r.URL.Path)
	}

	p.Log("%s %s", r.Method, r.URL.Path)
	//
	// "/<basepath>/<groupname>/<key>"
	//
	parts := strings.SplitN(r.URL.Path[len(p.basePath):], "/", 2)
	if len(parts) != 2 {
		http.Error(w, "bad request", http.StatusBadRequest)
		return
	}

	group := GetGroup(parts[0])
	key := parts[1]

	if group == nil {
		http.Error(w, "no such group: "+parts[0], http.StatusNotFound)
		return
	}

	view, err := group.Get(key)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/octet-stream")
	w.Write(view.ByteSlice())
}

//
// SetPeers set peers in the hashring, use url as peer's name
//   peers: urls of peers
//
func (p *HTTPPool) SetPeers(peers ...string) {
	p.mu.Lock()
	defer p.mu.Unlock()
	p.peers = consistenthash.New(defaultReplicas, nil)
	p.peers.Add(peers...)
	p.HTTPClients = make(map[string]*HTTPClient, len(peers))
	for _, peer := range peers {
		p.HTTPClients[peer] = &HTTPClient{baseURL: peer + p.basePath}
	}
}

//
// PickPeer picks a peer based on data's search key
//
func (p *HTTPPool) PickPeer(key string) (*HTTPClient, bool) {
	p.mu.Lock()
	defer p.mu.Unlock()
	if peer := p.peers.GetNode(key); peer != "" && peer != p.self {
		p.Log("Pick peer %s", peer)
		return p.HTTPClients[peer], true
	}
	return nil, false
}

// HTTPClient represents a http client
type HTTPClient struct {
	baseURL string
}

//
// Get send a get request to get cache data
//
func (c *HTTPClient) Get(group string, key string) ([]byte, error) {
	u := fmt.Sprintf(
		"%v%v/%v",
		c.baseURL,
		url.QueryEscape(group),
		url.QueryEscape(key),
	)
	res, err := http.Get(u)
	if err != nil {
		return nil, err
	}
	defer res.Body.Close()

	if res.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("server returned: %v", res.Status)
	}

	bytes, err := ioutil.ReadAll(res.Body)
	if err != nil {
		return nil, fmt.Errorf("reading response body: %v", err)
	}

	return bytes, nil
}
