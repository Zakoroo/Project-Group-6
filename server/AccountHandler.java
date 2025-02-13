import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSet;

public class AccountHandler {
    private Connection conn;

    public AccountHandler(Connection conn) {
        this.conn = conn;
    }

    public void registerUser(String username, String email, String password) throws SQLException {
        String sql = "INSERT INTO Users VALUES(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.executeUpdate();
    }

    public User authenticateUser(String username, String password) throws SQLException {
        if(!password.equals(getPassword(username))) {
            throw new SQLException("Password incorrect!");
        }

        String sql = "SELECT name, email FROM Users WHERE name = ? AND password = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new User(username, rs.getString("email"), password);
        }
        else {
            return null;
        }
    }

    public void deleteUser(String username, String password) throws SQLException {
        if(!password.equals(getPassword(username))) {
            throw new SQLException("Password incorrect!");
        }

        String sql = "DELETE FROM Users WHERE name = ? AND password = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.executeUpdate();
    }

    private String getPassword(String username) throws SQLException {
        String sql = "SELECT password FROM Users WHERE name = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return rs.getString("password");
        }
        else  {
            throw new SQLException(String.format("The user %d does not exist!", username));
        }
    }
}