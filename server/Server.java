import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

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
        }
        catch (SQLException e1) {
            System.out.println(e1);
        }
        catch (IOException e2) {
            System.out.println(e2);
        }

        // test the to see if the server and database interact properly
        try {
            chatHandler.createChat("The Bros", "kebabmaster");
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }
}