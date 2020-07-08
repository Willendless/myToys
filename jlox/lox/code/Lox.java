/**
 * @Author: Willendless
 * @Date: 2020-06-28
 * @Description: Do not edit
 * @LastEditTime: 2020-06-28
 * @FilePath: \lox\code\Lox.java
 */

package code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lox {

    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script file]");
            System.exit(64);
        } else if (args.length == 1) {
            runfile(args[0]);
        } else {
            runprompt();
        }
    }

    private static void runfile(String path) throws IOException {
        byte[] contents = Files.readAllBytes(Paths.get(path));
        run(new String(contents, Charset.defaultCharset()));
    }

    private static void runprompt() throws IOException {
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader bReader = new BufferedReader(reader);
        while (true) {
            System.out.print("> ");
            run(bReader.readLine());
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token t : tokens) {
            System.out.println(t);
        }

        if (hadError) {
            System.exit(65);
        }
    }

    public static void error(int lineNumber, String message) {
        report(lineNumber, "", message);
        hadError = true;
    }

    private static void report(int lineNumber, String where, String message) {
        System.err.println(
            "[line " + lineNumber + "] Error: " + where + " " + message);
    }

}
