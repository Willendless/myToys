/**
 * @Author: Willendless
 * @Date: 2020-06-14
 * @Description: top main file of Linematch
 * @LastEditTime: 2020-06-16
 * @FilePath: \code\Main.java
 */

package code;

import java.util.Scanner;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> search expression: ");
        while (scanner.hasNextLine()) {
            String expr = scanner.nextLine();
            try {
                List<Token> tokens = new Tokenizer(expr).tokenizer();
                int i = 0;
                for (Token t : tokens) {
                    System.out.println("> " + "token " + i + ": " + t);
                    ++i;
                }
                System.out.print("> " + "ast: ");
                Expr ast = new Parser(tokens).parse();
                new AstPrinter().print(ast);
                System.out.println(new Interpreter().interpret(ast));
            } catch (LineMatchException e) {
                System.err.println(e.getMsg());
                System.exit(-1);
            }
            System.out.print("> search expression: ");
        }
        scanner.close();
    }

}

