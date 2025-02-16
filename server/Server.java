import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.util.Scanner;

public class Server {
    private Database database;
    private Connection conn;
    private ChatHandler chatHandler;
    private AccountHandler accountHandler;

    public Server() {
        database = new Database( "postgres"); // create database object
        String url = database.url(); // get the url for the database connection
        Properties props = new Properties(); // for reading the user and password from the config file

        try (FileInputStream fis = new FileInputStream("config.properties");){
            props.load(fis); // reading form the config file
            String user = props.getProperty("db.user"); // get the user property 
            String password = props.getProperty("db.password"); // get the password property

            conn = DriverManager.getConnection(url, user, password); // create connection object
            chatHandler = new ChatHandler(conn);
            accountHandler = new AccountHandler(conn);
        }
        catch (SQLException e1) {
            System.out.println(e1);
        }
        catch (IOException e2) {
            System.out.println(e2);
        }

        test(); // run simple test
    }

    private void test() {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Registering users...");
            accountHandler.registerUser("user1", "user1@example.com", "password1");
            accountHandler.registerUser("user2", "user2@example.com", "password2");
            accountHandler.registerUser("user3", "user3@example.com", "password3");
            accountHandler.registerUser("user4", "user4@example.com", "password4");
            accountHandler.registerUser("adina", "adinaan@chalmers.se", "4739840ab490b");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);

        try {
            System.out.println("Trying to register an already registered user...");
            accountHandler.registerUser("user1", "user1@example.com","password1");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);

        try {
            System.out.println("Trying authentication with incorrect password...");
            accountHandler.authenticateUser("user1", "wrongpassword");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);
        
        try {
            System.out.println("Trying to delete a user with incorrect password...");
            accountHandler.deleteUser("user1", "wrongpassword");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);

        try {
            System.out.println("Deleting a user with correct password...");
            accountHandler.deleteUser("user1", "password1");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);

        try {
            System.out.println("Creating chatrooms...");
            chatHandler.createChat("chatroom1", "user2");
            chatHandler.createChat("chatroom2", "user3");
            chatHandler.createChat("chatroom3", "user3");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);

        try {
            System.out.println("Deleting a chat room...");
            chatHandler.deleteChat("chatroom1", "user2");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);

        try {
            System.out.println("Trying to enter a user to a chatroom that doesn't exist...");
            chatHandler.joinChat("user2", "nonexistentChatRoom");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);
        
        try {
            System.out.println("Populating chatrooms with members...");
            chatHandler.joinChat("user2", "chatroom2");
            chatHandler.joinChat("user3", "chatroom2");
            chatHandler.joinChat("user4", "chatroom2");
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);

        try {
            System.out.println("Sending messages to different chatrooms...");
            chatHandler.sendMessage(new Message("user2", "chatroom2", null, "text","Hello, this is a test message.", null));
        } catch (SQLException e) {
            System.out.println(e);
        }
        next(sc);

    }

    private void next(Scanner sc) {
        System.out.println("(Press Enter)");
        sc.nextLine();
    }
}