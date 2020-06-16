/**
 * @Author: Willendless
 * @Date: 2020-06-15
 * @Description: Helper class for Ast print
 * @LastEditTime: 2020-06-16
 * @FilePath: \code\AstPrinter.java
 */
package code;

import code.Expr;
import code.Expr.Binary;
import code.Expr.Grouping;
import code.Expr.Literal;
import code.Expr.Unary;
import code.Token.TokenType;

public class AstPrinter implements Expr.Visitor<String> {

    public void print(Expr expr) {
        System.out.println(expr.accept(this));
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    @Override
    public String visitLiteral(Literal literal) {
        return literal.word;
    }

    @Override
    public String visitUnary(Unary unary) {
        return parenthesize(unary.operator.getValue(), unary.literal);
    }

    @Override
    public String visitBinary(Binary binary) {
        return parenthesize(binary.operator.getValue(), binary.left, binary.right);
    }

    @Override
    public String visitGrouping(Grouping grouping) {
        return parenthesize("grouping", grouping.expression);
    }

    public static void main(String[] args) {
        Expr test = new Binary(
            new Unary(
                new Token(TokenType.NOT, "!"),
                new Literal("asdf")),
            new Token(TokenType.OR, "||"),
            new Grouping(
                new Literal("aaaa")
            ));
        new AstPrinter().print(test);
    }
    
}