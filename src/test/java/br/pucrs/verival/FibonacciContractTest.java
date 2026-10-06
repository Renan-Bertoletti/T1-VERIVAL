package br.pucrs.verival;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Técnica: Teste baseado em Contratos (Design by Contract — DbC).
 *
 * Verifica que o método fib(n) respeita seu contrato formal:
 *
 *   Pré-condição 1: n != null
 *   Pré-condição 2: |n| <= 2.000.000
 *   Pós-condição:   resultado != null para qualquer entrada válida
 */
@DisplayName("CT-DBC — Design by Contract")
class FibonacciContractTest {

    // --- Violações de pré-condição ---------------------------------------

    @Test
    @DisplayName("CT-DBC-01: Pré-condição 1 — entrada nula interrompe com IllegalArgumentException")
    void testPreconditionNull() {
        assertThrows(IllegalArgumentException.class, () -> Fibonacci.fib(null));
    }

    @ParameterizedTest(name = "fib({0}) viola pré-condição 2")
    @ValueSource(longs = {2_000_001L, 3_000_000L, Long.MAX_VALUE, -2_000_001L, -3_000_000L})
    @DisplayName("CT-DBC-02: Pré-condição 2 — |n| > 2.000.000 interrompe com IllegalArgumentException")
    void testPreconditionOutOfRange(long n) {
        assertThrows(IllegalArgumentException.class,
                () -> Fibonacci.fib(BigInteger.valueOf(n)));
    }

    // --- Satisfação de pós-condição --------------------------------------

    @ParameterizedTest(name = "fib({0}) != null")
    @ValueSource(longs = {0L, 1L, 10L, 92L, 93L, 1_000L, 2_000_000L, -1L, -6L, -2_000_000L})
    @DisplayName("CT-DBC-03: Pós-condição — resultado nunca é nulo para entrada válida")
    void testPostconditionNotNull(long n) {
        assertNotNull(Fibonacci.fib(BigInteger.valueOf(n)));
    }
}
