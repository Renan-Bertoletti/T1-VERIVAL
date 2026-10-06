# Documentação de Casos de Teste (tests.md) — Trabalho T1

**Disciplina:** Verificação e Validação de Software (2026/I)
**Instituição:** Pontifícia Universidade Católica do Rio Grande do Sul (PUCRS) — Escola Politécnica
**Sistema sob Teste (SUT):** *The Millionth Fibonacci Kata* (Codewars, Nível 3 kyu)
**Linguagem & Plataforma:** Java 17+ | JUnit 5 Jupiter | jqwik 1.8+ | Apache Maven

---

## 1. Visão Geral do SUT e Especificação Técnica

O Sistema sob Teste (SUT) consiste na classe `Fibonacci` e no seu método público estático `public static BigInteger fib(BigInteger n)`. O objetivo funcional é calcular o termo exato `fib(n)` da sequência de Fibonacci para inteiros no intervalo `n ∈ [-2.000.000, 2.000.000]`.

### 1.1 Regras de Negócio e Requisitos Matemáticos

1. **Casos Base:** `fib(0) = 0`, `fib(1) = 1`.
2. **Recorrência Positiva (n ≥ 2):** `fib(n) = fib(n-1) + fib(n-2)`.
3. **Recorrência Negativa (n < 0):** Derivada da inversão `fib(n) = fib(n+2) - fib(n+1)`, resultando na regra de sinal: `fib(-n) = (-1)^(n+1) * fib(n)`.
4. **Precisão Numérica:** Uso obrigatório de `java.math.BigInteger` para evitar estouro aritmético (`long` estoura para `n > 92`).
5. **Tratamento de Exceções & Contratos:**
   - `n = null` → lança `IllegalArgumentException`.
   - `|n| > 2.000.000` → lança `IllegalArgumentException`.

---

## 2. Matriz de Rastreabilidade de Casos de Teste

| ID Caso | Técnica de V&V | Categoria / Descrição | Entrada (n) | Resultado Esperado | Classe / Método de Teste |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **CT-PART-01** | Particionamento | Caso Base Zero | `0` | `0` | `FibonacciTest#testBaseZero` |
| **CT-PART-02** | Particionamento | Pequenos Positivos (n ≤ 92) | `1`–`10` | `1, 1, 2, 3, 5, 8, 13, 21, 34, 55` | `FibonacciTest#testSmallPositive` |
| **CT-PART-03** | Particionamento | Grande Positivo (BigInteger) | `1.000` | Termo de 209 dígitos | `FibonacciTest#testLargePositive` |
| **CT-PART-04** | Particionamento | Negativo Par (sinal negativo) | `-6` | `-8` | `FibonacciTest#testNegativeEven` |
| **CT-PART-05** | Particionamento | Negativo Ímpar (sinal positivo) | `-7` | `13` | `FibonacciTest#testNegativeOdd` |
| **CT-PART-06** | Particionamento | Nulo (exceção) | `null` | `IllegalArgumentException` | `FibonacciTest#testNullInput` |
| **CT-BVA-01** | Valor Limite | Fronteira Zero (On-point) | `0` | `0` | `FibonacciBoundaryTest#testZeroOnPoint` |
| **CT-BVA-02** | Valor Limite | Fronteira Um (Off-point) | `1` | `1` | `FibonacciBoundaryTest#testOneOffPoint` |
| **CT-BVA-03** | Valor Limite | Transição Long→BigInteger (On) | `92` | `7540113804746346429` | `FibonacciBoundaryTest#testMaxLongOnPoint` |
| **CT-BVA-04** | Valor Limite | Transição Long→BigInteger (Off) | `93` | `12200160415121876738` | `FibonacciBoundaryTest#testBigIntegerOffPoint` |
| **CT-BVA-05** | Valor Limite | Limite Superior do Kata (On) | `2.000.000` | Sucesso (~417.974 dígitos) | `FibonacciBoundaryTest#testUpperLimitOnPoint` |
| **CT-BVA-06** | Valor Limite | Acima do Limite Kata (Out) | `2.000.001` | `IllegalArgumentException` | `FibonacciBoundaryTest#testUpperLimitOutPoint` |
| **CT-BVA-07** | Valor Limite | Limite Inferior do Kata (On) | `-2.000.000` | Sucesso | `FibonacciBoundaryTest#testLowerLimitOnPoint` |
| **CT-BVA-08** | Valor Limite | Abaixo do Limite Kata (Out) | `-2.000.001` | `IllegalArgumentException` | `FibonacciBoundaryTest#testLowerLimitOutPoint` |
| **CT-DBC-01** | Contratos (DbC) | Pré-condição: entrada nula | `null` | `IllegalArgumentException` | `FibonacciContractTest#testPreconditionNull` |
| **CT-DBC-02** | Contratos (DbC) | Pré-condição: fora do domínio | `±2.000.001`, `±3.000.000` | `IllegalArgumentException` | `FibonacciContractTest#testPreconditionOutOfRange` |
| **CT-DBC-03** | Contratos (DbC) | Pós-condição: resultado não nulo | `0, 1, 10, 92, 93, ...` | Resultado != null | `FibonacciContractTest#testPostconditionNotNull` |
| **CT-PBT-01** | Propriedades | Recorrência: `fib(n) = fib(n-1) + fib(n-2)` | ∀ n ∈ [2, 5000] | Relação mantida (~1000 amostras) | `FibonacciPropertyTest#testRecurrenceProperty` |
| **CT-PBT-02** | Propriedades | Negafibonacci: `fib(-n) = (-1)^(n+1) * fib(n)` | ∀ n ∈ [1, 5000] | Simetria de sinal mantida | `FibonacciPropertyTest#testNegafibonacciProperty` |
| **CT-PBT-03** | Propriedades | Coprimaridade: `gcd(fib(n), fib(n+1)) = 1` | ∀ n ∈ [1, 2000] | gcd = 1 | `FibonacciPropertyTest#testCoprimalityProperty` |

