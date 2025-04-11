# Projeto Distribuído

Este projeto é uma aplicação distribuída que consiste em um servidor e um cliente. O servidor é composto por um mestre e dois escravos, que processam requisições HTTP para contar letras e números em um arquivo de texto enviado pelo cliente.

## Estrutura do Projeto

```
projeto-distribuido
├── cliente
│   └── ClienteGUI.class
│   └── ClienteGUI.java
│   └── ClienteGUI$1.class
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
2. Navegue até o diretório `cliente`.
3. Compile e execute o arquivo `ClienteGUI.java`:

   ```
   javac ClienteGUI.java
   java ClienteGUI


4. Ira aparecer uma interface gráfica permitirá que você selecione um arquivo de texto e o envie para o servidor mestre.
![Captura de tela 2025-04-11 112241](https://github.com/user-attachments/assets/3d6ff5a0-9cb6-4b7b-88ef-2b75c65f6faf)

5. Após aparecer a interface basta clicar em "Enviar arquivo" e navegar ate o .txt que deseja enviar
![Captura de tela 2025-04-11 112411](https://github.com/user-attachments/assets/36fb4452-839a-4f98-90c9-f5a4d9a82fff)

6. E após o envio será mostrado o reusltado tanto no log do terminal quanto na interface, com o log no terminal sendo mais detalhado o processo.
   
![Captura de tela 2025-04-11 112411](https://github.com/user-attachments/assets/09f9ebf7-8f95-42ca-8ad4-03c417cf08ba)

7.
![Captura de tela 2025-04-11 112517](https://github.com/user-attachments/assets/0be4f520-fbb4-495b-9b68-9a02624ee9b6)

## Como Funciona


- O cliente envia um arquivo de texto para o servidor mestre.
- O servido mestre confere a disponibilidade dos escravos pela função "escravoDisponivel".
- O servidor mestre distribui a requisição para os escravos:
  - O escravo1 conta o número de letras.
  - O escravo2 conta o número de dígitos.

- O servidor mestre compila as respostas e as retorna ao cliente.

## Requisitos

- Docker e Docker Compose instalados para executar o servidor.
- Java JDK instalado para compilar e executar o cliente.

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir um pull request ou relatar problemas.
