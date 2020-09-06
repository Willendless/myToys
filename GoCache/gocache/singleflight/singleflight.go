package singleflight

import (
	"sync"
)

type call struct {
	wg  sync.WaitGroup
	val interface{}
	err error
}

type Group struct {
	mu sync.Mutex
	m  map[string]*call
}

func (g *Group) Do(key string, fn func() (interface{}, error)) (interface{}, error) {
	g.mu.Lock()
	// check existence of call instance
	if c, ok := g.m[key]; ok {
		g.mu.Unlock()
		c.wg.Wait()
		return c.val, c.err
	}
	// set wg initial state
	if g.m == nil {
		g.m = map[string]*call{}
	}
	g.m[key] = &call{}
	g.m[key].wg.Add(1)
	c := g.m[key]
	g.mu.Unlock()
	c.val, c.err = fn()
	c.wg.Done()
	g.mu.Lock()
	delete(g.m, key)
	g.mu.Unlock()
	return c.val, c.err
}
