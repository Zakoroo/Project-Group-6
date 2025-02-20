import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatHandler {
    private Connection conn;

    public ChatHandler(Connection conn) {
        this.conn = conn;
    }

    public boolean createChat(String chatName, String chatHost) {
        try {
            String sql = "INSERT INTO ChatRooms (chatname, chathost) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, chatName);
            ps.setString(2, chatHost);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ChatRoom findChat(String chatName) throws SQLException {
        String sql = "SELECT * FROM ChatRooms WHERE name = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, chatName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) { // if found create chat room object
            String chatHost = rs.getString("chathost");
            return new ChatRoom(chatName, chatHost);
        } else { // else return null
            return null;
        }
    }

    public void joinChat(String userName, String chatName) throws SQLException {
        String sql = "INSERT INTO ChatMembers (username, chatname) VALUES (?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, chatName);
        ps.executeUpdate();
    }

    public void quitChat(String userName, String chatName) throws SQLException {
        String sql = "DELETE FROM ChatMembers WHERE username = ? AND chatname = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, chatName);
        ps.executeUpdate();
    }

    /*
     * public void deleteChat(String chatName, String chatHost) throws SQLException
     * {
     * String sql = "DELETE FROM ChatRooms WHERE name = ? AND chathost = ?;";
     * PreparedStatement ps = conn.prepareStatement(sql);
     * ps.setString(1, chatName);
     * ps.setString(2, chatHost);
     * ps.executeUpdate();
     * }
     */

    public void sendMessage(Message msg, String chatname) throws SQLException {
        String sql = "INSERT INTO Messages (username, chatname, type, textmsg, imagedata, timestamp) VALUES (?,?,?,?,?, CURRENT_TIMESTAMP);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, msg.username());
        ps.setString(2, chatname);
        ps.setString(3, msg.type());
        ps.setString(4, msg.text());
        ps.setBytes(5, msg.image());
        ps.executeUpdate();
    }

    public ArrayList<Message> getHistory(String chatName) throws SQLException {
        ArrayList<Message> history = new ArrayList<>();
        String sql = "SELECT * FROM Messages WHERE chatname = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, chatName);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Message msg = new Message(
                    rs.getString("username"),
                    rs.getString("type"),
                    rs.getString("textmsg"),
                    rs.getBytes("imagedata"),
                    rs.getTimestamp("timestamp"));
            history.add(msg);
        }
        return history;
    }
}