import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    private int getExp() { //   if (lexer.peek().getType().equals(Token.Type.POW))
        lexer.next();   // ^
        if (lexer.peek().getType().equals(Token.Type.ADD)) {
            lexer.next();      // ABSOLUTELY POSITIVE
        }
        int result = Integer.parseInt(lexer.peek().getContent());
        lexer.next();   // exp
        return result;
    }

    private ArrayList<String> getFuncArguments() {
        ArrayList<String> arguments = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        lexer.next();   // f/g/h
        lexer.next();// (
        int layer = 1;
        while (true) {
            if (lexer.peek().getType().equals(Token.Type.LPAREN)) {
                layer++;
            } else if (lexer.peek().getType().equals(Token.Type.RPAREN)) {
                layer--;
                if (layer == 0) {
                    break;
                }
            }
            if (layer == 1 && lexer.peek().getType().equals(Token.Type.COMMA)) {
                arguments.add(sb.toString());
                sb.delete(0, sb.length());
                lexer.next(); // ,
            } else {
                sb.append(lexer.peek().getContent());
                lexer.next();
            }
        }
        lexer.next();    // )
        arguments.add(sb.toString());

        return arguments;
    }

    public Factor parseFactor() {
        Token.Type type = lexer.peek().getType();
        String content = lexer.peek().getContent();
        if (type.equals(Token.Type.LPAREN)) {
            // *************** EXPRESSION *************** //
            lexer.next();    // (
            Expr expr = parseExpr();
            lexer.next();   // )
            if (lexer.peek().getType().equals(Token.Type.POW)) {
                expr.setExp(String.valueOf(getExp()));
            }
            return expr;
        } else if (type.equals(Token.Type.NUM)) {
            // *************** NUMBER *************** //
            lexer.next();
            return new Number(content);
        } else if (type.equals(Token.Type.VAR)) {
            // *************** VARIABLE *************** //
            lexer.next(); // "x"
            int exp = 1;
            if (lexer.peek().getType().equals(Token.Type.POW)) {
                exp = getExp();
            }
            return new Variable(String.valueOf(exp));
        } else if (type.equals(Token.Type.FUNC)) {
            // *************** FUNCTION *************** //
            return new Function(content, getFuncArguments());
        } else if (type.equals(Token.Type.EXP)) {
            // *************** EXPONENTIAL *************** //
            lexer.next();   // "exp"
            lexer.next();   // (
            Factor factor = parseFactor();
            lexer.next(); // )
            int exp = 1;
            if (lexer.peek().getType().equals(Token.Type.POW)) {
                exp = getExp();
            }
            return new Exponential(factor, String.valueOf(exp));
        } else if (type.equals(Token.Type.DERIVE)) {
            // *************** DERIVIATION *************** //
            lexer.next(); // "dx"
            lexer.next(); // (
            Expr expr = parseExpr();
            lexer.next(); // )
            return new Derivation(expr);
        } else if (type.equals(Token.Type.ADD) || type.equals(Token.Type.SUB)) {
            // *************** PLUS or MINUS *************** //
            lexer.next();
            String num = lexer.peek().getContent();
            boolean isNegative = type.equals(Token.Type.SUB); // -
            if (isNegative) {
                num = "-" + num;
            }
            lexer.next();
            return new Number(num);
        } else {
            throw new AssertionError("Unrecognized Token!!!");
        }
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parseFactor());

        while (lexer.peek().getType().equals(Token.Type.MUL)) { // *
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();

        if (lexer.peek().getType().equals(Token.Type.SUB) ||
                lexer.peek().getType().equals(Token.Type.ADD)) {
            expr.addOp(lexer.peek());
            lexer.next();
            expr.addTerm(parseTerm());
        } else {
            expr.addOp(new Token(Token.Type.ADD, "+"));
            expr.addTerm(parseTerm());
        }

        while (lexer.peek().getType().equals(Token.Type.ADD) ||
                lexer.peek().getType().equals(Token.Type.SUB)) {
            expr.addOp(lexer.peek());
            lexer.next();
            expr.addTerm(parseTerm());
        }
        return expr;
    }
}

