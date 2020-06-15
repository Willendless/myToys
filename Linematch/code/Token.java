/**
 * @Author: Willendless
 * @Date: 2020-06-14
 * @Description: Class represents token.
 * @LastEditTime: 2020-06-14
 * @FilePath: \code\Token.java
 */
package code;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Token {

    public static enum TokenType{
        LPAR,
        RPAR,
        NOT,
        OR,
        AND,
        WORD,
        BLANK
    }

    private static final Pattern tokenPatterns[] = new Pattern[] {
        Pattern.compile("\\("),
        Pattern.compile("\\)"),
        Pattern.compile("!"),
        Pattern.compile("\\|\\|"),
        Pattern.compile("&&"),
        Pattern.compile("[a-zA-Z]+"),
        Pattern.compile("\\s")
    };

    public static final Map<TokenType, Pattern> tokenPatternMap;

    static {
        tokenPatternMap = new HashMap<>();
        for (TokenType t : TokenType.values()) {
            tokenPatternMap.put(t, tokenPatterns[t.ordinal()]);
        }
    }

    private TokenType _type;
    private String _value;

    public Token(TokenType t, String value) {
        _type = t;
        _value = value;
    }

    public TokenType getType() {
        return _type;
    }

    public String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return "{" + _type + " : " + _value + "}";
    }
    
}