# Trabalho T1 — Verificação e Validação de Software

**PUCRS — Escola Politécnica**  
**Disciplina:** Verificação e Validação de Software (2026/I)  
**Professor:** Prof. Dr. Marco Aurélio Souza Mangan  
**Autores:** Juliano Machado e Renan Bertoletti  

---

## Visão Geral do Projeto

Este repositório contém a solução completa e a suíte de testes do **Trabalho T1**, cujo objetivo é aplicar e avaliar diversas estratégias de Verificação e Validação (V&V) de software sobre um Sistema Sob Teste (SUT) real, integrando conceitos fundamentais da disciplina com literatura acadêmica recente.

O problema selecionado é o desafio **The Millionth Fibonacci Kata** (nível 3 kyu na plataforma *Codewars*). O desafio exige o cálculo exato do n-ésimo número da sequência de Fibonacci para valores gigantescos de n (até 2.000.000), incluindo o suporte a índices inteiros negativos (n < 0).

---

## Sistema Sob Teste (SUT) e Tecnologias

### Requisitos e Regras do SUT
1. **Precisão Numérica Exata:** Para suportar números com centenas de milhares de dígitos (evitando *arithmetic overflow* em Java para n > 92), a implementação utiliza a classe `java.math.BigInteger`.
2. **Algoritmo Eficiente:** Utilização da técnica de *Fast Doubling* (duplicação rápida baseada nas propriedades matriciais de Fibonacci), reduzindo a complexidade temporal de O(n) para O(log |n|).
3. **Suporte a Índices Negativos:** Aplicação da relação de recorrência invertida `fib(n) = fib(n+2) - fib(n+1)`, gerando a regra de sinal: `fib(-n) = (-1)^(n+1) * fib(n)`

### Tecnologias Utilizadas
* **Linguagem:** Java 17+
* **Framework de Teste:** JUnit 5 (Jupiter)
* **Teste Baseado em Propriedades (PBT):** jqwik
* **Análise de Cobertura:** JaCoCo (Java Code Coverage)
* **Gerenciamento e Automação:** Apache Maven

---

## Estrutura do Repositório

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
    │           └── Fibonacci.java                  # SUT — Fast Doubling O(log n)
    └── test
        └── java
            └── br/pucrs/verival
                ├── FibonacciTest.java              # CT-PART: Particionamento por equivalência
                ├── FibonacciBoundaryTest.java       # CT-BVA:  Análise de valor limite
                ├── FibonacciContractTest.java       # CT-DBC:  Design by Contract
                └── FibonacciPropertyTest.java       # CT-PBT:  Testes baseados em propriedades
```

---

## Estratégias de Verificação e Validação Aplicadas

O projeto implementa uma abordagem multicamadas de V&V, conforme documentado em detalhes no arquivo [`tests.md`](./tests.md):

1. **Particionamento por Classes de Equivalência:** Divisão do domínio de entrada em partições válidas (zero, inteiros positivos pequenos e grandes, inteiros negativos pares e ímpares) e inválidas (valores nulos e fora dos limites operacionais).
2. **Análise de Valor Limite (BVA):** Mapeamento de pontos de fronteira (*On-points*, *Off-points*, *In-points* e *Out-points*) em torno de n=0, n=1, n=-1 e limites de capacidade de memória.
3. **Design por Contrato (DbC):** Definição estrita de pré-condições (`n != null` e `|n| <= 2.000.000`), pós-condições (retorno não nulo e coerente em `BigInteger`) e invariantes de classe com asserções ativas.
4. **Teste Baseado em Propriedades (PBT):** Uso do framework *jqwik* para validação de propriedades matemáticas universais em domínios infinitos:
   * **Identidade de Caso Base:** `fib(0) = 0`, `fib(1) = 1`.
   * **Regra de Sinal Negativa:** `fib(-n) = (-1)^(n+1) * fib(n)`.
   * **Aditividade e Recorrência:** `fib(n+2) = fib(n+1) + fib(n)`.
5. **Teste Estrutural e Cobertura:** Medição de cobertura de linhas, decisões e caminhos com *JaCoCo*, atingindo 100% de cobertura nos métodos do SUT.

---

## Resumo do Referencial Teórico (Item A)

A resenha crítica contida em [`resenha_critica_verival.pdf`](./resenha_critica_verival.pdf) analisa duas referências de destaque da literatura científica em geração automatizada de testes:

1. **Echidna (Grieco et al., ISSTA 2020):** Ferramenta de *fuzzing* para contratos inteligentes na Ethereum. Destaca-se pela alta velocidade e capacidade de identificar falhas de segurança e consumo de gás (*worst-case gas estimation*).
2. **RLCheck (Reddy et al., ICSE 2020):** Abordagem de PBT guiada por Aprendizado por Reforço (Monte Carlo Control). Resolve o problema de geração de entradas válidas sob restrições estritas, gerando de 1,4× a 40× mais testes válidos e diversos que abordagens tradicionais sem desacelerar a execução por instrumentação.

---

## Como Executar o Projeto

### Pré-requisitos
* **Java Development Kit (JDK):** Versão 17 ou superior
* **Apache Maven:** Versão 3.8 ou superior

### Comandos de Compilação e Teste

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/usuario/verival-t1-fibonacci.git
   cd verival-t1-fibonacci
   ```

2. **Executar a suíte completa de testes (Unitários e PBT):**
   ```bash
   mvn clean test
   ```

3. **Gerar o relatório de cobertura do JaCoCo:**
   ```bash
   mvn jacoco:report
   ```
   *O relatório HTML será gerado em `target/site/jacoco/index.html`.*

---

## Autores e Contribuições

* **Alunos:** Juliano Machado e Renan Bertoletti
* **Professor:** Prof. Dr. Marco Aurélio Souza Mangan
* **Instituição:** Pontifícia Universidade Católica do Rio Grande do Sul (PUCRS)
