import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private final ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public Polynomial toPoly() {
        Polynomial poly = new Polynomial(new PolyKey(), BigInteger.ONE); // 1*...*...
        for (Factor factor : factors) {
            poly.multPoly(factor.toPoly());
        }
        return poly;
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}
