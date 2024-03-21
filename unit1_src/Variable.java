import java.math.BigInteger;

public class Variable implements Factor {
    private final BigInteger exp;

    public Variable(String expStr) {
        this.exp = new BigInteger(expStr);
    }

    public Polynomial toPoly() {
        return new Polynomial(new PolyKey(exp, "0"), BigInteger.ONE);
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}