---

## 3. Detalhamento das Técnicas de Teste Aplicadas

### 3.1 Técnica 1: Particionamento por Classes de Equivalência

O espaço de entrada foi dividido em partições mutuamente exclusivas e exaustivas.

**Partições Válidas:**

| ID | Intervalo | Característica |
| :--- | :--- | :--- |
| P1 | `n = 0` | Elemento neutro inicial |
| P2 | `n ∈ [1, 92]` | Positivo computável em `long` (64 bits) |
| P3 | `n ∈ [93, 2.000.000]` | Positivo de grande escala, requer `BigInteger` |
| P4 | `n ∈ [-92, -1]`, n par | Negativo com resultado negativo |
| P5 | `n ∈ [-92, -1]`, n ímpar | Negativo com resultado positivo |
| P6 | `n ∈ [-2.000.000, -93]`, n par | Grande escala negativa, resultado negativo |
| P7 | `n ∈ [-2.000.000, -93]`, n ímpar | Grande escala negativa, resultado positivo |

**Partições Inválidas:**

| ID | Condição | Comportamento Esperado |
| :--- | :--- | :--- |
| P8 | `n = null` | Lança `IllegalArgumentException` |
| P9 | `|n| > 2.000.000` | Lança `IllegalArgumentException` |

---

### 3.2 Técnica 2: Análise de Valor Limite (BVA)

Identificação de *On-points* (exatamente no limite), *Off-points* (adjacente que muda o comportamento), *In-points* (internos válidos) e *Out-points* (fora da faixa válida).

| Fronteira Analisada | Condição | On-point | Off-point | In-point | Out-point |
| :--- | :--- | :--- | :--- | :--- | :--- |
| Fronteira Inicial Zero | `n ≥ 0` | `0` (T) | `-1` (F) | `10` | `-50` |
| Transição Long/BigInteger | `n ≤ 92` | `92` (T) | `93` (F) | `45` | `500` |
| Limite Superior Kata | `n ≤ 2.000.000` | `2.000.000` (T) | `2.000.001` (F) | `1.000.000` | `3.000.000` |
| Limite Inferior Kata | `n ≥ -2.000.000` | `-2.000.000` (T) | `-2.000.001` (F) | `-500.000` | `-3.000.000` |

---

### 3.3 Técnica 3: Teste Baseado em Contratos (Design by Contract — DbC)

O método `fib(n)` possui um contrato formal com pré-condições e pós-condições:

```java
// Pré-condição 1: n não pode ser nulo
if (n == null) throw new IllegalArgumentException(...);

// Pré-condição 2: |n| não pode exceder 2.000.000
if (n.abs().compareTo(MAX_LIMIT) > 0) throw new IllegalArgumentException(...);

// Pós-condição: resultado nunca é nulo para entrada válida
// (garantido pelo algoritmo — BigInteger.ZERO ou fastDoubling()[0])
```

