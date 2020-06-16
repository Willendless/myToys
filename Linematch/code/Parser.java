/**
 * @Author: Willendless
 * @Date: 2020-06-15
 * @Description: parser implementation
 * @LastEditTime: 2020-06-15
 * @FilePath: \code\Parser.java
 */
package code;

import java.util.List;

import code.Token.TokenType;

import static code.Token.TokenType.*;

public class Parser {
    private final List<Token> _tokens;
    private int _pos = 0;

    public Parser(List<Token> tokens) {
        _tokens = tokens;
    }

    public Expr parse() {
        try {
            return expression();
        } catch (LineMatchException e) {
            return null;
        }
    }

    private Expr expression() {
        return or();
    }

    private Expr or() {
        Expr expr = and();
        while (match(OR)) {
            Token operator = previous();
            Expr next = and();
            expr = new Expr.Binary(expr, operator, next);
        }
        return expr;
    }

    private Expr and() {
        Expr expr = primary();
        while (match(AND)) {
            Token operator = previous();
            Expr next = primary();
            expr = new Expr.Binary(expr, operator, next);
        }
        return expr;
    }

    private Expr primary() throws LineMatchException {
        if (match(WORD)) return new Expr.Literal(previous().getValue());
        if (match(NOT)) {
            if (!match(WORD))
                throw new LineMatchException("Fail to match word");
            return new Expr.Unary(
                                _tokens.get(_pos - 2),
                                new Expr.Literal(previous().getValue()));
        }
        if (match(LPAR)) {
            Expr expr = expression();
            if (!match(RPAR))
                throw new LineMatchException("Fail to match Right parenthesis");
            return new Expr.Grouping(expr);
        }
        throw new LineMatchException("primary parse can not reach here");
    }

    private boolean match(TokenType tokenType) {
        if (_pos == _tokens.size()
            || _tokens.get(_pos).getType() != tokenType)
            return false;
        advance();
        return true;
    }

    private Token advance() {
        Token token = null;
        if (_pos != _tokens.size()) {
            token = _tokens.get(_pos);
            _pos++;
        }
        return token;
    }

    private Token previous() {
        Token token = null;
        if (_pos != 0)
            token = _tokens.get(_pos - 1);
        return token;
    }
    
}