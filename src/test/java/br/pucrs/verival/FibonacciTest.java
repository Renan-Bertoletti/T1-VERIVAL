package br.pucrs.verival;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Técnica: Particionamento por Classes de Equivalência.
 *
 * O domínio de entrada BigInteger foi dividido em partições mutuamente
 * exclusivas e exaustivas: zero (P1), positivos pequenos (P2), positivos
 * grandes (P3), negativos pares (P4), negativos ímpares (P5) e inválidas
 * (nulo P6, fora do limite P7).
 */
@DisplayName("CT-PART — Particionamento por Classes de Equivalência")
class FibonacciTest {

    // --- Partições válidas -----------------------------------------------

    @Test
    @DisplayName("CT-PART-01: P1 — Caso base zero")
    void testBaseZero() {
        assertEquals(BigInteger.ZERO, Fibonacci.fib(BigInteger.ZERO));
    }

    @ParameterizedTest(name = "fib({0}) = {1}")
    @CsvSource({
        "1,  1",
        "2,  1",
        "3,  2",
        "4,  3",
        "5,  5",
        "6,  8",
        "7,  13",
        "8,  21",
        "9,  34",
        "10, 55"
    })
    @DisplayName("CT-PART-02: P2 — Pequenos positivos (faixa primitiva, n ≤ 92)")
    void testSmallPositive(long input, String expected) {
        assertEquals(new BigInteger(expected), Fibonacci.fib(BigInteger.valueOf(input)));
    }

    @Test
    @DisplayName("CT-PART-03: P3 — Grande positivo exige BigInteger (fib(1000) tem 209 dígitos)")
    void testLargePositive() {
        BigInteger result = Fibonacci.fib(BigInteger.valueOf(1000));
        assertNotNull(result);
        assertTrue(result.signum() > 0);
        assertEquals(209, result.toString().length());
    }

    @Test
    @DisplayName("CT-PART-04: P4 — Negativo par resulta em valor negativo: fib(-6) = -8")
    void testNegativeEven() {
        assertEquals(BigInteger.valueOf(-8), Fibonacci.fib(BigInteger.valueOf(-6)));
    }

    @Test
    @DisplayName("CT-PART-05: P5 — Negativo ímpar resulta em valor positivo: fib(-7) = 13")
    void testNegativeOdd() {
        assertEquals(BigInteger.valueOf(13), Fibonacci.fib(BigInteger.valueOf(-7)));
    }

    // --- Partições inválidas ---------------------------------------------

    @Test
    @DisplayName("CT-PART-06: P6 — Entrada nula lança IllegalArgumentException")
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> Fibonacci.fib(null));
    }
}
