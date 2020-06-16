/**
 * @Author: Willendless
 * @Date: 2020-06-14
 * @Description: top main file of Linematch
 * @LastEditTime: 2020-06-15
 * @FilePath: \code\Main.java
 */

package code;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import code.Token.TokenType;

import java.util.List;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> search expression: ");
        while (scanner.hasNextLine()) {
            String expr = scanner.nextLine();
            try {
                List<Token> tokens = tokenizer(expr);
                int i = 0;
                for (Token t : tokens) {
                    System.out.println("> " + "token " + i + ": " + t);
                    ++i;
                }
                System.out.print("> " + "ast ");
                new AstPrinter().print(new Parser(tokens).parse());
            } catch (LineMatchException e) {
                System.err.println(e.getMsg());
                System.exit(-1);
            }
            System.out.print("> search expression: ");
        }
        scanner.close();
    }

    public static List<Token> tokenizer(String expr) throws LineMatchException {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;
        while (pos < expr.length()) {
            boolean match = false;
            expr = expr.substring(pos);
            System.out.println("debug: pos" + pos + " " + expr);
            for (Token.TokenType t : Token.TokenType.values()) {
                Pattern p = Token.tokenPatternMap.get(t);
                Matcher m = p.matcher(expr);
                if (m.lookingAt()) {
                    if (t != TokenType.BLANK) {
                        tokens.add(new Token(t, expr.substring(0, m.end())));   
                    }
                    pos = m.end();
                    match = true;
                    break;
                }
            }
            if (!match)
                throw new LineMatchException("Fail match any token type : "
                                            + expr);
        }
        return tokens;
    }
}

