/**
 * @Author: Willendless
 * @Date: 2020-06-16
 * @Description: tokenizer implement
 * @LastEditTime: 2020-06-16
 * @FilePath: \code\Tokenizer.java
 */
package code;

import java.util.List;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import code.Token.TokenType;

public class Tokenizer {

    String text;

    public Tokenizer(String text) {
        this.text = text;
    }

    public List<Token> tokenizer() throws LineMatchException {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;
        while (pos < text.length()) {
            boolean match = false;
            text = text.substring(pos);
            for (Token.TokenType t : Token.TokenType.values()) {
                Pattern p = Token.tokenPatternMap.get(t);
                Matcher m = p.matcher(text);
                if (m.lookingAt()) {
                    if (t != TokenType.BLANK) {
                        tokens.add(new Token(t, text.substring(0, m.end())));   
                    }
                    pos = m.end();
                    match = true;
                    break;
                }
            }
            if (!match)
                throw new LineMatchException("Fail match any token type : "
                                            + text);
        }
        return tokens;
    }
    
}
