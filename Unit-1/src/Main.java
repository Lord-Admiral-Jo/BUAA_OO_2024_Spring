import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Input Handling
        InputHandler inputHandler = new InputHandler(new Scanner(System.in));
        String input = inputHandler.getExpression();

        // Processing
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        String rawOutput = expr.toPoly().toString();

        // Output Handling
        String finalOutput = new OutputHandler(rawOutput).getResult();
        System.out.println(finalOutput);
    }
}
