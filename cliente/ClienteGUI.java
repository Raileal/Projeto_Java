import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClienteGUI extends JFrame {

    private JTextArea resultadoArea;
    private JButton enviarBtn;
    private JFileChooser fileChooser;

    public ClienteGUI() {
        setTitle("Cliente - Contador de Letras e Números");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        resultadoArea = new JTextArea();
        enviarBtn = new JButton("Enviar Arquivo");

        // Filtro para arquivos .txt
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Arquivos txt (*.txt)";
            }
        });

        enviarBtn.addActionListener(e -> enviarArquivo());

        add(enviarBtn, "North");
        add(new JScrollPane(resultadoArea), "Center");

        setVisible(true);
    }

    private void enviarArquivo() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Arquivo selecionado: " + file.getAbsolutePath());

            try {
                byte[] data = Files.readAllBytes(file.toPath());
                System.out.println("Conteúdo do arquivo lido (bytes): " + data.length + " bytes");

                URL url = new URL("http://192.168.18.113:8080/processar"); // Substitua <IP_DO_SERVIDOR> pelo IP do computador que executa o servidor
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "text/plain");

                System.out.println("Enviando dados para o mestre...");
                conn.getOutputStream().write(data);
                System.out.println("Dados enviados com sucesso!");

                int responseCode = conn.getResponseCode();
                System.out.println("Código de resposta do mestre: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                resultadoArea.setText("");
                System.out.println("Resposta recebida do mestre:");
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    resultadoArea.append(line + "\n");
                }
                in.close();
                System.out.println("Leitura da resposta concluída.");

            } catch (Exception ex) {
                ex.printStackTrace();
                resultadoArea.setText("Erro ao enviar arquivo");
                System.out.println("Erro ao enviar o arquivo:");
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Iniciando Cliente GUI...");
        new ClienteGUI();
    }
}