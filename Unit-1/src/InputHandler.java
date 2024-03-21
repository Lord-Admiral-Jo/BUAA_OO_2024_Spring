import java.util.Scanner;

public class InputHandler {
    private String input;

    public InputHandler(Scanner scanner) {
        int n = scanner.nextInt();
        scanner.nextLine();
        while (n-- > 0) {
            String def = scanner.nextLine();

            def = removeBlanks(def);
            def = removeExtraAddnSub(def);

            FuncManager funcManager = FuncManager.getInstance();
            funcManager.addFuncDef(def);
        }
        this.input = scanner.nextLine();
    }

    private String removeExtraAddnSub(String string) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '+' || string.charAt(i) == '-') {
                int j = i;
                boolean isPositive = true;
                while (string.charAt(j) == '+' || string.charAt(j) == '-') {
                    if (string.charAt(j) == '-') {
                        isPositive = !isPositive;
                    }
                    j++;
                }
                sb.append(isPositive ? "+" : "-");
                i = j - 1;
            } else {
                sb.append(string.charAt(i));
            }
        }
        return sb.toString();
    }

    private String removeBlanks(String input) {
        return input.replaceAll("\\t", "").replaceAll(" ", "");
    }

    public String getExpression() {
        this.input = removeBlanks(input);
        this.input = removeExtraAddnSub(input);
        return this.input;
    }
}
