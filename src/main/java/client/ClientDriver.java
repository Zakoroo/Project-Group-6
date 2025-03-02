package client;

import java.io.*;
import java.net.*;
import shared.Container;


public class ClientDriver {
    public static void main(String[] args) {
        int PORT = 8005;
        String serverAddress = "127.0.0.1";

        try (
            Socket socket = new Socket(serverAddress, PORT);
        ) {
            System.out.println("Connected to the server");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Object responseObject;
            Container responseContainer;
    
            //request signin
            System.out.println("Signin request sent");
            Container singinRequest = new Container("signin", "username=Kebab&password=123456");
            oos.writeObject(singinRequest);
            oos.flush();
            
            // Signin response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); 

            //connect-chat request
            System.out.println("connect-chat request sent");
            Container connecContainer = new Container("connect-chat", "chatname=Example chatroom");
            oos.writeObject(connecContainer);
            oos.flush();

            // connect response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); 

            //Create create-chat request
            System.out.println("send-message request sent");
            Container messageContainer = new Container("send-message", "Hello this is a testing message!");
            oos.writeObject(messageContainer);
            oos.flush();

            // message sending response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}