package consistenthash

import (
	"strconv"
	"testing"
)

func TestHashing(t *testing.T) {
	ring := New(3, func(name []byte) uint32 {
		hash, _ := strconv.Atoi(string(name))
		return uint32(hash)
	})

	// nodes: 2, 4, 6, 12, 14, 16, 22, 24, 26
	ring.Add("6", "4", "2")

	testCases := map[string]string{
		"2":  "2",
		"17": "2",
		"23": "4",
		"25": "6",
	}

	for k, v := range testCases {
		if ring.GetNode(k) != v {
			t.Errorf("Corresponding node of %v should be %v instead of %v", k, v, ring.GetNode(k))
		}
	}

	ring.Add("8")
	testCases["27"] = "8"
	testCases["17"] = "8"

	for k, v := range testCases {
		if ring.GetNode(k) != v {
			t.Errorf("Corresponding node of %v should be %v", k, v)
		}
	}

}
