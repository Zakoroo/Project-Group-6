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
            Container singinRequest = new Container("signin", "username=abood&password=123456");
            oos.writeObject(singinRequest);
            oos.flush();

            // Signin response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); 


/*          // Create Join-chat request request
            System.out.println("join chat request sent");
            Container joinchatrequest = new Container("join-chat", "username=kebab&chatname=Intruders");
            oos.writeObject(joinchatrequest);
            oos.flush();

            // Join-chat response
            responseObject = ois.readObject();
            System.out.println("Container received");
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); */


/*          // Create quit-chat request request
            System.out.println("quit chat request sent");
            Container quitchatrequest = new Container("QuitChat", "username=abood&chatname=Intruders");
            oos.writeObject(quitchatrequest);
            oos.flush();
            
            // Create quit-chat response
            responseObject = ois.readObject();
            System.out.println("Container received");
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); */


            //Create connect-chat request
            System.out.println("connect-chat request sent");
            Container connectchatrequest = new Container("connect-chat", "chatname=Intruders");
            oos.writeObject(connectchatrequest);
            oos.flush();

            //connect-chat response
            responseObject = ois.readObject();
            System.out.println("Container received");
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer);

            //Create send-message request request
            System.out.println("send message request sent");
            Container sendmessagerequest = new Container("send-message", "hello world");
            oos.writeObject(sendmessagerequest);
            oos.flush();

            // Create send-message response
            responseObject = ois.readObject();
            System.out.println("Container received");
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}