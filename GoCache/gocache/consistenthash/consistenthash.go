package consistenthash

import (
	"hash/crc32"
	"sort"
	"strconv"
)

// Hash maps byts to uint32
type Hash func(data []byte) uint32

// HashRing contains all hashed keys
type HashRing struct {
	hash     Hash           // hash function
	replicas int            // number of virtual nodes of a single real node
	keys     []int          // virtual node keys (hash result in the ring)
	hashMap  map[int]string // map of virtual nodes to real node
}

//
// New creates a HashRing instance
//
func New(replicas int, fn Hash) *HashRing {
	m := &HashRing{
		replicas: replicas,
		hash:     fn,
		hashMap:  make(map[int]string),
	}
	if m.hash == nil {
		m.hash = crc32.ChecksumIEEE
	}
	return m
}

//
// Add adds new nodes to the hash
//
func (m *HashRing) Add(names ...string) {
	for _, name := range names {
		for i := 0; i < m.replicas; i++ {
			hash := int(m.hash([]byte(strconv.Itoa(i) + name)))
			// log.Printf("add node %v", m.hash([]byte(strconv.Itoa(i)+name)))
			m.keys = append(m.keys, hash)
			m.hashMap[hash] = name
		}
	}
	sort.Ints(m.keys)
}

//
// GetNode gets the cache node in the ring
// according to the hash result of the data key
//
func (m *HashRing) GetNode(key string) string {
	// no nodes in the ring yet
	if len(m.keys) == 0 {
		return ""
	}
	// hash result of the data key
	hash := int(m.hash([]byte(key)))
	// get the first i where m.keys[i] >= hash
	index := sort.Search(len(m.keys), func(i int) bool {
		return m.keys[i] >= hash
	})
	return m.hashMap[m.keys[index%len(m.keys)]]
}
