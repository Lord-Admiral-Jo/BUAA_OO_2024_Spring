public class Derivation implements Factor {
    private final Expr base;

    public Derivation(Expr base) {
        this.base = base;
    }

    @Override
    public Polynomial toPoly() {
        Polynomial polyBase = base.toPoly();
        polyBase.derPoly();
        return polyBase;
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}
