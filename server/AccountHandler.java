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

    public void signup(String nickname, String username, String email, String password) throws SQLException {
        String sql = "INSERT INTO Users VALUES(?,?,?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.executeUpdate();
    }

    public boolean signin(String username, String password) throws SQLException {
        String sql = "SELECT EXISTS(SELECT 1 FROM Users WHERE username = ? AND password = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        return Boolean.parseBoolean(rs.getString(1));
    }

    public boolean deleteUser(String username, String password) throws SQLException {
        if(this.signin(username, password)) {
            String sql = "DELETE FROM Users WHERE name = ? AND password = ?;";
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