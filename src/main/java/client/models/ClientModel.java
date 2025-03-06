package client.models;


import java.util.List;
import javafx.beans.property.StringProperty;
import shared.ChatRoom;
import shared.Message;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientModel {
    private static ClientModel instance;
    private StringProperty username = new SimpleStringProperty("");
    private ObservableList<ChatRoom> joinedChatRooms = FXCollections.observableArrayList();
    private ObservableList<Message> history = FXCollections.observableArrayList();
    private ChatRoom connectedChatRoom;

    public static ClientModel getInstance() {
        if (instance == null) {
            instance = new ClientModel();
        }
        return instance;
    }

    private ClientModel() {
        this.joinedChatRooms = FXCollections.observableArrayList();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public ObservableList<ChatRoom> getJoinedChatRooms() {
        return joinedChatRooms;
    }

    public void setJoinedChatRooms(ObservableList<ChatRoom> joinedChatRooms) {
        this.joinedChatRooms.setAll(joinedChatRooms);
    }

    public void addChatRoom(ChatRoom chatroom) {
        this.joinedChatRooms.add(chatroom);
    }

    public void addChatRooms(List<ChatRoom> chatrooms) {
        this.joinedChatRooms.addAll(chatrooms);
    }

    public void removeChatRoom(String chatname) {
        this.joinedChatRooms.removeIf(r -> r.name().equals(chatname));
    }

    public ChatRoom getConnectedChatRoom() {
        return connectedChatRoom;
    }

    public void setConnectedChatRoom(String chatname) {
        for (ChatRoom chatroom : joinedChatRooms) {
            if (chatroom.name().equals(chatname)) {
                this.connectedChatRoom = chatroom;
            }
        }
    }

    public ObservableList<Message> getHistory() {
        return history;
    }

    public void setHistory(List<Message> history) {
        if (connectedChatRoom != null) {
            this.history.setAll(history);
        }
    }

    public void addMessage(Message message) {
        if (connectedChatRoom != null) {
            this.history.add(message);
        }
    }

    public void clearModel() {
        this.username.set("");
        this.joinedChatRooms.clear();
        this.connectedChatRoom = null;
    }
}
