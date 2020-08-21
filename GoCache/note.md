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
