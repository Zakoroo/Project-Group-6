import java.sql.*;
import com.zaxxer.hikari.HikariDataSource;

public class AccountHandler {
    private HikariDataSource dataSource;

    public AccountHandler(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean signup(String nickname, String username, String email, String password) throws SQLException {
        String sql = "INSERT INTO Users (nickname, username, email, password) VALUES(?, ?, ?, ?);";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
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
        String sql = "SELECT 1 FROM Users WHERE username = ? AND password = ?;";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String username, String password) throws SQLException {
        String sql = "DELETE FROM Users WHERE username = ? AND password = ?;";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}