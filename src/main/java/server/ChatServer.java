package server;

import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import com.zaxxer.hikari.*;

public class ChatServer {
    public static final int PORT = 8005;

    public ChatServer() {
        // Fetch info for and create a connection pool
        HikariConfig config = new HikariConfig("src/main/resources/db/hikari.properties");
        HikariDataSource dataSource = new HikariDataSource(config);

        // Start up the notificaiton service
        try (
            ServerSocket serverSocket = new ServerSocket(PORT);
        ) {
            // Create a server socket
            System.out.println("Server started. Listening on port " + PORT);

            // Create a notification handler with access to the database
            Connection connection = dbConnectionInit();
            NotificationHandler notificationHandler = new NotificationHandler(connection);
            Thread notificationThread = new Thread(notificationHandler);
            notificationThread.start();
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Create a new client handler thread
                ClientHandler clientHandler = new ClientHandler(clientSocket, dataSource, notificationHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure closing the connection pool
            dataSource.close();
        }
    }

    private Connection dbConnectionInit() throws IOException, SQLException {
        try (FileInputStream fis = new FileInputStream("src/main/resources/db/config.properties")) {
            Properties props = new Properties();
            props.load(fis);
            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port", "5432");
            String databaseName = props.getProperty("db.name");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
            return DriverManager.getConnection(url, user, password);
        }
    }
}