'''
@Author: Willendless
@Date: 2020-06-10
@Description: parser driver
@LastEditTime: 2020-06-10
@FilePath: \Imp\parser_test.py
'''

import sys
import imp.parser
import imp.lexer

if __name__ == '__main__':
    if len(sys.argv) != 2:
        sys.stderr.write('usage: %s filename' % sys.argv[0])
        sys.exit(1)
    filename = sys.argv[1]
    with open(filename, 'r') as f:
        text = f.read()
    tokens = imp.lexer.imp_lexer(text)
    print(tokens)
    ast = imp.parser.imp_parser(tokens)
    print(ast)

