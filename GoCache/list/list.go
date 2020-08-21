package list

import "fmt"

// Entry is linked list's node
type Entry struct {
	Value interface{}
	prev  *Entry
	post  *Entry
}

type List struct {
	Size int
	head Entry
	tail Entry
}

func New() *List {
	var ll List
	ll.Size = 0
	ll.head.post = &ll.tail
	ll.tail.prev = &ll.head
	return &ll
}

func (en *Entry) Post() *Entry {
	if en == nil || en.post.post == nil || en.post == nil {
		return nil
	}
	return en.post
}

func (en *Entry) Prev() *Entry {
	if en == nil || en.prev.prev == nil || en.prev == nil {
		return nil
	}
	return en.prev
}

func (en *Entry) IsEnd() bool {
	if en.post.post == nil {
		return true
	}
	return false
}

func (ll *List) Begin() *Entry {
	if ll.head.post == &ll.tail {
		return nil
	}
	return ll.head.post
}

func (ll *List) PushBack(v interface{}) *Entry {
	ll.Size++
	en := &Entry{v, nil, nil}
	en.post = &ll.tail
	en.prev = ll.tail.prev
	en.post.prev = en
	en.prev.post = en
	return en
}

func (ll *List) PushFront(v interface{}) *Entry {
	ll.Size++
	en := &Entry{v, nil, nil}
	en.prev = &ll.head
	en.post = ll.head.post
	en.post.prev = en
	en.prev.post = en
	return en
}

func (ll *List) Remove(en *Entry) interface{} {
	if en == nil {
		panic(fmt.Sprintf("Nil pointer"))
	}

	ll.Size--
	en.prev.post = en.post
	en.post.prev = en.prev
	en.prev = nil
	en.post = nil
	return en.Value
}
