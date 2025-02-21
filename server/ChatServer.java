import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
// import java.util.concurrent.CopyOnWriteArrayList;
import com.zaxxer.hikari.*;

public class ChatServer {
    public static final int PORT = 8005;
    // private static List<PrintWriter> clientWriters = new  CopyOnWriteArrayList<>();

    public ChatServer() {
        // Fetch info for and create a connection pool 
        HikariConfig config = new HikariConfig("/hikari.properties");
        HikariDataSource dataSource = new HikariDataSource(config);

        // Create a server socket
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Create a new client handler thread
                ClientHandler clientHandler = new ClientHandler(clientSocket, dataSource);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure closing the connection pool
            dataSource.close();
        }
    }
}