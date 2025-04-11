import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Mestre {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/processar", new MestreHandler());
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();
        System.out.println("Mestre rodando na porta 8080");
    }
}

class MestreHandler implements HttpHandler {
    private static int clienteCounter = 0;

    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            clienteCounter++;
            String clienteIP = exchange.getRemoteAddress().getAddress().getHostAddress();

            System.out.println("\n===========================================");
            System.out.println("🔵 Nova conexão de cliente #" + clienteCounter);
            System.out.println("📍 IP do cliente: " + clienteIP);
            System.out.println("⏰ Hora da conexão: " + java.time.LocalDateTime.now());
            System.out.println("===========================================");

            InputStream is = exchange.getRequestBody();
            String conteudo = new String(is.readAllBytes());
            System.out.println("Conteúdo recebido do cliente:\n" + conteudo);

            // Verificar disponibilidade dos escravos
            boolean escravo1Ok = escravoDisponivel("http://escravo1:8081");
            boolean escravo2Ok = escravoDisponivel("http://escravo2:8082");

            System.out.println("\nVerificando disponibilidade dos escravos...");
            if (!escravo1Ok || !escravo2Ok) {
                StringBuilder erro = new StringBuilder("Alguns escravos estão indisponíveis:\n");
                if (!escravo1Ok) erro.append("🔴 Escravo 1 (letras) fora do ar\n");
                if (!escravo2Ok) erro.append("🔴 Escravo 2 (números) fora do ar\n");

                String msg = erro.toString();
                exchange.sendResponseHeaders(503, msg.length());
                OutputStream os = exchange.getResponseBody();
                os.write(msg.getBytes());
                os.close();
                System.out.println("⚠️ Resposta de erro enviada ao cliente:\n" + msg);
                return;
            }
            else{
                System.out.println("\n--- Escravos disponiveis!");
            }

            System.out.println("\n--- Iniciando processamento nos escravos ---");

            String[] resultados = new String[2];

            Thread t1 = new Thread(() -> {
                System.out.println(">>> [Thread 1] Enviando para escravo1 (letras)...");
                resultados[0] = chamarEscravo("http://escravo1:8081/letras", conteudo);
                System.out.println("<<< [Thread 1] Resposta do escravo1: " + resultados[0]);
            });

            Thread t2 = new Thread(() -> {
                System.out.println(">>> [Thread 2] Enviando para escravo2 (números)...");
                resultados[1] = chamarEscravo("http://escravo2:8082/numeros", conteudo);
                System.out.println("<<< [Thread 2] Resposta do escravo2: " + resultados[1]);
            });

            t1.start(); t2.start();
            try {
                t1.join(); t2.join();
            } catch (InterruptedException e) {
                System.out.println("Erro ao aguardar as threads.");
                e.printStackTrace();
            }

            String respostaFinal = resultados[0] + "\n" + resultados[1];
            System.out.println("Resposta final montada para o cliente:\n" + respostaFinal);

            exchange.sendResponseHeaders(200, respostaFinal.length());
            System.out.println("✅ Código de resposta enviado: 200 (OK)");
            OutputStream os = exchange.getResponseBody();
            os.write(respostaFinal.getBytes());
            os.close();
            System.out.println("--- Resposta enviada ao cliente ---\n");

            System.out.println("✅ Processamento finalizado para cliente #" + clienteCounter);
            System.out.println("===========================================\n");
        } else {
            System.out.println("Método não suportado: " + exchange.getRequestMethod());
        }
    }

    private String chamarEscravo(String urlStr, String dados) {
        try {
            System.out.println("==> Abrindo conexão com: " + urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "text/plain");

            OutputStream os = conn.getOutputStream();
            os.write(dados.getBytes());
            os.flush();
            os.close();
            System.out.println("==> Dados enviados para " + urlStr);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String resposta = in.readLine();
            in.close();

            System.out.println("==> Resposta recebida de " + urlStr);
            return resposta;
        } catch (Exception e) {
            System.out.println("Erro ao chamar escravo em " + urlStr + ": " + e.getMessage());
            return "Erro com escravo: " + urlStr;
        }
    }

    private boolean escravoDisponivel(String urlStr) {
        try {
            URL url = new URL(urlStr + "/status");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            conn.connect();

            int code = conn.getResponseCode();
            return code == 200;
        } catch (IOException e) {
            System.out.println("❌ Escravo indisponível: " + urlStr);
            return false;
        }
    }
}