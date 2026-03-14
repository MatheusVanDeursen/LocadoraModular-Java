# LocadoraModular-Java

Sistema desktop para gestão de locadora de veículos desenvolvido em **Java (Swing)**. O foco central deste projeto é a aplicação rigorosa de padrões arquiteturais para garantir **modularidade**, **segurança** e **resiliência de dados**.

---

## Demonstração em Vídeo
Confira o funcionamento do sistema e a explicação detalhada da arquitetura no link abaixo:
> [**Assistir apresentação do projeto no YouTube**](link) A adicionar

---

## Arquitetura e Padrões de Projeto

O sistema foi estruturado seguindo uma abordagem de **baixo acoplamento**, permitindo que a camada de dados seja substituída (ex: JDBC para REST) com impacto mínimo no restante do código.

* **MVP (Model-View-Presenter):** Separação estrita entre a interface (View passiva) e a lógica de orquestração (Presenter).
* **Arquitetura em Camadas (N-Tier):** Divisão clara em VIEW, PRESENTERS, SERVICES, MODELS e DAO.
* **Camada de Persistência (DAO):** Isolamento total da lógica de acesso aos dados utilizando o padrão Data Access Object.

---

## Destaques Técnicos

* **Controle Transacional Atômico (ACID):** Gerenciamento manual de transações (`setAutoCommit(false)`) no JDBC, garantindo que operações críticas ocorram de forma íntegra.
* **Segurança:** Proteção total contra *SQL Injection* via `PreparedStatement` e criptografia unidirecional de senhas com hash SHA-256.
* **Validação Fail-Fast:** Uso de Expressões Regulares (Regex) para validar dados sensíveis (CPF, CEP, Email) antes do processamento.
* **Tratamento de Exceções de Domínio:** Uso de exceções customizadas (`RegraNegocioException`) para separar falhas técnicas de violações de regras de negócio.

---

## Tecnologias Utilizadas

* **Linguagem:** Java (JDK 17+)
* **Interface Gráfica:** Java Swing
* **Banco de Dados:** MySQL
* **Persistência:** JDBC Puro

---

## Como Executar

1.  Clone o repositório.
2.  Execute o script SQL disponível na pasta `/sql` para criar e popular o banco de dados.
3.  Configure as credenciais de acesso ao MySQL na classe de conexão JDBC.
4.  Execute a classe principal de Login para iniciar a aplicação.

---

## Screenshots

A adicionar

---
Desenvolvido por **MatheusVanDeursen**.
