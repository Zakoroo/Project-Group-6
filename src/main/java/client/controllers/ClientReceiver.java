package client.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import shared.ChatRoom;
import shared.Container;
import shared.Message;
import client.models.*;

public class ClientReceiver implements Runnable {
    private ObjectInputStream ois;
    private ClientModel model;
    private SignInController signinController;
    private SignUpController signupController;
        

    public ClientReceiver(ObjectInputStream ois) {
        this.ois = ois;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object object = ois.readObject();
                if (object instanceof Container) {
                    Container response = (Container) object;
                    updateModel(response);

                } else {
                    System.out.println("Object is not of type container!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateModel(Container container) {
        String command = container.getCommand();
        Object data = container.getData();
        switch (command) {
            case "signin-success":
                handleSignin(data);
            case "signup-success":
                handleSignup(data);
            case "delete-user-success":
                handleDeleteUser(data);
            case "create-chat-success":
                handleCreateChat(data);
            case "join-chat-success":
                handleJoinChat(data);
            case "connect-chat-success":
                handleConnectChat(data);
            case "find-chat-success":
                handleFindChat(data);
            case "quit-chat-success":
                handleQuitChat(data);
            case "send-message-success":
                handleSendMessage(data);
            case "send-message":
                handleReceiveMessage(data);
            case "get-history-success":
                handleGetHistory(data);
            default:
                System.out.println("Received unknown command: " + command);
        }
    }

    // updates the model upon retreiving the history form the server
    // update the main view
    private void handleGetHistory(Object data) {
        if (data instanceof List) {
            List<Message> history = (List<Message>) data;
            if (model.getHistory() == null) {
                model.setHistory(history);
            } else {
                history.forEach(m -> model.addMessage(m));
            }

            // TODO: update the main view here
        }
    }

    // confirms that the server has successfully received the message form the user
    // no view should be updated here
    private void handleSendMessage(Object data) {
        if (data instanceof String) {
            String string = (String) data;
            System.out.println(string);
        }
    }

    // updates the history to the currently connected chat in the model with a
    // message
    // update the main view
    private void handleReceiveMessage(Object data) {
        if (data instanceof Message) {
            Message message = (Message) data;
            model.addMessage(message);
            
            // TODO: render the changes in the main view
        }

    }

    // updates the list of joined chats
    // update the main view
    private void handleQuitChat(Object data) {
        if (data instanceof String) {
            String chatname = (String) data;
            model.removeChatRoom(chatname); 
            
            // TODO: render the changes in the main view
        }
    }

    // update the search for chatrooms view
    private void handleFindChat(Object data) {
        if (data instanceof List) {
            ArrayList<ChatRoom> searchResult = (ArrayList<ChatRoom>) data;

            // TODO: update the search view with the searchResult
        }
    }

    // update the model by setting the connected chatroom
    // updat the main view
    private void handleConnectChat(Object data) {
        if (data instanceof String) {
            String chatname = (String) data;
            model.setConnectedChatRoom(chatname);

            // TODO: update the main view
        }
    }

    // add a chatroom to the list of joined chatrooms
    // update the main view
    private void handleJoinChat(Object data) {
        if (data instanceof ChatRoom) {
            ChatRoom chatroom = (ChatRoom) data;
            model.addChatRoom(chatroom);

            // TODO: update the main view
        }
    }

    // prints out the success message for debugging purposes
    private void handleCreateChat(Object data) {
        if (data instanceof String) {
            String string = (String) data;
            System.out.println(string);

            // TODO: close the prompt and send a connect request
        }
    }

    // upon deleting the currect account the client model must also be cleared
    // switch to sign in view and indicate that the user got deleted
    private void handleDeleteUser(Object data) {
        if (data instanceof String) {
            String string = (String) data;
            model.clearModel();
            System.out.println(string);

            // TODO: if not implemented then ignore
        }
    }

    // upon success switch to the signin view with indicating that the account was successfully created
    private void handleSignup(Object data) {
        if (data instanceof String) {
            String string = (String) data;
            System.out.println(string);

            // TODO: switch to sign in with
            try {
                signupController.switchToSignIn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // updates the model with the new username
    // switch to the main view
    private void handleSignin(Object data) {
        if (data instanceof String) {
            String username = (String) data;

            // TODO: switch to the main view
            try {
                signinController.switchToMainView(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}