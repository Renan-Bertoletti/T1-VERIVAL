package br.pucrs.verival;

import java.math.BigInteger;

/**
 * SUT — The Millionth Fibonacci Kata (Codewars, 3 kyu).
 *
 * Calcula fib(n) para n ∈ [-2.000.000, 2.000.000] usando o algoritmo
 * Fast Doubling (complexidade O(log|n|)), suportando índices negativos
 * pela relação: fib(-n) = (-1)^(n+1) * fib(n).
 */
public class Fibonacci {

    private static final BigInteger MAX_LIMIT = BigInteger.valueOf(2_000_000);

    public static BigInteger fib(BigInteger n) {
        if (n == null) {
            throw new IllegalArgumentException("O parâmetro 'n' não pode ser nulo.");
        }
        if (n.abs().compareTo(MAX_LIMIT) > 0) {
            throw new IllegalArgumentException(
                    "O parâmetro 'n' excede o limite máximo permitido (|n| <= 2.000.000).");
        }
        if (n.signum() == 0) {
            return BigInteger.ZERO;
        }

        boolean isNegative = n.signum() < 0;
        BigInteger absN = n.abs();
        BigInteger result = fastDoubling(absN)[0];

        // Negafibonacci: fib(-n) = (-1)^(n+1) * fib(n)
        // Sinal negativo somente quando n é par
        if (isNegative && absN.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return result.negate();
        }
        return result;
    }

    /**
     * Fast Doubling — retorna [F(k), F(k+1)].
     *
     * Identidades utilizadas:
     *   F(2m)   = F(m) * [2*F(m+1) - F(m)]
     *   F(2m+1) = F(m)^2 + F(m+1)^2
     */
    private static BigInteger[] fastDoubling(BigInteger k) {
        if (k.equals(BigInteger.ZERO)) {
            return new BigInteger[]{BigInteger.ZERO, BigInteger.ONE};
        }
        BigInteger[] half = fastDoubling(k.shiftRight(1));
        BigInteger a = half[0]; // F(m)
        BigInteger b = half[1]; // F(m+1)

        BigInteger c = a.multiply(b.shiftLeft(1).subtract(a)); // F(2m)
        BigInteger d = a.multiply(a).add(b.multiply(b));       // F(2m+1)

        if (k.testBit(0)) {
            return new BigInteger[]{d, c.add(d)};
        } else {
            return new BigInteger[]{c, d};
        }
    }
}
