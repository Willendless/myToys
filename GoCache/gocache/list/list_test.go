package list

import "testing"

func TestBasic(t *testing.T) {
	result := [3]int{1, 2, 3}
	ll := New()
	ll.PushFront(1)
	ll.PushBack(2)
	ll.PushBack(3)
	i := 0
	for en := ll.Begin(); en != nil; en = en.Post() {
		if en.Value != result[i] {
			t.Error("Entry fail: ", result[i], " vs ", en.Value)
		}
		i++
	}
}
