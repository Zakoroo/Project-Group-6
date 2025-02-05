package server;

public class Model {
    private User user;
    private ChatRoom chatRoom;
    private Message message;
    
    public Model(User user, ChatRoom chatRoom, Message message) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.message = message;
    }
}
