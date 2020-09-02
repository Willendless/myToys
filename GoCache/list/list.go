package list

import (
	"fmt"
	"log"
)

// Node is linked list's node
type Node struct {
	Value interface{}
	prev  *Node
	post  *Node
}

type List struct {
	Size int
	head Node
	tail Node
}

func New() *List {
	ll := new(List)
	ll.head.post = &ll.tail
	ll.tail.prev = &ll.head
	return ll
}

func (en *Node) Post() *Node {
	if en == nil || en.post == nil || en.post.post == nil {
		return nil
	}
	return en.post
}

func (en *Node) Prev() *Node {
	if en == nil || en.prev == nil || en.prev.prev == nil {
		return nil
	}
	return en.prev
}

func (en *Node) IsEnd() bool {
	if en == nil || en.post == nil {
		log.Fatalln("Invalid Node pointer")
	}
	if en.post.post == nil {
		return true
	}
	return false
}

func (ll *List) Begin() *Node {
	if ll.head.post == &ll.tail {
		return nil
	}
	return ll.head.post
}

func (ll *List) End() *Node {
	if ll.tail.prev == &ll.head {
		return nil
	}
	return ll.tail.prev
}

func (ll *List) PushBack(v interface{}) *Node {
	ll.Size++
	en := &Node{v, nil, nil}
	en.post = &ll.tail
	en.prev = ll.tail.prev
	en.post.prev = en
	en.prev.post = en
	return en
}

func (ll *List) PushFront(v interface{}) *Node {
	ll.Size++
	en := &Node{v, nil, nil}
	en.prev = &ll.head
	en.post = ll.head.post
	en.post.prev = en
	en.prev.post = en
	return en
}

func (ll *List) Remove(en *Node) interface{} {
	if en == nil || en.prev == nil || en.post == nil {
		panic(fmt.Sprintf("Nil pointer"))
	}

	ll.Size--
	en.prev.post = en.post
	en.post.prev = en.prev
	en.prev = nil
	en.post = nil
	return en.Value
}
