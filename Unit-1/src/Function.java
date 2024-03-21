import java.util.ArrayList;

public class Function implements Factor {
    private final String name;
    private final ArrayList<String> arguments;

    public Function(String name, ArrayList<String> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public Polynomial toPoly() {
        FuncManager funcManager = FuncManager.getInstance();
        Expr expr = funcManager.callFunc(name, arguments);
        return expr.toPoly();
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}
