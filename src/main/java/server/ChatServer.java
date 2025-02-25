package server;

import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import com.zaxxer.hikari.*;

public class ChatServer {
    public static final int PORT = 8005;
    NotificationHandler notificationHandler;

    public ChatServer() {
        // Fetch info for and create a connection pool
        HikariConfig config = new HikariConfig("src/main/resources/db/hikari.properties");
        HikariDataSource dataSource = new HikariDataSource(config);

        // Start up the notificaiton service
        try (FileInputStream fis = new FileInputStream("src/main/resources/db/config.properties")) {
            Properties props = new Properties();
            props.load(fis);
            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port", "5432");
            String databaseName = props.getProperty("db.name");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
            Connection connection = DriverManager.getConnection(url, user, password);
            notificationHandler = new NotificationHandler(connection);
            Thread notificationThread = new Thread(notificationHandler);
            notificationThread.start();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            dataSource.close();
            return;
        }

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

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure closing the connection pool
            dataSource.close();
        }
    }
}