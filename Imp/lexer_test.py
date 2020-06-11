'''
@Author: Willendless
@Date: 2020-06-08 18:24:55
@Description: Test the functionality of lexer
@FilePath: \Imp\lexer_test.py
'''
import imp.lexer
import sys

if __name__ == '__main__':
    if len(sys.argv) != 2:
        sys.stderr.write('usage: %s input_filename' % sys.argv[0])
        sys.exit(1)
    filename = sys.argv[1]
    with open(filename, 'r') as f:
        text = f.read()
    tokens = imp.lexer.imp_lexer(text)
    for token in tokens:
        print(token)
