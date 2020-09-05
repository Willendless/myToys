# Design Document

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
type httpClient struct {
	baseURL string
}
```


## PART 6 防止缓存击穿

## PART 7 使用Protobuf
