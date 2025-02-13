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

    public void createChat(String chatName, String chatHost) throws SQLException {
        String sql = "INSERT INTO ChatRooms (name, chathost) VALUES (?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, chatName);
        ps.setString(2, chatHost);
        ps.executeUpdate();
    }

    public ChatRoom findChat(String chatName) throws SQLException {
        String sql = "SELECT * FROM Chat_Rooms WHERE name = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, chatName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) { // if found create chat room object
            String chatHost = rs.getString("chathost");
            return new ChatRoom(chatName, chatHost);
        }
        else { // else return null
            return null;
        }
    }

    public void joinChat(String userName, String chatName) throws SQLException{
        String sql = "INSERT INTO Chat_Members (username, chatname) VALUES (?, ?);";
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

    public void deleteChat(String chatName, String chatHost) throws SQLException {
        String sql = "DELETE FROM ChatRooms WHERE name = ? AND chathost = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, chatName);
        ps.setString(2, chatHost);
        ps.executeUpdate();
    }

    public void sendMessage(Message msg) throws SQLException {
        String sql = "INSERT INTO Messages (username, chatname, type, textmsg, imageurl) VALUES (?,?,?,?,?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, msg.userName());
        ps.setString(2, msg.chatName());
        ps.setString(3, msg.type());
        ps.setString(4, msg.text_msg());
        ps.setString(5, msg.img_url());
        ps.executeUpdate();
    }

    public ArrayList<Message> getHistory(String chatName) throws SQLException{
        ArrayList<Message> history = new ArrayList<>();
        String sql = "SELECT * FROM Messages WHERE chatname = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, chatName);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Message msg = new Message(
                rs.getString("username"), 
                rs.getString("chatname"),
                rs.getTimestamp("timestamp"),
                rs.getString("type"),
                rs.getString("textmsg"),
                rs.getString("imageurl")
            );
            history.add(msg);
        }
        return history;
    }
}