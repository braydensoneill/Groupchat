import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupchatServer {
    private static final int DEFAULT_PORT = 1337;
    private static int port = DEFAULT_PORT;
    private static final String GROUPCHAT_NAME = "g_name";
    private static final List<String> GROUPCHAT_USERS = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

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

            // Receive the username from the client
            bytesRead = inputStream.read(buffer);
            String username = new String(buffer, 0, bytesRead);
            System.out.println(username + " has joined the chat!");
            
            // Add the username to the list of users
            GROUPCHAT_USERS.add(username);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String clientMessage = new String(buffer, 0, bytesRead);
            
                // Check if the client wants to disconnect
                if (clientMessage.equals("DISCONNECT")) {
                    System.out.println(username + " has left the chat!");
                    GROUPCHAT_USERS.remove(username);
                    break;
                }
            
                // Print the received message to the console
                System.out.println("Message received from " + username + ": '" + clientMessage + "'");
            
                // Broadcast the message to all clients
                String broadcastMessage = username + ": " + clientMessage;
                broadcastMessageToAll(broadcastMessage);
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

    private static void broadcastMessageToAll(String message) {
        for (String user : GROUPCHAT_USERS) {
            sendMessageToUser(user, message);
        }
    }

    private static void sendMessageToUser(String username, String message) {
        // Implement logic to send a message to a specific user
        // This could involve finding the user's socket and sending the message
        // through the user's output stream.
        // For simplicity, we print the message here.
        System.out.println("Message sent to " + username + ": " + message + "'");
    }
}