/**
 * @Author: Willendless
 * @Date: 2020-06-18
 * @Description: Controller class
 * @LastEditTime: 2020-06-18
 * @FilePath: \code\Controller.java
 */
package code;

import java.io.IOException;

public class Controller {

    View view;
    Tokenizer tokenizer;
    Parser parser;
    Interpreter interpreter;
    Matcher matcher;

    public void init() {
        tokenizer = new Tokenizer();
        parser = new Parser();
        interpreter = new Interpreter();
        matcher = new Matcher();
        view = new View(this);
        view.show();
    }

    public void process() {
        parser.setTokens(tokenizer.tokenizer());
        SearchTarget target = interpreter.interpret(parser.parse());
        matcher.setSearchTarget(target);
        try {
            view.update(matcher.match());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Controller().init();
    }

}
