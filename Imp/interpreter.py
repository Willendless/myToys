'''
@Author: Willendless
@Date: 2020-06-11
@Description: interpreter top file
@LastEditTime: 2020-06-11
@FilePath: \Imp\interpreter.py
'''

import sys
from imp.parser import imp_parser
from imp.lexer import imp_lexer

def usage():
    sys.stderr.write('Usage: imp filename\n')
    sys.exit(1)

if __name__ == '__main__':
    if len(sys.argv) != 2:
        usage()
    with open(sys.argv[1], 'r') as f:
        text = f.read()
    tokens = imp_lexer(text)
    ast = imp_parser(tokens)
    if not ast:
        sys.stderr.write('Parse error\n')
        sys.exit(1)
    env = {}
    ast.value.eval(env)

    sys.stdout.write('Final variables values:\n')
    for name in env:
        sys.stdout.write('%s: %s\n' % (name, env[name]))