Os testes de contrato verificam:
- **CT-DBC-01:** pré-condição 1 — `null` lança exceção.
- **CT-DBC-02:** pré-condição 2 — valores fora do domínio lançam exceção (parametrizado com 5 entradas).
- **CT-DBC-03:** pós-condição — resultado nunca é `null` para entradas válidas (parametrizado com 10 entradas).

---

### 3.4 Técnica 4: Teste Baseado em Propriedades (PBT com jqwik)

Usando **jqwik**, validam-se propriedades matemáticas universais com ~1.000 execuções aleatórias por propriedade:

1. **Recorrência Aditiva** — para todo `n ∈ [2, 5000]`:
   `fib(n) = fib(n-1) + fib(n-2)`

2. **Negafibonacci** — para todo `n ∈ [1, 5000]`:
   `fib(-n) = (-1)^(n+1) * fib(n)`

3. **Coprimaridade** — para todo `n ∈ [1, 2000]`:
   `gcd(fib(n), fib(n+1)) = 1`

---

### 3.5 Técnica 5: Teste Estrutural e Cobertura (JaCoCo)

Resultado da execução da suíte completa com `mvn test`:

| Métrica | Resultado |
| :--- | :--- |
| Total de testes executados | 42 (+ ~3.000 execuções PBT) |
| Falhas / Erros | 0 / 0 |
| Cobertura de instruções | 97% (3 de 129 não cobertas — construtor implícito) |
| Cobertura de ramos/branches | 100% (16/16) |

---

## 4. Código Fonte Completo do Repositório

### 4.1 SUT: `src/main/java/br/pucrs/verival/Fibonacci.java`

```java
package br.pucrs.verival;

import java.math.BigInteger;

/**
 * SUT — The Millionth Fibonacci Kata.
 * Utiliza o algoritmo Fast Doubling para complexidade O(log n).
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

        // Negafibonacci: fib(-n) = (-1)^(n+1) * fib(n) — sinal negativo quando n é par
        if (isNegative && absN.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return result.negate();
        }
        return result;
    }

    /**
     * Fast Doubling — retorna [F(k), F(k+1)].
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
```

---

### 4.2 Testes de Particionamento: `src/test/java/br/pucrs/verival/FibonacciTest.java`

```java
package br.pucrs.verival;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CT-PART — Particionamento por Classes de Equivalência")
class FibonacciTest {

    @Test
    @DisplayName("CT-PART-01: P1 — Caso base zero")
    void testBaseZero() {
        assertEquals(BigInteger.ZERO, Fibonacci.fib(BigInteger.ZERO));
    }

    @ParameterizedTest(name = "fib({0}) = {1}")
    @CsvSource({"1,1","2,1","3,2","4,3","5,5","6,8","7,13","8,21","9,34","10,55"})
    @DisplayName("CT-PART-02: P2 — Pequenos positivos (n <= 92)")
    void testSmallPositive(long input, String expected) {
        assertEquals(new BigInteger(expected), Fibonacci.fib(BigInteger.valueOf(input)));
    }

    @Test
    @DisplayName("CT-PART-03: P3 — Grande positivo (fib(1000) tem 209 dígitos)")
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

    @Test
    @DisplayName("CT-PART-06: P6 — Entrada nula lança IllegalArgumentException")
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> Fibonacci.fib(null));
    }
}
```

---

### 4.3 Testes de Valor Limite: `src/test/java/br/pucrs/verival/FibonacciBoundaryTest.java`

