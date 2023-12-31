import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GroupchatClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int DEFAULT_PORT = 1337;
    private static int port = DEFAULT_PORT;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, port);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            String message = "Hello, Server!";
            byte[] messageBytes = message.getBytes();

            outputStream.write(messageBytes);
            outputStream.flush();

            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);

            String echoedMessage = new String(buffer, 0, bytesRead);
            System.out.println("Received from server: " + echoedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}