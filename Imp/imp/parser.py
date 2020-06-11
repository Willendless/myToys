'''
@Author: Willendless
@Date: 2020-06-09
@Description: parser implementation
@LastEditTime: 2020-06-10
@FilePath: \Imp\imp\parser.py
'''

class Equality(object):
    def __eq__(self, other):
        return isinstance(other, self.__class__) and \
               self.__dict__ == other.__dict__
    
    def __ne__(self, other):
        return not __eq__(self, other)

# AST represent class

# Arithmetic expression
class ArithExp(Equality):
    """base class of arithmetic expression"""

class ArithInt(ArithExp):
    def __init__(self, i):
        self.i = i
    
    def __repr__(self):
        return 'ArithInt(%d)' % self.i

class ArithVal(ArithExp):
    def __init__(self, value):
        self.value = value

    def __repr__(self):
        return 'ArithVal(%s)' % self.value

class ArithBinExp(ArithExp):
    def __init__(self, op, left, right):
        self.op = op
        self.left = left
        self.right = right
    
    def __repr__(self):
        return 'ArithBinExp(%s, %s, %s)' % (self.op, self.left, self.right)

# Boolean expression
class BoolExp(Equality):
    pass

class BoolRelExp(BoolExp):
    def __init__(self, op, left, right):
        self.op = op
        self.left = left
        self.right = right
    
    def __repr__(self):
        return 'BoolRelExp(%s, %s, %s)' % (self.op, self.left, self.right)

class BoolAndExp(BoolExp):
    def __init__(self, left, right):
        self.left = left
        self.right = right
    
    def __repr__(self):
        return 'BoolAndExp(%s, %s)' % (self.left, self.right)

class BoolOrExp(BoolExp):
    def __init__(self, left, right):
        self.left = left
        self.right = right
    
    def __repr__(self):
        return 'BoolOrExp(%s, %s)' % (self.left, self.right)

class BoolNotExp(BoolExp):
    def __init__(self, exp):
        self.exp = exp
    
    def __repr__(self):
        return 'BoolNotExp(%s)' % (self.exp)

# statement

class Statement(Equality):
    pass

class StatementAssign(Statement):
    def __init__(self, var, expr):
        self.var = var
        self.expr = expr

    def __repr__(self):
        return 'StatementAssign(%s, %s)' % (self.var, self.expr)

class StatementCompound(Statement):
    def __init__(self, first, second):
        self.first = first
        self.second = second
    
    def __repr__(self):
        return 'StatementCompound(%s, %s)' % (self.first, self.second)

class StatementIf(Statement):
    def __init__(self, condition, true_statement, false_statement):
        self.condition = condition
        self.true_statement = true_statement
        self.false_statement = false_statement
    
    def __repr__(self):
        return 'StatementIf(%s, %s, %s)' % (self.condition, self.true_statement, self.false_statement)

class StatementWhile(Statement):
    def __init__(self, condition, body_statement):
        self.condition = condition
        self.body_statement = body_statement

    def __repr__(self):
        return 'StatementWhile(%s, %s)' % (self.condition, self.body_statement)


# primitives parser

from .parselib import *
from .token import *

# keyword parser generator
def keyword(kw):
    return Reserved(kw, RESERVED)

# INT parser generator
Int = TokenClass(INT) ^ (lambda i: int(i))

# ID 
Id = TokenClass(ID)


# arithmetic parser

# value parser generator
def arith_value():
    return Int ^ (lambda i: ArithInt(i)) \
         | Id ^ (lambda i: ArithVal(i))

# group parser generator
def process_group(parsed):
    ((_, p), _) = parsed
    return p

def arith_group():
    return keyword('(') + Lazy(arith_exp) + keyword(')') ^ process_group

# term parser generator
# term: self-contained expression
def arith_term():
    return arith_value() | arith_group()

# arith expression parser generator
arith_precedence_levels = [
    ['*', '/'],
    ['+', '-']
]

from functools import reduce

def same_level_op_parser_gen(ops):
    op_parsers = [keyword(op) for op in ops]
    parser = reduce(lambda l, r: l | r, op_parsers)
    return parser

def precedence_parser_gen(term_parser, precedence_levels, combine_func_gen):
    def op_parser_gen(precedence_level):
        return same_level_op_parser_gen(precedence_level) ^ combine_func_gen
    parser = term_parser * op_parser_gen(precedence_levels[0])
    for precedence_level in precedence_levels[1:]:
        parser = parser * op_parser_gen(precedence_level)
    return parser

def process_bin_exp(op):
    return lambda l, r: ArithBinExp(op, l, r)

def arith_exp():
    return precedence_parser_gen(arith_term(),
                                 arith_precedence_levels,
                                 process_bin_exp);

# boolean expression parser

# boolean relation expression parser generator
def bool_rel():
    bool_ops = ['<', '<=', '>', '>=', '=', '!=']
    def process_bool_rel(parsed):
        ((left, op), right) = parsed
        return BoolRelExp(op, left, right)
    return arith_exp() + same_level_op_parser_gen(bool_ops) + arith_exp() ^ process_bool_rel

# boolean not expression parser generator
def bool_not():
    return keyword('not') + Lazy(bool_term) ^ (lambda parsed: BoolNotExp(parsed[1]))

# boolean group expression parser generator
def bool_group():
    return keyword('(') + Lazy(bool_exp) + keyword(')') ^ process_group

# boolean term expression parser generator
def bool_term():
    return bool_rel() \
         | bool_not() \
         | bool_group()

# boolean top expression parser generator
def bool_exp():
    bool_precedence_levels = [
        ['and'],
        ['or']
    ]
    def process_bool_bin_func_gen(op):
        if op == 'and':
            return lambda l, r: BoolAndExp(l, r)
        elif op == 'or':
            return lambda l, r: BoolOrExp(l, r)
        else:
            raise RuntimeError('unknown logic operator: ' + op)
    return precedence_parser_gen(bool_term(),
                                 bool_precedence_levels,
                                 process_bool_bin_func_gen)

# statement parser

# assin statement
def statement_assign():
    def process_assign_func_gen(parsed):
        ((name, _), exp) = parsed
        return StatementAssign(name, exp)
    return Id + keyword(':=') + arith_exp() ^ process_assign_func_gen

# list statements
def statement_list():
    separator = keyword(';') ^ (lambda x: lambda l, r: StatementCompound(l, r))
    return Exp(statement_top(), separator)

# if statement
def statement_if():
    def process_if_func_gen(parsed):
        (((((_, cond), _), true_statement), branch_parsed), _) = parsed
        if branch_parsed:
            (_, false_statement) = branch_parsed
        else:
            false_statement = None
        return StatementIf(cond, true_statement, false_statement)
    return keyword('if') + bool_exp() \
            + keyword('then') + Lazy(statement_list) \
            + Opt(keyword('else') + Lazy(statement_list)) \
            + keyword('end') ^ process_if_func_gen


# while statement
def statement_while():
    def process_while_func_gen(parsed):
        ((((_, cond), _), statement_body), _) = parsed
        return StatementWhile(cond, statement_body)
    return keyword('while') + bool_exp() \
            + keyword('do') + Lazy(statement_list) \
            + keyword('end') ^ process_while_func_gen

# statement top
def statement_top():
    return statement_assign() \
         | statement_if() \
         | statement_while()

# imp parser
def imp_parser(tokens):
    def parser():
        return Phrase(statement_list())
    ast = parser()(tokens, 0)
    return ast
