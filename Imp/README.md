# IMP: A simple interpreter from scratch in Python

A toy interpreter wrote for a basic imperative language called IMP based on [blog serises](https://jayconrod.com/posts/37/a-simple-interpreter-from-scratch-in-python--part-1-).

Token stream--1--intermediate Result object--2--AST

## IMP syntax

```python
# assignment
x := 1
# concidtional statements
if x = 1 then
  y := 2
else
  y := 3
end
# while loops
while x < 10 do
  x := x + 1
end
# compound statements(separated by semicolons)
x := 1;
y := 2
```

## Stages of the interpreter

+ ***Lexing***: Divide source code into tokens
+ ***Parsing***: Parse tokens into abstract syntax tree(AST)
+ ***Evaluation***: Evalutate the abstract syntax tree and print the value

## Parser Library implementation


+ parser
  + ***def:*** function consume input tokens and produce AST
    + *input:* stream of tokens
    + *output:* part of the final AST and remaining tokens
  + ***implementation:***
    + callable object
    + arithmetic magic method overrided: *combinable*
+ parser combinator:
  + ***def:*** function that combine one or more parsers into one parser
    + *input:* one or more parsers
    + *output:* a parser
  + ***implementation:*** subclass of `parser` with own state and `__call__()` overrided
  + classes
    + Reserved combinator
    + TokenClass combinator
    + Concat combinator
    + Alternate combinator
    + Opt combinator
    + Process combinator
    + Exp combinator
    + Phrase combinator

## AST defination

Every syntactic element of IMP will have a corresponding class. Object of them represent nodes in AST.

1. arithmetic expressions
    + integer constant
    + variables
    + binary operations
2. boolean expressions
    + relational expressions (ex: x < 10)
    + **And** expressions (ex: x < 10 and y > 20)
    + **Or** expressions
    + **Not** expressions
3. statements: can contain both arithmetic and boolean expressions
    + assignment statements
    + compound statements
    + conditional statements
    + loop statements
