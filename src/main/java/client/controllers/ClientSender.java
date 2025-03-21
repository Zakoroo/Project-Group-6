package client.controllers;

import java.sql.Timestamp;
import client.ClientConnection;
import shared.Container;


public class ClientSender {
    private static ClientSender instance;
    private ClientConnection connection;

    public static void initialize(ClientConnection connection) {
        if (instance == null) {
            instance = new ClientSender(connection);
        } else {
            System.out.println("ClientSender already initialized!");
        }
    }

    public static ClientSender getInstance() {
        if(instance == null) {
            throw new IllegalStateException("ClientSender may have not been initialized!");
        } 
        return instance;
    }

    private ClientSender(ClientConnection connection) {
        this.connection = connection;
    }

    public void signup(String nickname, String username, String email, String password) throws Exception {
        System.out.println("Signup request sent");
        String params = "nickname=" + nickname + "&username=" + username + "&email=" + email + "&password=" + password;
        Container signupRequest = new Container("signup", params);
        sendRequest(signupRequest);
    }

    public void getJoinedChats() throws Exception {
        System.out.println("getjoinedchats request sent");
        Container getjoinedchatsrequeset = new Container("get-joined-chats", null);
        sendRequest(getjoinedchatsrequeset);
    }

    public void signin(String username, String password) throws Exception {
        System.out.println("Signin request sent");
        Container signinRequest = new Container("signin", "username=" + username + "&password=" + password);
        sendRequest(signinRequest);
    }

    public void deleteUser(String password) throws Exception {
        System.out.println("Delete user request sent");
        Container deleteUserRequest = new Container("delete-user", "password=" + password);
        sendRequest(deleteUserRequest);
    }

    public void createChat(String chatname) throws Exception {
        System.out.println("Create chat request sent");
        Container createChatRequest = new Container("create-chat", "chatname=" + chatname);
        sendRequest(createChatRequest);
    }

    public void joinChat(String chatname) throws Exception {
        System.out.println("Join chat request sent");
        Container joinChatRequest = new Container("join-chat", "chatname=" + chatname);
        sendRequest(joinChatRequest);
    }

    public void connectChat(String chatname) throws Exception {
        System.out.println("Connect to chat request sent");
        Container connectChatRequest = new Container("connect-chat", "chatname=" + chatname);
        sendRequest(connectChatRequest);
    }

    public void findChat(String toFind) throws Exception {
        System.out.println("Find chat request sent");
        Container findChatRequest = new Container("find-chat", "tofind=" + toFind);
        sendRequest(findChatRequest);
    }

    public void quitChat(String chatname) throws Exception {
        System.out.println("Quit chat request sent");
        Container quitChatRequest = new Container("quit-chat", "chatname=" + chatname);
        sendRequest(quitChatRequest);
    }

    public void sendMessage(Object message) throws Exception {
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
    }

    public void getHistory() throws Exception{
        this.getHistory(new Timestamp(0));
    }

    public void getHistory(Timestamp timestamp) throws Exception {
        System.out.println("Get chat history request sent");
        Container getHistoryRequest = new Container("get-history", timestamp);
        sendRequest(getHistoryRequest);
    }

    private void sendRequest(Container request) throws Exception {
        connection.getOutputStream().writeObject(request);
        connection.getOutputStream().flush();
    }
}