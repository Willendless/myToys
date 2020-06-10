'''
@Author: Willendless
@Date: 2020-06-08
@Description: parse library implementation
@LastEditTime: 2020-06-10
@FilePath: \Imp\imp\parselib.py
'''

class Result(object):
    """return value of parser

    Every parser will return a Result object
    on success or None on failure
    """
    def __init__(self, value, pos):
        self.value = value
        self.pos = pos
    
    def __repr__(self):
        return 'Result(%s, %d)' % (self.value, self.pos)


class Parser(object):
    """base class of parser"""
    def __call__(self, tokens, pos):
        """ method actually does the parsing"""
        return None

    def __add__(self, other):
        return Concat(self, other)

    def __mul__(self, other):
        return Exp(self, other)

    def __or__(self, other):
        return Alternate(self, other)

    def __xor__(self, func):
        return Process(self, func)

# Basic leaf parser combinator
class Reserved(Parser):
    """parse reserved keyword
    
        both value and token_class should match
        otherwise return None
    """
    def __init__(self, value, token_class):
        self.value = value
        self.token_class = token_class
    
    def __call__(self, tokens, pos):
        if pos < len(tokens) and \
            tokens[pos][0] == self.value and \
            tokens[pos][1] is self.token_class:
            return Result(tokens[pos][0], pos + 1)
        else:
            return None

class TokenClass(Parser):
    """parse token_class ID and INT
    
        only token_class should match
        otherwise return None
    """
    def __init__(self, token_class):
        self.token_class = token_class
    
    def __call__(self, tokens, pos):
        if pos < len(tokens) and \
            tokens[pos][1] is self.token_class:
            return Result(tokens[pos][0], pos + 1)
        else:
            return None

# Parser combinator
class Concat(Parser):
    """ apply left parser and right parser"""
    def __init__(self, left, right):
        self.left = left
        self.right = right
    
    def __call__(self, tokens, pos):
        left_result = self.left(tokens, pos)
        if left_result:
            right_result = self.right(tokens, left_result.pos)
            if right_result:
                combined_value = (left_result.value, right_result.value)
                return Result(combined_value, right_result.pos)
        return None

class Alternate(Parser):
    """ apply left parser or right parser if left fail"""
    def __init__(self, left, right):
        self.left = left
        self.right = right

    def __call__(self, tokens, pos):
        left_result = self.left(tokens, pos)
        if left_result:
            return left_result
        else:
            right_result = self.right(tokens, pos)
            return right_result

class Opt(Parser):
    """apply parser if match"""
    def __init__(self, parser):
        self.parser = parser
    
    def __call__(self, tokens, pos):
        opt_result = self.parser(tokens, pos)
        if opt_result:
            return opt_result
        else:
            return Result(None, pos)

class Process(Parser):
    """result object processsing parser"""
    def __init__(self, parser, process_func):
        self.process_func = process_func
        self.parser = parser

    def __call__(self, tokens, pos):
        result = self.parser(tokens, pos)
        if result:
            result.value = process_func(result.value)
            return result
        else:
            return None

class Lazy(Parser):
    """lazy parser combinator for recursive parsing
    
    Attributes:
        parser: lazy generated parser
        parser_gen: lazy parser generator function

    """
    def __init__(self, parser_gen):
        self.parser = None
        self.parser_gen = parser_gen

    def __call__(self, tokens, pos):
        if self.parser is None:
            self.parser = parser_gen()
        return self.parser(tokens, pos)


class Phrase(Parser):
    """top level parser combinator
    
        fail if cannot consume all tokens
    """
    def __init__(self, parser):
        self.parser = parser
    
    def __call__(self, tokens, pos):
        result = self.parser(tokens, pos)
        if result and result.pos == len(tokens):
            return result
        else:
            return None

class Exp(Parser):
    """expression parser combinator

    recursive descent parser, parse expression
    from left to right with separator in between,
    eliminate left recursion

    Attributes:
        parser: item parser
        separator_parser: separator parser

    """
    def __init__(self, parser, separator_parser):
        self.parser = parser
        self.separator_parser = separator_parser
    
    def __call__(self, tokens, pos):
        result = self.parser(tokens, pos)

        def process_fun_gen(parsed):
            (op_func, right) = parsed
            return op_func(result.value, right)

        next_parser = self.separator_parser + self.parser ^ process_fun_gen

        next_result = result
        while next_result:
            next_result = next_parser(tokens, next_result.pos)
            if next_result:
                result = next_result
        return result
