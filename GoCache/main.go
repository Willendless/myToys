package main

import (
	"flag"
	"fmt"
	"gocache"
	"log"
	"net/http"
)

var db = map[string]string{
	"Tom":  "630",
	"Jack": "589",
	"Sam":  "567",
}

func startAPIServer(apiAddr string, g *gocache.Group) {
	http.Handle("/api", http.HandlerFunc(
		func(w http.ResponseWriter, r *http.Request) {
			key := r.URL.Query().Get("key")
			view, err := g.Get(key)
			if err != nil {
				http.Error(w, err.Error(), http.StatusInternalServerError)
				return
			}
			w.Header().Set("Content-Type", "application/octet-stream")
			w.Write(view.ByteSlice())
		}))
	log.Println("API server is runing at", apiAddr)
	log.Fatal(http.ListenAndServe(apiAddr[7:], nil))
}

func main() {
	var port int
	var api bool
	flag.IntVar(&port, "port", 8001, "Gocache server port")
	flag.BoolVar(&api, "api", false, "Start a api server?")
	flag.Parse()
	// default api addr
	apiAddr := "http://localhost:9999"
	// use url as node's name
	addrMap := map[int]string{
		8001: "http://localhost:8001",
		8002: "http://localhost:8002",
		8003: "http://localhost:8003",
	}
	names := []string{}
	for _, v := range addrMap {
		names = append(names, v)
	}
	// create a new group
	g := gocache.NewGroup("scores", 2<<10, gocache.GetterFunc(
		func(key string) ([]byte, error) {
			log.Println("[DB] search key", key)
			if v, ok := db[key]; ok {
				return []byte(v), nil
			}
			return nil, fmt.Errorf("%s not exist", key)
		}))
	// create api server if api equals true
	if api {
		go startAPIServer(apiAddr, g)
	}
	bootNode := gocache.NewHTTPPool(addrMap[port])
	bootNode.SetPeers(names...)
	g.RegisterPeerPicker(bootNode)
	log.Println("gocache is running at", addrMap[port])
	log.Fatal(http.ListenAndServe(addrMap[port][7:], bootNode))
}
