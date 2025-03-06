package client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import shared.ChatRoom;
import shared.Container;
import shared.Message;
import client.SceneManager;
import client.models.*;

public class ClientReceiver implements Runnable {
    private static ClientReceiver instance;
    private ObjectInputStream ois;
    private ClientModel clientModel;
    private SearchModel searchModel;
    private SceneManager sceneManager;

    public static void initialize(ObjectInputStream ois) {
        if (instance == null) {
            instance = new ClientReceiver(ois);
        } else {
            System.out.println("ClientReceiver already initialized!");
        }
    }

    public static ClientReceiver getInstance() {
        if(instance == null) {
            throw new IllegalStateException("ClientReceiver may have not been initialized!");
        } 
        return instance;
    }

    private ClientReceiver(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    public void setSearchModel(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {

            
        });
        try {
            while (true) {
                if (ois == null) {
                    System.out.println("Input stream is null!");
                }
                Object object = ois.readObject();
                if (object instanceof Container) {
                    Container response = (Container) object;
                    Platform.runLater(() -> updateModel(response));

                } else {
                    System.out.println("Object is not of type container!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateModel(Container container) {
        System.out.println("Container received");
        String command = container.getCommand();
        Object data = container.getData();
        switch (command) {
            case "signin-success":
                handleSignin(data);
                break;
            case "signup-success":
                handleSignup(data);
                break;
            case "delete-user-success":
                handleDeleteUser(data);
                break;
            case "create-chat-success":
                handleCreateChat(data);
                break;
            case "join-chat-success":
                handleJoinChat(data);
                break;
            case "connect-chat-success":
                handleConnectChat(data);
                break;
            case "find-chat-success":
                handleFindChat(data);
                break;
            case "quit-chat-success":
                handleQuitChat(data);
                break;
            case "send-message-success":
                handleSendMessage(data);
                break;
            case "send-message":
                handleReceiveMessage(data);
                break;
            case "get-history-success":
                handleGetHistory(data);
                break;
            case "get-joined-chats-success":
                handGetJoinedChats(data);
                break;
            default:
                // print mysterious bugs
                System.out.println(command + ": " + ((String) data));
        }
    }

    // updates the clientModel upon retreiving the history form the server
    // update the main view
    private void handleGetHistory(Object data) {
        if (data instanceof List) {
            List<Message> history = (List) data;
            clientModel.setHistory(FXCollections.observableArrayList(history));
            history.forEach(message -> System.out.println(message));
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

    // updates the history to the currently connected chat in the clientModel with a
    // message
    // update the main view
    private void handleReceiveMessage(Object data) {
        if (data instanceof Message) {
            Message message = (Message) data;
            
            try {
                clientModel.addMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // updates the list of joined chats
    // update the main view
    private void handleQuitChat(Object data) {
        if (data instanceof String) {
            String chatname = (String) data;
            clientModel.removeChatRoom(chatname);

            // TODO: render the changes in the main view
        }
    }

    // update the search for chatrooms view
    private void handleFindChat(Object data) {
        if (data instanceof List) {
            List<ChatRoom> searchResult = (List) data;

            try {
                searchModel.setResultSet(FXCollections.observableArrayList(searchResult));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // update the clientModel by setting the connected chatroom
    // updat the main view
    private void handleConnectChat(Object data) {
        if (data instanceof String) {
            String chatname = (String) data;

            try {
                clientModel.setConnectedChatRoom(chatname);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // add a chatroom to the list of joined chatrooms
    // update the main view
    private void handleJoinChat(Object data) {
        if (data instanceof ChatRoom) {
            ChatRoom chatroom = (ChatRoom) data;
            clientModel.addChatRoom(chatroom);

            // TODO: update the main view

        }
    }

    private void handGetJoinedChats(Object data) {
        if (data instanceof List) {
            List<ChatRoom> chatRooms = (List) data;

            try {
                clientModel.setJoinedChatRooms(FXCollections.observableArrayList(chatRooms));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // prints out the success message for debugging purposes
    private void handleCreateChat(Object data) {
        if (data instanceof String) {
            ChatRoom chatRoom = (ChatRoom) data;

            try {
                clientModel.addChatRoom(chatRoom);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // upon deleting the currect account the client clientModel must also be cleared
    // switch to sign in view and indicate that the user got deleted
    private void handleDeleteUser(Object data) {
        if (data instanceof String) {
            String string = (String) data;
            clientModel.clearModel();
            System.out.println(string);

            // TODO: if not implemented then ignore
        }
    }

    // upon success switch to the signin view with indicating that the account was
    // successfully created
    private void handleSignup(Object data) {
        if (data instanceof String) {
            String string = (String) data;
            System.out.println(string);

            try {
                sceneManager.switchScene("/fxml/signinView.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // updates the clientModel with the new username
    // switch to the main view
    private void handleSignin(Object data) {
        if (data instanceof String) {
            String username = (String) data;

            try {
                clientModel.setUsername(username);
                sceneManager.switchScene("/fxml/mainView.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}