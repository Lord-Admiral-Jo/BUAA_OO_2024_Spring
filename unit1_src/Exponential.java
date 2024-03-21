import java.math.BigInteger;

public class Exponential implements Factor {
    private final Factor factor;
    private final BigInteger exp;

    public Exponential(Factor factor, String expStr) {
        this.factor = factor;
        this.exp = new BigInteger(expStr);
    }

    @Override
    public Polynomial toPoly() {
        Polynomial poly = new Number(String.valueOf(exp)).toPoly();
        poly.multPoly(factor.toPoly());

        PolyKey polyKey = new PolyKey(BigInteger.ZERO, poly.toString());
        return new Polynomial(polyKey, BigInteger.ONE);
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}
