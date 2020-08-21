package lru

import "gocache/list"

type Cache struct {
	ll list.List
	a  int
}

func New() *Cache {
	ll = list.New()

	return nil
}
