# DonkeyDB: A toy database management system in c

A toy database management system based on [blog series](https://cstack.github.io/db_tutorial/)

## Arch

```scala
tokenizer -> parser -> code generator -> virtual machine -> b-tree -> pager -> os interface
```

### front end

+ *input*: sql query
+ *output*: vm bytecode
  + tokenizer
  + parser
  + code generator

### back end

+ *vm*: take bytecode as input and performs operations on tables or indexes, which stored in B-tree
+ *B-tree*: each node is one page in length, retrieve or save a page by issuing commands to the pager
+ *pager*: receives commands to r/w pages of data and keeps a cache of recently-accessed pages in memory
+ *os interface*: layer that differs depending on os platform
