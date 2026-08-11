Florescer Studio de Beleza — Sistema de Gestão

Sistema desktop de gestão desenvolvido para o Florescer Studio de Beleza, como projeto da disciplina de Tópicos Integradores do curso de ADS.

Sobre o projeto

A ideia surgiu depois de uma entrevista com a proprietária do salão, pra entender como funciona a rotina de gestão no dia a dia / agendamentos, controle de estoque, financeiro e histórico de atendimentos. A partir disso, desenvolvi um sistema desktop em Java que cobre essas necessidades.

Funcionalidades

- Agendamento de horários e serviços
- Controle de estoque de produtos
- Controle financeiro (entradas e saídas)
- Histórico de clientes e atendimentos

Tecnologias

- Java (Swing) para a interface
- MySQL como banco de dados
- JDBC para a conexão entre a aplicação e o banco
- Desenvolvido no VS Code

Estrutura do projeto

O código está organizado em camadas:

- `model` — entidades do sistema
- `dao` — acesso ao banco de dados
- `controller` — regras de negócio
- `view` — telas da aplicação (Swing)

Como rodar

Pré-requisitos: JDK instalado e MySQL rodando na máquina.

1. Clone o repositório
2. Crie o banco de dados MySQL (script na pasta do projeto)
3. Configure o usuário/senha do banco no arquivo de conexão JDBC
4. Rode o projeto pela IDE

## Autor

Felipe
