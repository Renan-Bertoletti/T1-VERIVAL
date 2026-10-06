package br.pucrs.verival;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Técnica: Teste Baseado em Propriedades (Property-Based Testing — PBT).
 *
 * Usa o framework jqwik para validar propriedades matemáticas universais
 * da sequência de Fibonacci sobre domínios gerados automaticamente
 * (~1.000 execuções aleatórias por propriedade).
 *
 * Propriedades verificadas:
 *   1. Recorrência aditiva:  fib(n) = fib(n-1) + fib(n-2)  ∀ n ≥ 2
 *   2. Negafibonacci:        fib(-n) = (-1)^(n+1) * fib(n)  ∀ n ≥ 1
 *   3. Coprimaridade:        gcd(fib(n), fib(n+1)) = 1       ∀ n ≥ 1
 */
class FibonacciPropertyTest {

    @Property
    @Label("CT-PBT-01: Recorrência — fib(n) = fib(n-1) + fib(n-2) para todo n ∈ [2, 5000]")
    void testRecurrenceProperty(@ForAll @IntRange(min = 2, max = 5000) int n) {
        BigInteger fibN  = Fibonacci.fib(BigInteger.valueOf(n));
        BigInteger fibN1 = Fibonacci.fib(BigInteger.valueOf(n - 1));
        BigInteger fibN2 = Fibonacci.fib(BigInteger.valueOf(n - 2));

        assertEquals(fibN1.add(fibN2), fibN);
    }

    @Property
    @Label("CT-PBT-02: Negafibonacci — fib(-n) = (-1)^(n+1) * fib(n) para todo n ∈ [1, 5000]")
    void testNegafibonacciProperty(@ForAll @IntRange(min = 1, max = 5000) int n) {
        BigInteger fibPos = Fibonacci.fib(BigInteger.valueOf(n));
        BigInteger fibNeg = Fibonacci.fib(BigInteger.valueOf(-n));

        // n par → (-1)^(n+1) = -1 → resultado negativo
        // n ímpar → (-1)^(n+1) = +1 → resultado positivo
        if (n % 2 == 0) {
            assertEquals(fibPos.negate(), fibNeg);
        } else {
            assertEquals(fibPos, fibNeg);
        }
    }

    @Property
    @Label("CT-PBT-03: Coprimaridade — gcd(fib(n), fib(n+1)) = 1 para todo n ∈ [1, 2000]")
    void testCoprimalityProperty(@ForAll @IntRange(min = 1, max = 2000) int n) {
        BigInteger fibN  = Fibonacci.fib(BigInteger.valueOf(n));
        BigInteger fibN1 = Fibonacci.fib(BigInteger.valueOf(n + 1));

        assertEquals(BigInteger.ONE, fibN.gcd(fibN1));
    }
}
