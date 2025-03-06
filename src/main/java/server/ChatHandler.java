package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.zaxxer.hikari.HikariDataSource;
import shared.*;

public class ChatHandler {
    private HikariDataSource dataSource;

    public ChatHandler(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ChatRoom createChat(String chatName, String chatHost) {
        String sql = "INSERT INTO ChatRooms (chatname, chathost) VALUES (?, ?);";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, chatName);
            ps.setString(2, chatHost);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return new ChatRoom(chatName, chatHost, new ArrayList<Message>()); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ChatRoom> findChat(String searchTerm) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        String sql = "SELECT * FROM ChatRooms WHERE chatname ~ ? ORDER BY chatname ASC;";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, searchTerm);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String chatname = rs.getString("chatname");
                String chatHost = rs.getString("chathost");
                chatRooms.add(new ChatRoom(chatname, chatHost, new ArrayList<>()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatRooms;
    }

    public ChatRoom joinChat(String username, String chatname) {
        String sql1 = "INSERT INTO ChatMembers (username, chatname) VALUES (?, ?);";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, username);
            ps1.setString(2, chatname);
            int rowsAffected = ps1.executeUpdate();
            
            if (rowsAffected > 0) {
                String sql2 = "SELECT * FROM ChatRooms WHERE chatname = ?;";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setString(1, chatname);

                ResultSet rs = ps2.executeQuery();
                if(rs.next()) {
                    return new ChatRoom(rs.getString("chatname"), rs.getString("chathost"), new ArrayList<>());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChatRoom connectChat(String username, String chatname) {
        String sql = "SELECT * FROM Chatrooms WHERE chatname = ? AND EXISTS(SELECT 1 FROM ChatMembers WHERE chatname = ? AND username = ?);";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, chatname);
            ps.setString(2, chatname);
            ps.setString(3, username);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                return new ChatRoom(chatname, rs.getString("chathost"), new ArrayList<>());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean quitChat(String username, String chatname) {
        String sql = "DELETE FROM ChatMembers WHERE username = ? AND chatname = ?;";
        try (Connection conn = dataSource.getConnection()) {
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

    public List<ChatRoom> getJoinedChatRooms(String username) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        String sql = "SELECT * FROM Chatrooms WHERE chatname IN (SELECT chatname FROM Chatmembers WHERE username = ?);";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String chatname = rs.getString("chatname");
                String chatHost = rs.getString("chathost");
                chatRooms.add(new ChatRoom(chatname, chatHost, new ArrayList<>()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatRooms;
    } 

    public boolean sendMessage(Message msg, String chatname) {
        String sql = "INSERT INTO Messages (username, chatname, type, textmsg, imagedata, timestamp) VALUES (?,?,?,?,?, CURRENT_TIMESTAMP);";
        try (Connection conn = dataSource.getConnection()) {
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

    public List<Message> getHistory(String chatName, Timestamp timestamp) {
        List<Message> history = new ArrayList<>();
        String sql = "SELECT * FROM Messages WHERE chatname = ? AND timestamp > ?;";
        try (Connection conn = dataSource.getConnection()) {
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