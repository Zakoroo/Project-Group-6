package client;

import shared.Container;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RequestHandler {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public RequestHandler(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
    }

    public void signup(String nickname, String username, String email, String password) throws IOException, ClassNotFoundException {
        System.out.println("Signup request sent");
        Container signupRequest = new Container("signup", "nickname=" + nickname + "&username=" + username + "&email=" + email + "&password=" + password);
        sendRequest(signupRequest);
        receiveResponse();
    }

    public void signin(String username, String password) throws IOException, ClassNotFoundException {
        System.out.println("Signin request sent");
        Container signinRequest = new Container("signin", "username=" + username + "&password=" + password);
        sendRequest(signinRequest);
        receiveResponse();
    }

    public void deleteUser(String password) throws IOException, ClassNotFoundException {
        System.out.println("Delete user request sent");
        Container deleteUserRequest = new Container("delete-user", "password=" + password);
        sendRequest(deleteUserRequest);
        receiveResponse();
    }

    public void createChat(String chatname) throws IOException, ClassNotFoundException {
        System.out.println("Create chat request sent");
        Container createChatRequest = new Container("create-chat", "chatname=" + chatname);
        sendRequest(createChatRequest);
        receiveResponse();
    }

    public void joinChat(String chatname) throws IOException, ClassNotFoundException {
        System.out.println("Join chat request sent");
        Container joinChatRequest = new Container("join-chat", "chatname=" + chatname);
        sendRequest(joinChatRequest);
        receiveResponse();
    }

    public void connectChat(String chatname) throws IOException, ClassNotFoundException {
        System.out.println("Connect to chat request sent");
        Container connectChatRequest = new Container("connect-chat", "chatname=" + chatname);
        sendRequest(connectChatRequest);
        receiveResponse();
    }

    public void findChat(String toFind) throws IOException, ClassNotFoundException {
        System.out.println("Find chat request sent");
        Container findChatRequest = new Container("find-chat", "tofind=" + toFind);
        sendRequest(findChatRequest);
        receiveResponse();
    }

    public void quitChat(String chatname) throws IOException, ClassNotFoundException {
        System.out.println("Quit chat request sent");
        Container quitChatRequest = new Container("quit-chat", "chatname=" + chatname);
        sendRequest(quitChatRequest);
        receiveResponse();
    }

    public void sendMessage(Object message) throws IOException, ClassNotFoundException {
        System.out.println("Send message request sent");
        Container sendMessageRequest;
        if (message instanceof String) {
            sendMessageRequest = new Container("send-message", message);
        } else if (message instanceof byte[]) {
            sendMessageRequest = new Container("send-message", (byte[]) message);
        } else {
            System.out.println("Invalid message format!");
            return;
        }
        sendRequest(sendMessageRequest);
        receiveResponse();
    }

    public void getHistory(java.sql.Timestamp timestamp) throws IOException, ClassNotFoundException {
        System.out.println("Get chat history request sent");
        Container getHistoryRequest = new Container("get-history", timestamp);
        sendRequest(getHistoryRequest);
        receiveResponse();
    }

    private void sendRequest(Container request) throws IOException {
        oos.writeObject(request);
        oos.flush();
    }

    private void receiveResponse() throws IOException, ClassNotFoundException {
        Object response = ois.readObject();
        if (response instanceof Container) {
            System.out.println("Response received: " + response);
        } else {
            System.out.println("Invalid response format!");
        }
    }
}
