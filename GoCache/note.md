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
+ 使用：函数类型对命名/匿名函数强制类型转换后作为接口参数传递
