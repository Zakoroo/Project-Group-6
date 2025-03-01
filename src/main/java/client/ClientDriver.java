package client;
import shared.Container;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.Timestamp;

public class ClientDriver {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 8005;

        try (Socket socket = new Socket(serverAddress, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            RequestHandler requestHandler = new RequestHandler(oos, ois);
 
            // 1. Signup
            //requestHandler.signup("TestNick", "TestUser", "test@example.com", "password123");
 
            // 2. Signin
            requestHandler.signin("TestUser", "password123"); 
 
            // 3. Create Chatroom
            //requestHandler.createChat("TestChat"); 

            // 4. Join Chatroom
            //requestHandler.joinChat("TestChat");

            // 5. Connect to Chatroom
            requestHandler.connectChat("TestChat");

            String imagePath = "C:\\Users\\user\\Desktop\\cat.jpg"; //change the path 
            byte[] imageData = convertImageToByteArray(imagePath);

            // 6. Find Chat
            //requestHandler.findChat("Test");

            // 7. Send Text Message
            //requestHandler.sendMessage("Hello, this is a test message!");

            // 8. Send Image
            requestHandler.sendMessage(imageData);
 
            // 9. Get Chat History
            requestHandler.getHistory(new Timestamp(System.currentTimeMillis() - 36000000)); // 10 hours ago

            // 10. Quit Chat
            //requestHandler.quitChat("TestChat");

            // 11. Delete User
            //requestHandler.deleteUser("password123");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static byte[] convertImageToByteArray(String imagePath) {
        File imageFile = new File(imagePath);
        try {
            return Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            System.err.println("Error reading image file: " + e.getMessage());
            return null;
        }
    }

}
