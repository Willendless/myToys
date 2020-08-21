package main

import (
	"fmt"
	"gocache/ll"
)

func main() {
	ll := ll.New()
	ll.PushFront(1)
	ll.PushBack(2)
	for en := ll.Begin(); en != nil; en = en.Post() {
		fmt.Println(en)
	}

}
