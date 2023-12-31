import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class GroupchatClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int DEFAULT_PORT = 1337;
    private static int port = DEFAULT_PORT;
    private static String GROUPCHAT_NAME;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, port);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            GROUPCHAT_NAME = SERVER_IP + ":" + port;

            // Print information about the server
            System.out.println("Connected to server. Server info: " + SERVER_IP + ":" + port);
                
            // Start a thread to receive messages from the server
            new Thread(() -> receiveMessages(inputStream)).start();

            // Get user input and send messages to the server
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter your message: ");
                String userMessage = scanner.nextLine();
                sendMessage(outputStream, userMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(OutputStream outputStream, String message) throws IOException {
        byte[] messageBytes = message.getBytes();
        outputStream.write(messageBytes);
        outputStream.flush();
    }

    private static void receiveMessages(InputStream inputStream) {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String receivedMessage = new String(buffer, 0, bytesRead);
                System.out.println("Server says: " + receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
