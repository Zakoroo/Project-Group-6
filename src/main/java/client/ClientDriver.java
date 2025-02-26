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

            /* // Request signup
            System.out.println("Signup request sent");
            Container singupRequest1 = new Container("signup", "nickname=zein&username=alzeine&email=mohamad@alzeine.com&password=87654321");
            oos.writeObject(singupRequest1);
            oos.flush();  

            // Signup response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer);

            // Request signup
            System.out.println("Signup request sent");
            Container singupRequest = new Container("signup", "nickname=abdo&username=abood&email=abdo@gmail.com&password=123456");
            oos.writeObject(singupRequest);
            oos.flush();   
            
            // Signup response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); */
    
            //request signin
            System.out.println("Signin request sent");
            Container singinRequest = new Container("signin", "username=alzeine&password=87654321");
            oos.writeObject(singinRequest);
            oos.flush();

            // Signin response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); 


            /* 
            // Request user delete
            System.out.println("Delete user request sent");
            Container deleteUserRequest = new Container("delete-user", "username=Hussein&password=123456");
            oos.writeObject(deleteUserRequest);
            oos.flush(); 

            // Delete user response
            System.out.println("Container received");
            responseObject = ois.readObject();
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer);
*/
       /*      // Create chatroom request
            System.out.println("Create chatroom request sent");
            Container createChatRequest = new Container("create-chat", "chatname=Intruders");
            oos.writeObject(createChatRequest);
            oos.flush(); 

            //Create chatroom response
            responseObject = ois.readObject();
            System.out.println("Container received");
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); */

         /*    // Create Join-chat request request
            System.out.println("join chat request sent");
            Container joinchatrequest = new Container("join-chat", "username=alzeine&chatname=Intruders");
            oos.writeObject(joinchatrequest);
            oos.flush();

            // Join-chat response
            responseObject = ois.readObject();
            System.out.println("Container received");
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); */
 
             // Create quit-chat request request
            System.out.println("quit chat request sent");
            Container quitchatrequest = new Container("quit-chat", "username=alzeine&chatname=Intruders");
            oos.writeObject(quitchatrequest);
            oos.flush();
            
            // Create quit-chat response
            responseObject = ois.readObject();
            System.out.println("Container received");
            responseContainer = (Container) responseObject;
            System.out.println(responseContainer); 
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}