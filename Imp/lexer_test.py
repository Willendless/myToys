'''
@Author: Willendless
@Date: 2020-06-08 18:24:55
@Description: Test the functionality of lexer
@FilePath: \Imp\lexer_test.py
'''
import imp.lexer
import sys

if __name__ == '__main__':
    filename = sys.argv[1]
    file = open(filename)
    text = file.read()
    file.close()
    tokens = imp.lexer.imp_lexer(text)
    for token in tokens:
        print(token)
