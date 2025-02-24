package server;

import java.sql.*;
import org.postgresql.*;

public class NotificationHandler implements Runnable {
    Connection connection;

    public NotificationHandler(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try (
            Statement sql = connection.createStatement();
        ) {
            // Listen to the channel
            sql.execute("LISTEN update_channel");
            System.out.println("Listening to notifications on channel 'my_channel'...");

            while (true) {
                // Check for notifications
                ResultSet rs = sql.executeQuery("SELECT 1");
                while (rs.next()) {
                    // Process notifications
                    PGNotification notifications[] = ((PGConnection) connection).getNotifications();
                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            System.out.println("Received notification: " + notification.getParameter());
                        }
                    }
                }

                // Sleep to avoid busy-waiting
                Thread.sleep(1000);
            }

        } catch (SQLException | InterruptedException e) {
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
}
