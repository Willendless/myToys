/**
 * @Author: Willendless
 * @Date: 2020-06-14
 * @Description: top main file of Linematch
 * @LastEditTime: 2020-06-16
 * @FilePath: \code\Main.java
 */

package code;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.List;
import code.Matcher;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> search expression: ");
        while (scanner.hasNextLine()) {
            String expr = scanner.nextLine();
            SearchTarget s = null;
            try {
                // lexer
                List<Token> tokens = new Tokenizer(expr).tokenizer();
                int i = 0;
                for (Token t : tokens) {
                    System.out.println("> " + "token " + i + ": " + t);
                    ++i;
                }
                // parser
                Expr ast = new Parser(tokens).parse();
                System.out.print("> " + "ast: ");
                new AstPrinter().print(ast);
                // interpreter
                s = new Interpreter().interpret(ast);
                System.out.println("> " + "interpreted value: " + s);
            } catch (LineMatchException e) {
                System.err.println(e.getMsg());
                System.exit(-1);
            }

            // matcher
            File file = null;
            System.out.print("> search file: ");
            if (scanner.hasNextLine())
                file = new File(scanner.nextLine());
            if (file == null || !file.exists()) {
                System.out.println("> file not exists");
            } else {
                try {
                    System.out.println(new Matcher(s).match(file));
                } catch (IOException e) {
                    System.out.println("> IOException");
                }
            }
            System.out.print("> search expression: ");
        }
        scanner.close();
    }

}

