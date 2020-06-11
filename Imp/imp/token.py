'''
@Author: Willendless
@Date: 2020-06-10
@Description: token definition
@LastEditTime: 2020-06-10
@FilePath: \Imp\imp\token.py
'''

# token class
RESERVED = 'RESERVED'
INT = 'INT'
ID = 'ID'

# token_exprs
token_exprs = [
    # whitespace
    (r'[ \t\n]+',       None),
    # comments
    (r'#[^\n]*',        None),
    # reserved operator
    (r'\:=',            RESERVED),
    (r'\(',             RESERVED),
    (r'\)',             RESERVED),
    (r';',              RESERVED),
    (r'\+',             RESERVED),
    (r'-',              RESERVED),
    (r'\*',             RESERVED),
    (r'/',              RESERVED),
    (r'<=',             RESERVED),
    (r'>=',             RESERVED),
    (r'<',              RESERVED),
    (r'>',              RESERVED),
    (r'=',              RESERVED),
    (r'!=',             RESERVED),
    (r'and',            RESERVED),
    (r'or',             RESERVED),
    (r'not',            RESERVED),
    (r'if',             RESERVED),
    (r'then',           RESERVED),
    (r'else',           RESERVED),
    (r'while',          RESERVED),
    (r'do',             RESERVED),
    (r'end',            RESERVED),
    # INT
    (r'(0|[1-9][0-9]*)',      INT),
    # ID
    (r'[a-zA-Z][a-zA-Z0-9]*', ID),
]
