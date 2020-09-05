package list

import (
	"fmt"
	"log"
)

// Node is linked list's node
type Node struct {
	Item interface{}
	prev *Node
	post *Node
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

func (n *Node) Post() *Node {
	if n == nil || n.post == nil || n.post.post == nil {
		return nil
	}
	return n.post
}

func (n *Node) Prev() *Node {
	if n == nil || n.prev == nil || n.prev.prev == nil {
		return nil
	}
	return n.prev
}

func (n *Node) IsEnd() bool {
	if n == nil || n.post == nil {
		log.Fatalln("Invalid Node pointer")
	}
	if n.post.post == nil {
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
	n := &Node{v, nil, nil}
	n.post = &ll.tail
	n.prev = ll.tail.prev
	n.post.prev = n
	n.prev.post = n
	return n
}

func (ll *List) PushFront(v interface{}) *Node {
	ll.Size++
	n := &Node{v, nil, nil}
	n.prev = &ll.head
	n.post = ll.head.post
	n.post.prev = n
	n.prev.post = n
	return n
}

func (ll *List) Remove(n *Node) interface{} {
	if n == nil {
		panic(fmt.Sprintf("Entry Nil pointer"))
	}
	if n.prev == nil {
		panic("prev nil")
	}
	if n.post == nil {
		panic("prev nil")
	}

	ll.Size--
	n.prev.post = n.post
	n.post.prev = n.prev
	n.prev = nil
	n.post = nil
	return n.Item
}
