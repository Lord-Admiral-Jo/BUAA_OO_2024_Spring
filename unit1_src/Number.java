import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;

    public Number(String numString) {
        this.num = new BigInteger(numString);
    }

    @Override
    public Polynomial toPoly() {
        return new Polynomial(new PolyKey(), num);
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}



