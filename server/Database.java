import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/chatapp"; // Replace with your database URL
    private static final String USER = "postgres"; // Replace with your PostgreSQL username
    private static final String PASSWORD = "chalmers"; // Replace with your PostgreSQL password
    private Connection connection;

    public Database() {
        connect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    public void query(String sql) {
        try (Statement statement = connection.createStatement()) {
            if (sql.trim().toUpperCase().startsWith("SELECT")) {
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    System.out.println("Result: " + rs.getString(1)); // Customize this for your result
                }
            } else {
                int rowsAffected = statement.executeUpdate(sql);
                System.out.println("Query executed successfully, " + rowsAffected + " rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("Query failed: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }
}
