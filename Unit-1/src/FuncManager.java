import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class FuncManager {
    // *** Attributes *** //
    private static final class FuncInfo {
        private final ArrayList<String> params;
        private final String expression;

        private FuncInfo(ArrayList<String> params, String expression) {
            this.params = params;
            this.expression = expression;
        }
    }

    private FuncInfo curFunc;
    private final Stack<String> callTraces = new Stack<>();
    private final HashMap<String, FuncInfo> funcMap = new HashMap<>();

    // ********** SingleObject ********** //
    private static final FuncManager funcManager = new FuncManager();

    private FuncManager() {
    }

    public static FuncManager getInstance() {
        return funcManager;
    }

    // *** General Methods *** //
    public void addFuncDef(String def) {
        String name = String.valueOf(def.charAt(0));

        ArrayList<String> params = new ArrayList<>();
        int pos = 1;

        while (def.charAt(pos) != '=') {
            char c = def.charAt(pos);
            if (c == 'x' || c == 'y' || c == 'z') {
                params.add(String.valueOf(c));
            }
            pos++;
        }
        String expression = def.substring(pos + 1);
        FuncInfo funcInfo = new FuncInfo(params, expression);
        funcMap.put(name, funcInfo);
    }

    public Expr callFunc(String funcName, ArrayList<String> arguments) {
        pushStack(funcName); // f/g/h

        Parser newParser = new Parser(new Lexer(loadArguments(arguments)));
        Expr expr = newParser.parseExpr();

        popStack();

        return expr;
    }

    private void pushStack(String funcName) {
        callTraces.push(funcName);
        curFunc = funcMap.get(callTraces.peek());
    }

    private void popStack() {
        callTraces.pop();
        if (callTraces.isEmpty()) {
            curFunc = null;
        } else {
            curFunc = funcMap.get(callTraces.peek());
        }
    }

    private String loadArguments(ArrayList<String> arguments) {
        String string = curFunc.expression.replaceAll("exp", "&");
        string = string.replaceAll("dx", "#");

        for (int i = 0; i < arguments.size(); i++) {
            String param = curFunc.params.get(i);

            String arg = arguments.get(i).replaceAll("x", "@");

            string = string.replaceAll(param, "(" + arg + ")");
        }

        return string.replaceAll("&", "exp").replaceAll("#", "dx").replaceAll("@", "x");
    }
}



