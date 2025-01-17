# Design Document

## MENU

- [part1: 双链表实现lru cache](#part-1-双链表实现lru-cache)
- [part2: 线程安全lru cache](#part-2-线程安全lru-cache)
- [part3: http服务端](#part-3-http服务端)
- [part4: 一致性哈希](#part-4-一致性哈希)
- [part5: 分布式节点](#part-5-分布式节点)
- [part6: 防止缓存击穿](#part-6-防止缓存击穿)
- [part7: 使用Protobuf通信](#part-7-使用protobuf通信)

## PART 1 双链表实现LRU CACHE

### 数据结构

1. 双链表实现

```go
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
```

+ 类似pintos中的双链表，在List结构体中封装头尾pivot，头节点的prev为nil，尾节点的post为nil，便于处理
+ 节点值使用`interface{}`通用类型

2. LRU cache实现

```go
type Cache struct {
	maxBytes int64 // 最大可使用内存
	curBytes int64 // 当前使用的内存
	ll       *list.List // 双链表
	keyMap   map[string]*list.Node // key到链表中结构的映射

	OnEvicted func(key string, value Value) // 抢占时的回调函数
}

//
// entry is stored in element of list
//
type entry struct {
	key   string
	value Value
}
```

### 算法

1. `Get()`
    + 若命中，则将元素放入链表尾部，并返回其值
2. `Put()`
    + 若命中，则将元素放入链表尾部，同时将元素内值修改，重新计算当前cache的大小
    + 若不命中，建立新元素并放入链表尾部，重新计算当前cache大小
    + 如果cache当前大小超过上限，则抢占链表首部的元素

## PART 2 线程安全LRU CACHE

### 数据结构

1. cache: 线程安全lru cache的封装

```go
type cache struct {
	mu         sync.Mutex
	lru        *lru.Cache
	cacheBytes int64
}
```

+ 用互斥锁保护cache

2. ByteView: 缓存数据的抽象

```go
//
// ByteView holds a *immutable* view of bytes in case client modify it.
//
type ByteView struct {
	b []byte
}
```

+ lru cache存储的元素类型：`ByteView`
+ `ByteView`是不可变类型，一旦创建之后，包外不可见其内部的值。仅导出处理该值的函数。

3. Group: 缓存命名空间

```go
// A Group is a cache namespace and associated data loaded spread over
type Group struct {
	name      string
	getter    Getter
	mainCache cache
}

var (
	mu     sync.RWMutex
    groups = make(map[string]*Group)
)

//
// Getter is a interface of callback function to get user data when not hit
//
type Getter interface {
	Get(key string) ([]byte, error)
}

//
// GetterFunc is a function type implement Getter interface
//
type GetterFunc func(key string) ([]byte, error)

func (f GetterFunc) Get(key string) ([]byte, error) {
	return f(key)
}
```

+ Getter接口定义缓存不命中情况下的回调函数（用于获取数据）。
+ 这里使用了传接口而非传函数，扩展性更好
+ *函数转换为接口类型*：为避免传接口时创建一个struct、实现对应接口再创建一个实例作为参数。这里直接定义函数类型，实现接口的方法，并在方法中调用自己。

### 算法

1. Get
    + 从cache中查找，若命中则返回
    + 若未命中，则使用回调获取数据，并添加至cache中

## PART 3 HTTP服务端

```go
//
// HTTPPool represents a real node in the hash ring.
//
type HTTPPool struct {
	self     string
	basePath string
}
```

+ 处理http请求的一个节点
+ `basePath`作为节点间通讯的url的前缀，例如:`http://x.com/_gocache`

### 算法

1. ServeHTTP(w http.ResponseWriter, r *http.Request)
	+ 利用`strings.SplitN(r.URL.Path[len(p.basePath):], "/", 2)`解析请求的url
	+ 第一个参数作为group名
	+ 第二个参数作为key的值

## PART 4 一致性哈希

```go
// HashRing contains all hashed keys
type HashRing struct {
	hash     Hash           // hash function
	replicas int            // number of virtual nodes of a single real node
	keys     []int          // virtual node keys (hash result in the ring)
	hashMap  map[int]string // map of virtual nodes to real node
}
```

+ 记录hash环上节点信息

### 算法

1. 加入节点
	+ 根据`replicas`个数生成虚拟节点，利用`hash`计算HashRing上的位置
	+ 将所有虚拟节点的hash值加入keys中，同时设置虚拟节点到节点名的映射
	+ 使用`sort.Ints()`将keys内的所有hash值排序
2. 获取数据存储的节点
	+ 根据数据的key计算hash值
	+ 根据hash值在`keys`中二分查找第一个大于该值的节点的hash值
	+ 通过映射获取该虚拟节点对应的真实节点的名称

## PART 5 分布式节点

+ 实现HTTP客户端，与远程服务端通信
+ 注册节点，使用一致性哈希选择节点
	+ 如果本地未缓存数据值，首先查看key对应的存储节点
		+ 如果数据值由远程节点存储，则通过http从远程获取数据
		+ 否则，使用回调函数从本地获取数据值并放入缓存中

### 数据结构

```go
type HTTPPool struct {
	self        string // base url, e.g. "https://example.net:8000"
	basePath    string // default: '/_gocache'
	mu          sync.Mutex
	peers       *consistenthash.HashRing
	HTTPClients map[string]*HTTPClient
}

type httpClient struct {
	baseURL string
}
```

### 算法

1. 整体流程
	+ api服务器是某个peer服务器的goroutine
	+ 发送http请求给api服务器
	+ api服务器调用`group`的`get`方法尝试查询数据值
	+ 若存储在远程节点，通过当前`group`中的`PeerPicker`获取到数据存储节点`HTTPClient`
	+ 发送http的get请求给远程服务器

## PART 6 防止缓存击穿

+ 实现singleflight，对并发请求只做一次查询操作

### 数据结构

```go
type call struct {
	wg  sync.WaitGroup
	val interface{}
	err error
}
```

+ 抽象一次调用请求

```go
type Group struct {
	mu sync.Mutex
	m  map[string]*call
}
```

+ 修改`Group`，并发请求时只需要一次调用

```go
type Group struct {
	name       string
	getter     Getter
	mainCache  cache
	peerPicker PeerPicker
	// use loader to guarantee key only fetched once
	loader *singleflight.Group
}
```

+ 在`Group`的`load()`方法上使用`loader`，避免同时多次访问db或远程节点

### 算法

```go
func (g *Group) Do(key string, fn func() (interface{}, error)) (interface{}, error)
```

1. 通过singleflight发起请求
2. 首先获取mutex锁，并在`Group`查找当前是否有请求的缓存结果
3. 若没有。
	+ 则创建call实例，加入map中
	+ wg+1, 释放锁
	+ 执行回调函数，得到结果值，设置call实例`Done()`
	+ 获取锁，再删除call实例
4. 若有，则在`wg`上`wait`直到完成。并获取请求的结果，释放mutex锁

## PART 7 使用Protobuf通信

+ 使用protobuf配置http get请求的格式
+ 发送http请求和返回http结果前使用`marshal`和`unmarshal`封装通信数据
