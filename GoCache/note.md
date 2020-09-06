# gocache note

## 项目配置

### 国内源

+ `export GO111MODULE=on`
+ `export GOPROXY=https://goproxy.cn,direct`

### 模块与包

+ 首先需要初始化模块: `go mod init gocache`
+ 其次每个文件夹对应一个package。
    + 例如双链表的模块名为`list`，当`lru`模块引用时需要使用`import "gocache/list"`
    + 使用`list`模块导出的实体时需要模块名加实体名`list.List`
+ go模块的相对引用，可以通过修改mod文件达到
    + 例如使用`replace gocache => ./gocache`

### 注释

+ 导出实体(即首字母大写)的注释需要以实体名开头

## go practice

### 函数类型实现接口

+ 将函数类型*泛化*成接口类型的方法
    1. 首先定义函数类型
    2. 实现接口的方法，并在方法中调用自己（需要保证函数类型和返回值和接口中方法相同）
+ 意义：code for change
    + 使用接口作为参数还是使用函数作为参数
    + 将某个/某几个方法封装成接口，便于扩展。例：net/http中的Handler。
+ 使用：
    + 函数类型对命名/匿名函数强制类型转换后作为接口参数传递
    + 对任意类型并实现接口的方法作为接口参数传递

### http 标准库

+ 创建任意类型，并实现`ServeHTTP`方法
+ 调用http.ListenAndServe启动http服务，第一个参数是服务启动地址，第二个参数是某个实现了`ServeHTTP`方法的对象

### log日志封装

```go
func (p *HTTPPool) Log(format string, v ...interface{}) {
	log.Printf("[Server %s] %s", p.self, fmt.Sprintf(format, v...))
}
```

### 二分查找

+ Search uses binary search to find and return the smallest index i in [0, n) at which f(i) is true
+ 如果均为false则返回第一个参数
+ `sourt.Search(len(data), func(i int) bool { return data[i] >= x })`

### 条件变量和waitgroup的区别

+ *是否有内部状态*: 条件变量没有状态，waitgroup需要等待的数量即是状态
+ *是否有后效性*: 条件变量没有后效性，每一次`wait()`都需要之后的`broadcast`或`signal`来唤醒。而`waitgroup`在全部`done()`后发生的`wait()`不需要等待。

### map的delete函数

+ 删除后，内存并不释放，只设置一个empty标记
+ 保证能够在遍历map的同时删除元素
+ 真正释放内存需要`map = nil`

## 其它

### 查看端口使用情况并删除占用的进程

1. `netstat -ntlp`
    + -n: 拒绝显示别名
    + -l: 仅列出正在端口监听的服务
    + -t: 仅显示tcp相关选项
    + -p: 显示建立相关链接的程序名
2. kill -9

### vim全局替换

1. 替换当前行第一个`:s/old/new/`
2. 替换改行所有`:s/old/new/g`
3. 多行替换`:50, 100 s/old/new/g`
4. 整个文件替换`1,$s/old/new/g`
    + `%`等价于`1, $`
    + `%s/old/new/g`


### markdown内部锚点

+ 标题的大写字母转换为小写
+ 空格转换为`-`代替
+ 多级序号去除`.`

```
[Github标题1](#github标题1)

### Github标题1

[Github 标题2 Test](#github-标题2-test)

### Github 标题2 Test

[2.3. Github 标题](#23-github-标题)

### 2.3. Github 标题
```

### 配置protobuf

1. 下载https://github.com/protocolbuffers/protobuf/releases发布版
2. `sudo 7z x pro... -o/usr/local`解压
3. 安装`protoc-gen-go`以将`.proto`文件转换为golang文件
    + `go get -u github.com/golang/protobuf/protoc-gen-go`
    + 将自动安装到`$GOPATH/bin`目录下
    + 环境变量中加入`$(go env GOPATH)/bin`

### protobuf使用

1. 定义message
2. 定义service
