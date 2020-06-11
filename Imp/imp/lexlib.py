'''
@Author: Willendless
@Date: 2020-06-08
@Description: lex library implementation
@LastEditTime: 2020-06-11
@FilePath: \Imp\imp\lexlib.py
'''
import sys
import re

def lex(text, token_exprs):
    """Lexing the input text

    Iterate all input text, using regex pattern from
    token_exprs to match them. If match, then append it 
    into the output token list. Abort otherwise.

    Args:
        text: input source imp file
        token_exprs: (regex pattern, token class) high priority first
    Returns:
        tokens: list of tokens (lexeme, token_class)
    """
    pos = 0
    tokens = []
    while pos < len(text):
        match = None
        # first match
        for token_expr in token_exprs:
            pattern, token_class = token_expr
            regex = re.compile(pattern)
            match = regex.match(text, pos)
            if match:
                if token_class:
                    token = (match.group(0), token_class)
                    tokens.append(token)
                break
        if not match:
            sys.stderr.write('Illegal character: %s\\n' % text[pos])
            sys.exit(1)
        else:
            pos = match.end(0)
    return tokens
