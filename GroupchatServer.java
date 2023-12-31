import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GroupchatServer {
    private static final int DEFAULT_PORT = 1337;
    private static int port = DEFAULT_PORT;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Print information about the new client
                System.out.println("New user: " + clientSocket.getPort() + "-" + clientSocket.getInetAddress());

                // Start a thread to handle messages from the client
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String clientMessage = new String(buffer, 0, bytesRead);
                System.out.println(clientSocket.getInetAddress() + "-" + clientSocket.getPort() + ": " + clientMessage);

                // You can choose not to echo the message back to the client if desired
                // outputStream.write(buffer, 0, bytesRead);
                // outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
