import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.util.Scanner;

public class Server {
    private Connection databaseConnection;
    private ChatHandler chatHandler;
    private AccountHandler accountHandler;
    private HttpConnectionHandler httpConnectionHandler;

    public Server() {
        try (FileInputStream fis = new FileInputStream("config.properties");) {
            Properties props = new Properties();
            props.load(fis); 
            String databaseName = props.getProperty("db.name");
            String user = props.getProperty("db.user"); 
            String password = props.getProperty("db.password");
            databaseConnection = DriverManager.getConnection("jdbc:postgresql:" + databaseName, user, password); // create connection object
            chatHandler = new ChatHandler(databaseConnection);
            accountHandler = new AccountHandler(databaseConnection);
        }
        catch (SQLException | IOException e) {
            System.out.println(e);
        }


        try {
            httpConnectionHandler = new HttpConnectionHandler();            
            httpConnectionHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}