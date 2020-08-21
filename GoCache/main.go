package main

import (
	"fmt"
	"gocache/list"
)

func main() {
	ll := list.New()
	ll.PushFront(1)
	ll.PushBack(2)
	for en := ll.Begin(); en != nil; en = en.Post() {
		fmt.Println(en)
	}

}
