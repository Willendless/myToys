# Linematch: A simple file line search engine in java

## TODO

- [x] lexer
- [x] parser
- [x] interpreter
- [x] command line version
- [ ] gui version
- [ ] 忽略单复数
- [ ] 忽略三单，现在分词，过去式和过去分词


## search flow

1. input search expression
    + expr -> token -> AST -> SearchTarget
2. input search file path
3. try match each line in file

## expression grammar

+ Literals: *word*
+ Unary expressions: *!*
+ Binary expressions: *&&*, *||*
+ Parentheses for grouping

```python
# first version with ambiguity
expression -> literal
            | unary
            | binary
            | grouping;
literal    -> WORD;
grouping   -> "(" expression ")";
unary      -> "!" expression;
binary     -> expression operator expression
operator   -> "&&" | "||"

# second version without ambiguity
expression -> or
or         -> and ("||" and)*
and        -> primary ("&&" primary)*
primary    -> WORD
            | "(" expression ")"
            | "!" WORD
```
