package shared;

import java.util.List;

public class Profile {
    String username;
    List<ChatRoom> joinedChatRooms;

    public Profile() {
        
    }
    
    public Profile(String username, List<ChatRoom> joinedChatRooms) {
        this.username = username;
        this.joinedChatRooms = joinedChatRooms;
    }

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

    
}
