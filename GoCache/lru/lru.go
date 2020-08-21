package lru

import "gocache/list"

// Cache is a LRU cache. Currently not safe for concurrent access.
type Cache struct {
	maxBytes int64 // 最大可使用内存
	curBytes int64 // 当前使用的内存
	ll       *list.List
	keyMap   map[string]*list.Node

	OnEvicted func(key string, value Value)
}

// entry is stored in element of list
type entry struct {
	key   string
	value Value
}

// Value can return how many bytes it takes
type Value interface {
	Len() int
}

func (c *Cache) Len() int {
	return c.ll.Size
}

// New is the constructor of cache
func New(maxBytes int64, onEvicted func(string, Value)) *Cache {
	return &Cache{
		maxBytes:  maxBytes,
		ll:        list.New(),
		keyMap:    make(map[string]*list.Node),
		OnEvicted: onEvicted,
	}
}

// Get looks up corresponding value of key
func (c *Cache) Get(key string) (value Value, ok bool) {
	if enp, ok := c.keyMap[key]; ok {
		value := c.ll.Remove(enp).(*entry)
		c.ll.PushBack(value)
		ret := enp.Value.(*entry)
		return ret.value, true
	}
	return nil, false
}

// Evict drop least recently used item
func (c *Cache) Evict() {
	en := c.ll.Begin()
	if en != nil {
		c.ll.Remove(en)
		enValue := en.Value.(*entry)
		delete(c.keyMap, enValue.key)
		c.curBytes -= int64(len(enValue.key)) + int64(enValue.value.Len())
		if c.OnEvicted != nil {
			c.OnEvicted(enValue.key, enValue.value)
		}
	}
}

// Put entry to the cache
func (c *Cache) Put(key string, value Value) {
	// check if key exists
	if enp, ok := c.keyMap[key]; ok {
		c.ll.Remove(enp)
		c.ll.PushBack(enp)
		data := enp.Value.(*entry)
		c.curBytes += int64(value.Len()) - int64(data.value.Len())
		data.value = value
	} else {
		enp := c.ll.PushBack(&entry{key, value})
		c.keyMap[key] = enp
		c.curBytes += int64(len(key)) + int64(value.Len())
	}
	// if curBytes > memBytes, evict tail of the cache
	for c.maxBytes != 0 && c.curBytes > c.maxBytes {
		c.Evict()
	}
}
