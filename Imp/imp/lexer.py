'''
@Author: Willendless
@Date: 2020-06-08
@Description: lexer implementation
@LastEditTime: 2020-06-10
@FilePath: \Imp\imp\lexer.py
'''

from .lexlib import lex
from .token import token_exprs

def imp_lexer(text):
    """lexing input text and return token lists"""
    return lex(text, token_exprs)
