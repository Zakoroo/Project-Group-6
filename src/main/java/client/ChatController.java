package client;

import java.util.concurrent.Future;

import javafx.application.Platform;
import shared.Container;

public class ChatController {
    private ChatView view;
    private ClientConnection client;
    private String currentChatRoom;

    public ChatController(ChatView view) {
        this.view = view;
        try {
            this.client = new ClientConnection("127.0.0.1", 8005);
        } catch (Exception e) {
            e.printStackTrace();
            view.showAlert("Unable to connect to server.");
        }
    }

    public void setCurrentChatRoom(String chatRoom) {
        this.currentChatRoom = chatRoom;
    }

    public String getCurrentChatRoom() {
        return currentChatRoom;
    }

    public void handleCreateChat(String chatname) {
        Container request = new Container("create-chat", "chatname=" + chatname);
        client.sendRequest(request);
    }

    public void handleLogout() {
        try {
            client.close();
            view.returnToLogin();
        } catch (Exception e) {
            e.printStackTrace();
            view.showAlert("Logout error.");
        }
    }

    public void handleSendMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            view.showAlert("Message cannot be empty.");
            return;
        }

        String chatName = getCurrentChatRoom();
        Container request = new Container("send-message", "chatname=" + chatName + "&message=" + message);

        client.sendRequest(request);
    }

    public void loadChatHistory(String chatroom) {
        Future<Container> futureResponse = client.sendRequestWithResponse(
            new Container("get-history", "chatname=" + chatroom)
        );
    
        new Thread(() -> {
            try {
                Container response = futureResponse.get(); 
                if ("chat-history".equals(response.getCommand())) {
                    String chatHistory = (String) response.getData();
                    Platform.runLater(() -> {
                        for (String msg : chatHistory.split("\n")) {
                            view.addMessage(msg);
                        }
                    });
                } else {
                    Platform.runLater(() -> view.showAlert("Failed to load chat history."));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> view.showAlert("Error loading chat history."));
            }
        }).start();
    }
    

    public void handleEnterChatroom(String chatroom) {
        if (chatroom == null || chatroom.trim().isEmpty()) {
            view.showAlert("Chatroom name cannot be empty.");
            return;
        }

        setCurrentChatRoom(chatroom);

        Future<Container> futureResponse = client.sendRequestWithResponse(
                new Container("enter-chat", "chatname=" + chatroom));

        new Thread(() -> {
            try {
                Container response = futureResponse.get();
                Platform.runLater(() -> {
                    if ("enter-chat-success".equals(response.getCommand())) {
                        System.out.println("Successfully entered chat: " + chatroom);
                        loadChatHistory(chatroom);
                    } else {
                        view.showAlert("Failed to enter chatroom: " + response.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> view.showAlert("Error entering chatroom."));
            }
        }).start();
    }

}
