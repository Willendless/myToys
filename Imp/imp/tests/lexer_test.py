'''
@Author: Willendless
@Date: 2020-06-08 15:14:04
@Description: test for lexer
@FilePath: \Imp\imp\tests\lexer_test.py
'''

import sys

from .. import lexer

if __name__ == '__main__':
    filename = sys.argv[1]
    file = open(filename)
    text = file.read()
    file.close()
    tokens = lexer.imp_lexer(text)
    for token in tokens:
        print(token)
