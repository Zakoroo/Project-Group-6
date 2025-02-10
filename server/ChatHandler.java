public class ChatHandler {
    private Database database;

    public ChatHandler(Database database) {
        this.database = database;
    }

    public void sendMessage(String chatRoom, String message) {
        String sql = "INSERT INTO messages (chatRoom, message) VALUES ('" + chatRoom + "', '" + message + "');";
        database.query(sql);
        System.out.println("Message sent to " + chatRoom);
    }

    public void fetchChatLog(String chatRoom) {
        String sql = "SELECT * FROM messages WHERE chatRoom = '" + chatRoom + "';";
        database.query(sql);
        System.out.println("Chat log fetched for " + chatRoom);
    }

    public void createChatRoom(String chatRoomName) {
        String sql = "INSERT INTO chatrooms (name) VALUES ('" + chatRoomName + "');";
        database.query(sql);
        System.out.println("Chat room " + chatRoomName + " created.");
    }

    public void joinChatRoom(String chatRoomName) {
        System.out.println("Joined chat room: " + chatRoomName);
    }

    public void leaveChatRoom(String chatRoomName) {
        System.out.println("Left chat room: " + chatRoomName);
    }
}