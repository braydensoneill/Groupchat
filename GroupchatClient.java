import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Random;

public class GroupchatClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int DEFAULT_PORT = 1337;
    private static int port = DEFAULT_PORT;

    private static String DISCONNECT_KEYWORD = "DISCONNECT";
    private static String DISCONNECT_MESSAGE = "You have left the chat";

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, port);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            
            // Initialisation
            Scanner scan = new Scanner(System.in);
            Random rand = new Random();
            String username = String.valueOf(rand.nextInt(999999)); // random name for now

            // Send a message to the server
            sendMessage(outputStream, username);

            // Start a thread to receive messages from the server
            new Thread(() -> receiveMessages(inputStream)).start();

            while (true) {

                String userMessage = scan.nextLine();
                sendMessage(outputStream, userMessage);
            }

        } catch (IOException e) {
            System.out.println(DISCONNECT_MESSAGE);
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
                if (receivedMessage.equals(DISCONNECT_KEYWORD)) {
                    System.out.println(DISCONNECT_MESSAGE);
                    System.exit(0);
                } else {
                    System.out.println(DISCONNECT_MESSAGE);
                }
            }
        } catch (IOException e) {
            System.out.println(DISCONNECT_MESSAGE);
        }
    }
}