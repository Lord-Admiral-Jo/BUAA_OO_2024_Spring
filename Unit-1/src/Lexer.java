public class Lexer {
    private final String input;
    private int pos;
    private Token curToken;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.next();
    }

    public boolean notEnd() {
        return pos < input.length();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    public Token peek() {
        return this.curToken;
    }

    public void next() {
        if (pos >= input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (c == '+') {
            ++pos;
            curToken = new Token(Token.Type.ADD, String.valueOf(c));
        } else if (c == '-') {
            ++pos;
            curToken = new Token(Token.Type.SUB, String.valueOf(c));
        } else if (c == '*') {
            ++pos;
            curToken = new Token(Token.Type.MUL, String.valueOf(c));
        } else if (c == '(') {
            ++pos;
            curToken = new Token(Token.Type.LPAREN, String.valueOf(c));
        } else if (c == ')') {
            ++pos;
            curToken = new Token(Token.Type.RPAREN, String.valueOf(c));
        } else if (c == '^') {
            ++pos;
            curToken = new Token(Token.Type.POW, String.valueOf(c));
        } else if (Character.isDigit(c)) { // number factor
            curToken = new Token(Token.Type.NUM, getNumber());
        } else if (c == 'x') { // variable
            ++pos;
            curToken = new Token(Token.Type.VAR, String.valueOf(c));
        } else if (c == 'f' || c == 'g' || c == 'h') {
            ++pos;
            curToken = new Token(Token.Type.FUNC, String.valueOf(c));
        } else if (c == ',') {
            ++pos;
            curToken = new Token(Token.Type.COMMA, String.valueOf(c));
        } else if (c == 'e') {
            pos += 3;
            curToken = new Token(Token.Type.EXP, "exp");
        } else if (c == 'd') {
            pos += 2;
            curToken = new Token(Token.Type.DERIVE, "dx");
        }
    }
}