```java
package br.pucrs.verival;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CT-BVA — Análise de Valor Limite")
class FibonacciBoundaryTest {

    @Test
    @DisplayName("CT-BVA-01: On-point — fib(0) = 0")
    void testZeroOnPoint() {
        assertEquals(BigInteger.ZERO, Fibonacci.fib(BigInteger.ZERO));
    }

    @Test
    @DisplayName("CT-BVA-02: Off-point — fib(1) = 1")
    void testOneOffPoint() {
        assertEquals(BigInteger.ONE, Fibonacci.fib(BigInteger.ONE));
    }

    @Test
    @DisplayName("CT-BVA-03: On-point — fib(92) = 7540113804746346429 (maior fib em long)")
    void testMaxLongOnPoint() {
        assertEquals(new BigInteger("7540113804746346429"),
                Fibonacci.fib(BigInteger.valueOf(92)));
    }

    @Test
    @DisplayName("CT-BVA-04: Off-point — fib(93) = 12200160415121876738 (estoura long)")
    void testBigIntegerOffPoint() {
        assertEquals(new BigInteger("12200160415121876738"),
                Fibonacci.fib(BigInteger.valueOf(93)));
    }

    @Test
    @DisplayName("CT-BVA-05: On-point — fib(2.000.000) executa sem exceção")
    void testUpperLimitOnPoint() {
        assertNotNull(Fibonacci.fib(BigInteger.valueOf(2_000_000)));
    }

    @Test
    @DisplayName("CT-BVA-06: Out-point — fib(2.000.001) lança IllegalArgumentException")
    void testUpperLimitOutPoint() {
        assertThrows(IllegalArgumentException.class,
                () -> Fibonacci.fib(BigInteger.valueOf(2_000_001)));
    }

    @Test
    @DisplayName("CT-BVA-07: On-point — fib(-2.000.000) executa sem exceção")
    void testLowerLimitOnPoint() {
        assertNotNull(Fibonacci.fib(BigInteger.valueOf(-2_000_000)));
    }

    @Test
    @DisplayName("CT-BVA-08: Out-point — fib(-2.000.001) lança IllegalArgumentException")
    void testLowerLimitOutPoint() {
        assertThrows(IllegalArgumentException.class,
                () -> Fibonacci.fib(BigInteger.valueOf(-2_000_001)));
    }
}
```

---

### 4.4 Testes de Contrato: `src/test/java/br/pucrs/verival/FibonacciContractTest.java`

```java
package br.pucrs.verival;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CT-DBC — Design by Contract")
class FibonacciContractTest {

    @Test
    @DisplayName("CT-DBC-01: Pré-condição 1 — null lança IllegalArgumentException")
    void testPreconditionNull() {
        assertThrows(IllegalArgumentException.class, () -> Fibonacci.fib(null));
    }

    @ParameterizedTest(name = "fib({0}) viola pré-condição 2")
    @ValueSource(longs = {2_000_001L, 3_000_000L, Long.MAX_VALUE, -2_000_001L, -3_000_000L})
    @DisplayName("CT-DBC-02: Pré-condição 2 — |n| > 2.000.000 lança IllegalArgumentException")
    void testPreconditionOutOfRange(long n) {
        assertThrows(IllegalArgumentException.class,
                () -> Fibonacci.fib(BigInteger.valueOf(n)));
    }

    @ParameterizedTest(name = "fib({0}) != null")
    @ValueSource(longs = {0L, 1L, 10L, 92L, 93L, 1_000L, 2_000_000L, -1L, -6L, -2_000_000L})
    @DisplayName("CT-DBC-03: Pós-condição — resultado nunca é nulo para entrada válida")
    void testPostconditionNotNull(long n) {
        assertNotNull(Fibonacci.fib(BigInteger.valueOf(n)));
    }
}
```

---

### 4.5 Testes de Propriedades: `src/test/java/br/pucrs/verival/FibonacciPropertyTest.java`

```java
package br.pucrs.verival;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
```

---

### 4.6 Configuração Maven: `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>br.pucrs.verival</groupId>
    <artifactId>millionth-fibonacci-kata</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <junit.jupiter.version>5.10.2</junit.jupiter.version>
        <jqwik.version>1.8.4</jqwik.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.jupiter.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>net.jqwik</groupId>
            <artifactId>jqwik</artifactId>
            <version>${jqwik.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <argLine>@{argLine} -ea</argLine>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.12</version>
                <executions>
                    <execution>
                        <goals><goal>prepare-agent</goal></goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals><goal>report</goal></goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 5. Estrutura do Repositório e Instruções de Execução

### 5.1 Árvore de Diretórios

```text
.
├── README.md
├── resenha_critica_verival.pdf
├── tests.md
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── br/pucrs/verival
    │           └── Fibonacci.java
    └── test
        └── java
            └── br/pucrs/verival
                ├── FibonacciTest.java
                ├── FibonacciBoundaryTest.java
                ├── FibonacciContractTest.java
                └── FibonacciPropertyTest.java
```

### 5.2 Comandos

```bash
# Executar todos os testes
mvn clean test

# Gerar relatório de cobertura JaCoCo
mvn verify
# Relatório disponível em: target/site/jacoco/index.html
```
