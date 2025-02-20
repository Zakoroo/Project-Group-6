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

    public boolean signup(String nickname, String username, String email, String password) throws SQLException {
    String sql = "INSERT INTO Users (nickname, username, email, password) VALUES(?, ?, ?, ?);";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nickname);
        ps.setString(2, username);
        ps.setString(3, email);
        ps.setString(4, password);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public boolean signin(String username, String password) throws SQLException {
        String sql = "SELECT EXISTS(SELECT 1 FROM Users WHERE username = ? AND password = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBoolean(1);
        }
        else {
            return false;
        }
    }

    public boolean deleteUser(String username, String password) throws SQLException {
        if(this.signin(username, password)) {
            String sql = "DELETE FROM Users WHERE username = ? AND password = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        }
        else {
            return false;
        }
    }
}