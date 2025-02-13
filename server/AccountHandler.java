import java.sql.Connection;
import java.sql.SQLException;

public class AccountHandler {
    private Connection conn;

    public AccountHandler(Connection conn) {
        this.conn = conn;
    }

    public void registerUser(String username, String password) throws SQLException {
        
    }

    public void authenticateUser(String username, String password) throws SQLException {

    }

    public void deleteUser(String username, String password) throws SQLException {

    }
}