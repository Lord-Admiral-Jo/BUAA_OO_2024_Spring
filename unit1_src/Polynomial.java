import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Polynomial {
    private HashMap<PolyKey, BigInteger> coefMap = new HashMap<>();

    public Polynomial() {
    }

    public Polynomial(PolyKey polyKey, BigInteger coef) {
        this.addItem(polyKey, coef);
    }

    public void addItem(PolyKey polyKey, BigInteger coef) {
        this.coefMap.put(polyKey, coef);
    }

    public void addPoly(Polynomial right) {
        for (Map.Entry<PolyKey, BigInteger> entry : right.coefMap.entrySet()) {
            PolyKey keyR = entry.getKey();
            BigInteger coefR = entry.getValue();
            if (this.coefMap.containsKey(keyR)) {
                addItem(keyR, coefR.add(this.coefMap.get(keyR)));
            } else {
                addItem(keyR, coefR);
            }
        }
    }

    public void subPoly(Polynomial right) {
        for (Map.Entry<PolyKey, BigInteger> entry : right.coefMap.entrySet()) {
            PolyKey keyR = entry.getKey();
            BigInteger coefR = entry.getValue();
            if (this.coefMap.containsKey(keyR)) {
                addItem(keyR, this.coefMap.get(keyR).subtract(coefR));
            } else {
                addItem(keyR, coefR.negate());
            }
        }
    }

    public void multPoly(Polynomial right) {
        Polynomial res = new Polynomial();
        for (Map.Entry<PolyKey, BigInteger> entryL : this.coefMap.entrySet()) {
            for (Map.Entry<PolyKey, BigInteger> entryR : right.coefMap.entrySet()) {
                PolyKey polyKey = entryL.getKey().multiplyKey(entryR.getKey());
                BigInteger coef = entryL.getValue().multiply(entryR.getValue());
                res.addPoly(new Polynomial(polyKey, coef));
            }
        }
        this.coefMap = res.coefMap;
    }

    public void derPoly() {
        Polynomial res = new Polynomial();
        for (Map.Entry<PolyKey, BigInteger> entry : this.coefMap.entrySet()) {
            PolyKey polyKey = entry.getKey();
            BigInteger coef = entry.getValue();

            res.addPoly(polyKey.deriveKey(coef));
        }
        this.coefMap = res.coefMap;
    }

    public void powPoly(BigInteger exp) {
        if (exp.compareTo(BigInteger.ZERO) == 0) {
            this.coefMap.clear();
            this.addItem(new PolyKey(), BigInteger.ONE);
            return;
        }
        Polynomial newPoly = new Polynomial();
        newPoly.coefMap.putAll(this.coefMap);
        for (BigInteger i = BigInteger.ONE; i.compareTo(exp) < 0; i = i.add(BigInteger.ONE)) {
            this.multPoly(newPoly);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean hasValue = false;
        for (Map.Entry<PolyKey, BigInteger> entry : this.coefMap.entrySet()) {
            final PolyKey polyKey = entry.getKey();
            final BigInteger coef = entry.getValue();
            final String polyKeyStr = polyKey.toString();

            if (coef.compareTo(BigInteger.ZERO) == 0) {
                continue;  // coef == 0
            }

            if (hasValue && coef.compareTo(BigInteger.ZERO) > 0) {
                sb.append("+"); // coef > 0
            }
            hasValue = true;

            if (coef.compareTo(BigInteger.ONE) == 0) { // coef == 1
                sb.append(polyKeyStr);
            } else if (coef.compareTo(BigInteger.ONE.negate()) == 0) {
                sb.append("-").append(polyKeyStr);
            } else {
                sb.append(coef);
                if (!polyKeyStr.equals("1")) {
                    sb.append("*").append(polyKeyStr);
                }
            }
        }
        if (!hasValue) {
            return "0";
        }
        return sb.toString();
    }

    // Saved For Optimizing
    public String optimize() {
        if (this.coefMap.size() == 1) {
            return optimizeParen();
        }
        return optimizeCoef();
    }

    private String optimizeParen() {
        if (this.coefMap.size() != 1) {
            throw new AssertionError("Wrong Optimize-1!!!");
        }

        PolyKey polyKey = null;
        BigInteger coef = null;
        for (Map.Entry<PolyKey, BigInteger> entry : this.coefMap.entrySet()) {
            if (polyKey != null || entry.getValue().compareTo(BigInteger.ZERO) == 0) {
                throw new AssertionError("coef != 0");
            }
            polyKey = entry.getKey();
            coef = entry.getValue();
        }
        if (polyKey.onlyConst()) {
            // 114514, -1
            return String.format("(_%s_)", coef);
        }

        if (coef.compareTo(BigInteger.ONE) == 0 && polyKey.onlyXorExp()) {
            // x^2, exp()
            return String.format("(_%s_)", polyKey);
        }
        if (coef.compareTo(BigInteger.ZERO) > 0 && polyKey.onlyXorExp()) {
            // 3*x, 2*exp()
            return String.format("(_%s_)^%d", polyKey, coef);
        }
        return "((_" + this + "_))";
    }

    private Polynomial divideNumRes(BigInteger val) {
        Polynomial res = new Polynomial();
        for (Map.Entry<PolyKey, BigInteger> entry : this.coefMap.entrySet()) {
            res.addPoly(new Polynomial(entry.getKey(), entry.getValue().divide(val)));
        }
        return res;
    }

    private String optimizeCoef() {
        if (this.coefMap.size() < 2) {
            throw new AssertionError("Wrong Optimize-2!!!");
        }
        BigInteger gcd = null;

        // Get "GCD"
        for (Map.Entry<PolyKey, BigInteger> entry : this.coefMap.entrySet()) {
            if (gcd == null) { // init gcd
                gcd = entry.getValue();
            } else { // update gcd
                gcd = gcd.gcd(entry.getValue());
            }

            if (gcd.compareTo(BigInteger.ONE) == 0) {
                return "((_" + this + "_))";
            }
        }
        // Search Best "Q"
        Polynomial minLenAns = this; // init : q == gcd!!!(no ^)
        int minLen = this.toString().length();
        BigInteger q = BigInteger.ONE;
        BigInteger targetQ = gcd;

        while (q.compareTo(BigInteger.TEN.min(gcd.add(BigInteger.ONE))) < 0) {
            // 1 <= q <= min(9, gcd)
            if (gcd.mod(q).compareTo(BigInteger.ZERO) != 0) {
                q = q.add(BigInteger.ONE);
                continue;
            }
            // check
            Polynomial res = this.divideNumRes(gcd.divide(q));
            int len = res.toString().length();
            if (q.compareTo(gcd) != 0) { // q != gcd
                len += 1 + gcd.divide(q).toString().length(); // ^ gcd / q
            }

            if (len < minLen) {
                minLenAns = res;
                minLen = len;
                targetQ = q;
            }
            q = q.add(BigInteger.ONE);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("((_").append(minLenAns).append("_))");

        if (targetQ.compareTo(gcd) != 0) {
            sb.append("^").append(gcd.divide(targetQ));
        }
        return sb.toString();
    }
}
