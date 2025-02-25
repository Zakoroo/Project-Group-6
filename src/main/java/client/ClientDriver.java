package client;

import java.io.*;
import java.net.*;
import shared.Container;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientDriver extends Application{
    public static void main(String[] args) {
        int PORT = 8005;
        String serverAddress = "127.0.0.1";
        launch(args);

        try (
            Socket socket = new Socket(serverAddress, PORT);
        ) {
            System.out.println("Connected to the server");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Object responseObject;
            Container responseContainer;

            // Request signup
            System.out.println("Signup request sent");
            Container singupRequest = new Container("signup", "nickname=abdo&username=abood&email=abdo@gmail.com&password=123456");
            oos.writeObject(singupRequest);
            oos.flush();   
            
            // Signup response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer);
            
            //request signin
            System.out.println("Signin request sent");
            Container singinRequest = new Container("signin", "username=abood&password=123456");
            oos.writeObject(singinRequest);
            oos.flush();

            // Signin response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer);
            
            // Request user delete
            //System.out.println("Delete user request sent");
            //Container deleteUserRequest = new Container("delete-user", "username=Hussein&password=123456");
            //oos.writeObject(deleteUserRequest);
            //oos.flush(); 

            // Delete user response
            //System.out.println("Container received");
            //responseObject = ois.readObject();
            //responseContainer = (Container) responseObject;
            //System.out.println(responseContainer);

            // Create chatroom request
            System.out.println("Create chatroom request sent");
            Container createChatRequest = new Container("create-chat", "chatname=Intruders");
            oos.writeObject(createChatRequest);
            oos.flush(); 

            // Create chatroom response
            responseObject = ois.readObject();
            System.out.println("Container received");
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }
}