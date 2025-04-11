# Projeto Distribuído

Este projeto é uma aplicação distribuída que consiste em um servidor e um cliente. O servidor é composto por um mestre e dois escravos, que processam requisições HTTP para contar letras e números em um arquivo de texto enviado pelo cliente.

## Estrutura do Projeto

```
projeto-distribuido
├── servidor
│   ├── mestre
│   │   ├── Mestre.java
│   │   └── Dockerfile
│   ├── escravo1
│   │   ├── Escravo1.java
│   │   └── Dockerfile
│   ├── escravo2
│   │   ├── Escravo2.java
│   │   └── Dockerfile
│   └── docker-compose.yml
├── cliente
│   └── src
│       └── ClienteGUI.java
└── README.md
```

## Instruções de Configuração

### Para o Servidor

1. Navegue até o diretório `servidor`.
2. Execute o comando a seguir para construir e iniciar os containers Docker:

   ```
   docker-compose up --build
   ```

3. O servidor mestre estará disponível na porta `8080`, o escravo1 na porta `8081` e o escravo2 na porta `8082`.

### Para o Cliente

1. Certifique-se de que o servidor está em execução.
2. Navegue até o diretório `cliente/src`.
3. Compile e execute o arquivo `ClienteGUI.java`:

   ```
   javac ClienteGUI.java
   java ClienteGUI
   ```

4. A interface gráfica permitirá que você selecione um arquivo de texto e o envie para o servidor mestre.

## Como Funciona

- O cliente envia um arquivo de texto para o servidor mestre.
- O servidor mestre distribui a requisição para os escravos:
  - O escravo1 conta o número de letras.
  - O escravo2 conta o número de dígitos.
- O servidor mestre compila as respostas e as retorna ao cliente.

## Requisitos

- Docker e Docker Compose instalados para executar o servidor.
- Java JDK instalado para compilar e executar o cliente.

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir um pull request ou relatar problemas.