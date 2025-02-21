import java.sql.*;
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
        }
        return false;
    }

    public ChatRoom findChat(String chatName) {
        try {
            String sql = "SELECT * FROM ChatRooms WHERE chatname = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, chatName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String chatHost = rs.getString("chathost");
                return new ChatRoom(chatName, chatHost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean joinChat(String username, String chatname) {
        try {
            String sql = "INSERT INTO ChatMembers (username, chatname) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, chatname);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean quitChat(String username, String chatname) {
        try {
            String sql = "DELETE FROM ChatMembers WHERE username = ? AND chatname = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, chatname);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMessage(Message msg, String chatname) {
        try {
            String sql = "INSERT INTO Messages (username, chatname, type, textmsg, imagedata, timestamp) VALUES (?,?,?,?,?, CURRENT_TIMESTAMP);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, msg.username());
            ps.setString(2, chatname);
            ps.setString(3, msg.type());
            ps.setString(4, msg.text());
            ps.setBytes(5, msg.image());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Message> getHistory(String chatName, Timestamp timestamp) {
        ArrayList<Message> history = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Messages WHERE chatname = ? AND timestamp > ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, chatName);
            ps.setTimestamp(2, timestamp);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
}