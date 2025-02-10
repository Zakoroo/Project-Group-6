public class Controller {
    private ChatHandler chatHandler;
    private AccountHandler accountHandler;

    public Controller() {
        Database database = new Database();
        this.chatHandler = new ChatHandler(database);
        this.accountHandler = new AccountHandler(database);
    }

    public void start() {
        System.out.println("Welcome to the Chat Application");
    }
}