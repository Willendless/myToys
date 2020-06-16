/**
 * @Author: Willendless
 * @Date: 2020-06-15
 * @Description: Expr class
 * @LastEditTime: 2020-06-15
 * @FilePath: \code\Expr.java
 */
package code;

public abstract class Expr {

    abstract <R> R accept(Visitor<R> visitor);

    interface Visitor<R> {
        R visitLiteral(Literal literal);
        R visitUnary(Unary unary);
        R visitBinary(Binary binary);
        R visitGrouping(Grouping grouping);
    }

    public static class Literal extends Expr {
        String word;

        public Literal(String word) {
            this.word = word;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class Unary extends Expr {
        Token operator;
        Expr expression;

        public Unary(Token operator, Expr expression) {
            this.operator = operator;
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnary(this);
        }
    }

    public static class Binary extends Expr {
        Expr left;
        Token operator;
        Expr right;

        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinary(this);
        }
    }

    public static class Grouping extends Expr {
        Expr expression;

        public Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGrouping(this);
        }
    }
    
}