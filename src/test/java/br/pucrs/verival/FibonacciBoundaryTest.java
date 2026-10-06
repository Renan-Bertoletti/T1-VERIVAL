package br.pucrs.verival;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Técnica: Análise de Valor Limite (Boundary Value Analysis — BVA).
 *
 * Fronteiras analisadas:
 *   1. Fronteira inicial zero       (n = 0 / n = 1)
 *   2. Transição primitivo→BigInteger (n = 92 / n = 93)
 *   3. Limite superior do Kata      (n = 2.000.000 / n = 2.000.001)
 *   4. Limite inferior do Kata      (n = -2.000.000 / n = -2.000.001)
 */
@DisplayName("CT-BVA — Análise de Valor Limite")
class FibonacciBoundaryTest {

    // --- Fronteira 1: zero / um ------------------------------------------

    @Test
    @DisplayName("CT-BVA-01: On-point — fib(0) = 0 (elemento neutro)")
    void testZeroOnPoint() {
        assertEquals(BigInteger.ZERO, Fibonacci.fib(BigInteger.ZERO));
    }

    @Test
    @DisplayName("CT-BVA-02: Off-point — fib(1) = 1 (primeiro elemento não-zero)")
    void testOneOffPoint() {
        assertEquals(BigInteger.ONE, Fibonacci.fib(BigInteger.ONE));
    }

    // --- Fronteira 2: último valor que cabe em long / primeiro que não cabe

    @Test
    @DisplayName("CT-BVA-03: On-point — fib(92) = 7540113804746346429 (maior fib dentro de long)")
    void testMaxLongOnPoint() {
        BigInteger expected = new BigInteger("7540113804746346429");
        assertEquals(expected, Fibonacci.fib(BigInteger.valueOf(92)));
    }

    @Test
    @DisplayName("CT-BVA-04: Off-point — fib(93) = 12200160415121876738 (estoura long, requer BigInteger)")
    void testBigIntegerOffPoint() {
        // 12200160415121876738 > Long.MAX_VALUE (9223372036854775807)
        BigInteger expected = new BigInteger("12200160415121876738");
        assertEquals(expected, Fibonacci.fib(BigInteger.valueOf(93)));
    }

    // --- Fronteira 3: limite superior do Kata ----------------------------

    @Test
    @DisplayName("CT-BVA-05: On-point — fib(2.000.000) executa sem exceção (~417.974 dígitos)")
    void testUpperLimitOnPoint() {
        BigInteger result = Fibonacci.fib(BigInteger.valueOf(2_000_000));
        assertNotNull(result);
        assertTrue(result.signum() > 0);
    }

    @Test
    @DisplayName("CT-BVA-06: Out-point — fib(2.000.001) lança IllegalArgumentException")
    void testUpperLimitOutPoint() {
        assertThrows(IllegalArgumentException.class,
                () -> Fibonacci.fib(BigInteger.valueOf(2_000_001)));
    }

    // --- Fronteira 4: limite inferior do Kata ----------------------------

    @Test
    @DisplayName("CT-BVA-07: On-point — fib(-2.000.000) executa sem exceção")
    void testLowerLimitOnPoint() {
        BigInteger result = Fibonacci.fib(BigInteger.valueOf(-2_000_000));
        assertNotNull(result);
    }

    @Test
    @DisplayName("CT-BVA-08: Out-point — fib(-2.000.001) lança IllegalArgumentException")
    void testLowerLimitOutPoint() {
        assertThrows(IllegalArgumentException.class,
                () -> Fibonacci.fib(BigInteger.valueOf(-2_000_001)));
    }
}
