public class OutputHandler {
    private String input;

    public OutputHandler(String input) {
        this.input = input;
    }

    private void optimizeResult() {
        Lexer lexer = new Lexer(input);
        StringBuilder sb = new StringBuilder();

        while (lexer.notEnd()) {
            Token.Type type = lexer.peek().getType();
            if (!type.equals(Token.Type.EXP) && !type.equals(Token.Type.RPAREN)) {
                sb.append(lexer.peek().getContent());
                lexer.next();
                continue;
            }
            sb.append("exp");
            lexer.next();
            lexer.next();// (

            Polynomial poly = new Parser(lexer).parseFactor().toPoly();
            String[] strs = poly.optimize().split("_");

            sb.append(strs[0]);
            sb.append(new OutputHandler(strs[1]).getResult());
            sb.append(strs[2]);

            lexer.next(); // )
        }
        if (!lexer.peek().getType().equals(Token.Type.RPAREN)) {
            sb.append(lexer.peek().getContent());
        }
        input = sb.toString();
    }

    public String getResult() {
        optimizeResult();
        return this.input;
    }
}
