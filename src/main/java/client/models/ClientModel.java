package client.models;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.ChatRoom;
import shared.Message;

public class ClientModel {
    private String username;
    private List<ChatRoom> joinedChatRooms;
    private ChatRoom connectedChatRoom;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ChatRoom> getJoinedChatRooms() {
        return joinedChatRooms;
    }

    public void setJoinedChatRooms(List<ChatRoom> joinedChatRooms) {
        this.joinedChatRooms = joinedChatRooms;
    }

    public void addChatRoom(ChatRoom chatroom) {
        this.joinedChatRooms.add(chatroom);
    }

    public void removeChatRoom(String chatname) {
        this.joinedChatRooms.removeIf(r -> r.name().equals(chatname));
    }

    public ChatRoom getConnectedChatRoom() {
        return connectedChatRoom;
    }

    public void setConnectedChatRoom(String chatname) {
        for(ChatRoom chatroom: joinedChatRooms) {
            if(chatroom.name().equals(chatname)) {
                this.connectedChatRoom = chatroom;
            }
        }
    }

    public List<Message> getHistory() {
        return connectedChatRoom.history();
    }

    public void setHistory(List<Message> history) {
        connectedChatRoom.history().addAll(history);
    }

    public void addMessage(Message message) {
        connectedChatRoom.history().add(message);
    }

    public void clearModel() {
        this.username = null;
        this.joinedChatRooms = null;
        this.connectedChatRoom = null;
    }
}
