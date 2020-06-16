package code;

import code.Expr.Binary;
import code.Expr.Grouping;
import code.Expr.Literal;
import code.Expr.Unary;
import code.SearchTarget.SearchTargetItem.SearchType;

import static code.SearchTarget.SearchTargetItem;

public class Interpreter implements Expr.Visitor<SearchTarget> {

    public SearchTarget interpret(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public SearchTarget visitLiteral(Literal literal) {
        SearchTarget re = new SearchTarget();
        re.add(new SearchTargetItem(SearchType.EXIST, literal.word));
        return re;
    }

    @Override
    public SearchTarget visitUnary(Unary unary) {
        SearchTarget re = new SearchTarget();
        re.add(new SearchTargetItem(SearchType.NONEXIST, unary.literal.word));
        return re;
    }

    @Override
    public SearchTarget visitBinary(Binary binary) {
        SearchTarget re;
        switch (binary.operator.getType()) {
        case AND: {
            re = new SearchTarget(binary.left.accept(this));
            re.and(binary.right.accept(this));
            break;
        }
        case OR: {
            re = new SearchTarget(binary.left.accept(this));
            re.or(binary.right.accept(this));
            break;
        }
        default:
            throw new LineMatchException("Can not to reach here");
        }
        return re;
    }

    @Override
    public SearchTarget visitGrouping(Grouping grouping) {
        return grouping.expression.accept(this);
    }
    
}
