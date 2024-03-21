import java.math.BigInteger;

public class PolyKey {
    private final BigInteger varExp;
    private final String expStr;

    public PolyKey() {
        this.varExp = BigInteger.ZERO;
        this.expStr = "0";
    }

    public PolyKey(BigInteger varExp, String expStr) {
        this.varExp = varExp;
        this.expStr = expStr;
    }

    @Override
    public int hashCode() {
        return 31 * expStr.hashCode() + varExp.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PolyKey)) {
            return false;
        }
        PolyKey polyKey = (PolyKey) obj;
        return polyKey.varExp.compareTo(this.varExp) == 0 && polyKey.expStr.equals(this.expStr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (varExp.compareTo(BigInteger.ZERO) == 0 && expStr.equals("0")) {
            return "1";
        }
        if (varExp.compareTo(BigInteger.ZERO) > 0) {
            sb.append("x");
            if (varExp.compareTo(BigInteger.ONE) > 0) {
                sb.append("^").append(varExp);
            }
        }
        if (varExp.compareTo(BigInteger.ZERO) > 0 && !expStr.equals("0")) {
            sb.append("*");
        }
        if (!expStr.equals("0")) {
            sb.append("exp(");

            sb.append("(").append(expStr).append(")");


            sb.append(")");
        }
        return sb.toString();
    }

    public boolean onlyXorExp() {
        return varExp.compareTo(BigInteger.ZERO) == 0 ^ expStr.equals("0");
    }

    public boolean onlyConst() {
        return varExp.compareTo(BigInteger.ZERO) == 0 && expStr.equals("0");
    }

    public PolyKey multiplyKey(PolyKey right) {
        BigInteger varExp = this.varExp.add(right.varExp);
        if (this.expStr.equals("0") && right.expStr.equals("0")) { // EXIT
            return new PolyKey(varExp, "0");
        }
        // *** !!!!!MORE EXITS!!!!!! *** //
        if (this.expStr.equals("0")) {
            return new PolyKey(varExp, right.expStr);
        }
        if (right.expStr.equals("0")) {
            return new PolyKey(varExp, this.expStr);
        }
        String newExpr = this.expStr + "+" + right.expStr;
        Parser parser = new Parser(new Lexer(newExpr));
        String expStr = parser.parseExpr().toPoly().toString();
        return new PolyKey(varExp, expStr);
    }

    public Polynomial deriveKey(BigInteger coef) {
        if (expStr.equals("0") && varExp.equals(BigInteger.ZERO)) {
            return new Polynomial(new PolyKey(BigInteger.ZERO, "0"), BigInteger.ZERO);
        }

        PolyKey originalX = new PolyKey(varExp, "0");
        PolyKey originalExp = new PolyKey(BigInteger.ZERO, expStr);

        if (varExp.equals(BigInteger.ZERO)) { // exp
            Polynomial poly = new Parser(new Lexer(expStr)).parseExpr().toPoly();
            poly.derPoly();
            poly.multPoly(new Polynomial(originalExp, coef));
            return poly;
        }

        if (expStr.equals("0")) { // x ^ (exp > 0)
            BigInteger newCoef = coef.multiply(varExp);
            return new Polynomial(new PolyKey(varExp.subtract(BigInteger.ONE), "0"), newCoef);
        }


        Polynomial left = new Polynomial(originalX, coef);
        Polynomial right = new Polynomial(originalExp, coef);

        left.multPoly(originalExp.deriveKey(BigInteger.ONE));
        right.multPoly(originalX.deriveKey(BigInteger.ONE));

        left.addPoly(right);
        return left;
    }
}
