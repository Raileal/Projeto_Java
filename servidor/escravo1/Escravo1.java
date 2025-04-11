import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;

public class Escravo1 {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // Endpoint principal
        server.createContext("/letras", new Handler());

        // Novo endpoint de status
        server.createContext("/status", exchange -> {
            String resposta = "OK";
            exchange.sendResponseHeaders(200, resposta.length());
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Escravo1 rodando na porta 8081");
    }

    static class Handler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String conteudo = new String(exchange.getRequestBody().readAllBytes());
            long count = conteudo.chars().filter(Character::isLetter).count();
            String resposta = "Letras: " + count;
            exchange.sendResponseHeaders(200, resposta.length());
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        }
    }
}