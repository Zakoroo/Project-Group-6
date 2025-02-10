public class Controller {
    private ChatHandler chatHandler;
    private AccountHandler accountHandler;

    public Controller() {
        Database database = new Database();
        this.chatHandler = new ChatHandler(database);
        this.accountHandler = new AccountHandler(database);
    }

    public void test() {
        System.out.println("Welcome to the Chat Application");
        ChatHandler.createChatRoom(The cool kids);
    }
}