package client;
import shared.Container;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;

public class ClientDriver {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 8005;

        try (Socket socket = new Socket(serverAddress, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            RequestHandler requestHandler = new RequestHandler(oos, ois);

/*             // 1. Signup
            System.out.println("Testing signup...");
            requestHandler.signup("TestNick", "TestUser", "test@example.com", "password123");  */

            // 2. Signin
            System.out.println("Testing signin...");
            requestHandler.signin("TestUser", "password123"); 
/* 
             // 3. Create Chatroom
            System.out.println("Testing create chat...");
            requestHandler.createChat("TestChat"); */
/* 
            // 4. Join Chatroom
            System.out.println("Testing join chat...");
            requestHandler.joinChat("TestChat"); */
/*
            // 5. Connect to Chatroom
            System.out.println("Testing connect chat...");
            requestHandler.connectChat("TestChat");

            // 6. Find Chat
            System.out.println("Testing find chat...");
            requestHandler.findChat("Test");

            // 7. Send Text Message
            System.out.println("Testing send message (text)...");
            requestHandler.sendMessage("Hello, this is a test message!");

            // 8. Send Image
            System.out.println("Testing send message (image)..."); 
            byte[] dummyImageData = new byte[]{1, 2, 3, 4, 5}; // Simulating binary data
            requestHandler.sendMessage(dummyImageData);

            // 9. Get Chat History
            System.out.println("Testing get history...");
            requestHandler.getHistory(new Timestamp(System.currentTimeMillis() - 36000000)); // 10 hours ago

            // 10. Quit Chat
            System.out.println("Testing quit chat...");
            requestHandler.quitChat("TestChat");

            // 11. Delete User
            System.out.println("Testing delete user...");
            requestHandler.deleteUser("password123"); */

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}