import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;

public class Escravo2 {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        server.createContext("/numeros", new NumerosHandler());

        // Endpoint de status (igual ao do Escravo1)
        server.createContext("/status", new StatusHandler());

        server.start();
        System.out.println("Escravo2 rodando na porta 8082");
    }

    static class NumerosHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String conteudo = new String(exchange.getRequestBody().readAllBytes());
                long count = conteudo.chars().filter(Character::isDigit).count();
                String resposta = "Numeros: " + count;

                exchange.sendResponseHeaders(200, resposta.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(resposta.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Método não permitido
            }
        }
    }

    static class StatusHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "OK";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
