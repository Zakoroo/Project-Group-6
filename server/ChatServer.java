import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    public static final int PORT = 8005;

    // Thread-safe list to keep track of all client output stream
    private static List<PrintWriter> clientWriters = new CopyOnWriteArrayList<>();
    private Connection databaseConnection;
    private ChatHandler chatHandler;
    private AccountHandler accountHandler;
    private ServerSocket serverSocket;

    public ChatServer() {
        // Establish connection with the database
        try (FileInputStream fis = new FileInputStream("config.properties");) {
            Properties props = new Properties();
            props.load(fis); 
            String databaseName = props.getProperty("db.name");
            String user = props.getProperty("db.user"); 
            String password = props.getProperty("db.password");
            databaseConnection = DriverManager.getConnection("jdbc:postgresql:" + databaseName, user, password); 
            chatHandler = new ChatHandler(databaseConnection);
            accountHandler = new AccountHandler(databaseConnection);
        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            System.out.println("Server started. Listening on port " + PORT);

            while(true) {
                Socket clientSocket = serverSocket.accept();

                // Create a new client handler thread
                ClientHandler clientHandler = new ClientHandler(accountHandler, chatHandler, clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Broadcast a message to all connected writers
    public static void broadcast(String message) {
        for(PrintWriter writer: clientWriters) {
            writer.println(message);
        } 
    }

    // Add a client writer to the list
    public static void addClientWriter(PrintWriter writer) {
        clientWriters.add(writer);
    }

    // Remove a client from the list
    public static void removeClientWriter(PrintWriter writer) {
        clientWriters.remove(writer);
    } 
}