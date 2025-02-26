package client;

import shared.Container;

public class ChatController {
    private ChatView view;
    private Client client;

    public ChatController(ChatView view) {
        this.view = view;
        try {
            this.client = new Client("127.0.0.1", 8005);
        } catch (Exception e) {
            e.printStackTrace();
            view.showAlert("Unable to connect to server.");
        }
    }

    public void handleCreateChat(String chatname) {
     Container request = new Container("create-chat", "chatname=");
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
}
