package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.postgresql.PGNotification;
import org.postgresql.PGConnection;
import shared.Container;
import shared.Message;


public class NotificationHandler implements Runnable {
    private Connection connection;
    private ConcurrentMap<String, CopyOnWriteArrayList<ObjectOutputStream>> chatListeners;

    public NotificationHandler(Connection connection) {
        this.connection = connection;
        this.chatListeners = new ConcurrentHashMap<>();
    }

    public void addChatRoom(String chatname) {
        if (chatListeners.get(chatname) == null) {
            chatListeners.put(chatname, new CopyOnWriteArrayList<>());
        }
    }

    public void removeChatRoomIfEmpty(String chatname) {
        if (chatListeners.get(chatname).isEmpty()) {
            chatListeners.remove(chatname);
        }
    }

    public void addListener(String chatname, ObjectOutputStream listener) {
        if (chatListeners.get(chatname) != null) {
            chatListeners.get(chatname).add(listener);
        } else {
            addChatRoom(chatname);
            chatListeners.get(chatname).add(listener);
        }
    }

    public void removeListener(String chatname, ObjectOutputStream listener) {
        if (chatListeners.get(chatname) != null) {
            chatListeners.get(chatname).remove(listener);
            removeChatRoomIfEmpty(chatname);
        }
    }

    @Override
    public void run() {
        try (
                Statement st = connection.createStatement();) {
            // Listen to the channel
            st.execute("LISTEN update_channel");
            System.out.println("Listening to notifications on channel 'update_channel'...");
            PGConnection pgConnection = (PGConnection) connection;

            while (true) {
                PGNotification[] notifications = pgConnection.getNotifications(10000);
                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        broadcast(notification.getParameter());
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // make sure that connection gets closed
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(String chatname) {
        if (chatListeners.get(chatname) != null) {
            String sql = "SELECT * FROM Messages WHERE chatname = ? ORDER BY timestamp DESC LIMIT 1;";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, chatname);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String type = rs.getString("type");
                    Message msg;
                    if (type.equals("text")) {
                        msg = new Message(
                                rs.getString("username"),
                                rs.getString("textmsg"),
                                rs.getTimestamp("timestamp"));
                    } else {
                        msg = new Message(
                                rs.getString("username"),
                                rs.getBytes("imagedata"),
                                rs.getTimestamp("timestamp"));
                    }
                    Container container = new Container("send-message", msg);
                    chatListeners.get(chatname).forEach(os -> {
                        try {
                            os.writeObject(container);
                            os.flush();
                        } catch (IOException e) {
                            // remove listener if diconnected
                            removeListener(chatname, os);
                        }
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
