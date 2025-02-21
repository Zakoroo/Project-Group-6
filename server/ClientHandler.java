import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.zaxxer.hikari.*;
import java.sql.*;
import shared.Container;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private AccountHandler accountHandler;
    private ChatHandler chatHandler;
    private ChatRoom chatroom;
    private String username;

    public ClientHandler(Socket clientSocket, HikariDataSource dataSource) {
        this.clientSocket = clientSocket;
        this.accountHandler = new AccountHandler(dataSource);
        this.chatHandler = new ChatHandler(dataSource);
    }

    @Override
    public void run() {
        try (
                // Set up input and output stream for communication
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            ) {


            Object object;
            Container container;

            while ((object = ois.readObject()) != null) {
                if (object instanceof Container) {
                    container = (Container) object;
                    oos.writeObject(executeCommand(container));
                    oos.flush();
                } else {
                    Container errorResponse = new Container("error", "The object is not of type container.");
                    oos.writeObject(errorResponse);
                    oos.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("A client disconnected or an error occurred.");
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Container executeCommand(Container container) {
        String command = container.getCommand();
        Object data = container.getData();
        switch (command) {
            case "signin":
                return handleSignin(data);
            case "signup":
                return handleSignup(data);
            case "delete-user":
                return handleDeleteUser(data);
            case "create-chat":
                return handleCreateChat(data);
            case "join-chat":
                return handleJoinChat(data);
            case "find-chat":
                return handleFindChat(data);
            case "QuitChat":
                return handleQuitChat(data);
            case "SendMessage":
                return handleSendMessage(data);
            case "GetHistory":
                return handleGetHistory(data);
            default:
                return new Container("error", "Command nonexistent!");
        }
    }

    // Handle user information
    private Container handleSignup(Object data) {
        Map<String, String> params;
        boolean success = false;

        try {
            if (data instanceof String) {
                System.out.println("Received data: " + data);
                params = parseData((String) data);
                if (params.keySet().isEmpty()) {
                    System.out.println("Parsed params are null");
                    return new Container("error", "Invalid data!");
                }
                success = accountHandler.signup(params.get("nickname"), params.get("username"), params.get("email"),
                        params.get("password"));
                if (success) {
                    username = params.get("username");
                    return new Container("signup-success", "user: " + username);
                } else {
                    return new Container("error", "something went wrong");
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }

        return new Container("error", "Invalid data!");
    }

    private Container handleSignin(Object data) {
        Map<String, String> params;
        boolean success = false;

        try {
            if (data instanceof String) {
                params = parseData((String) data);
                if (params.keySet().isEmpty()) {
                    System.out.println("Parsed params are null");
                    return new Container("error", "Invalid data!");
                }

                success = accountHandler.signin(params.get("username"), params.get("password"));
                if (success) {
                    username = params.get("username");
                    return new Container("signin-success", "You are now signed in as user: " + username);
                } else {
                    return new Container("error", "Invalid credentials!");
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }

        return new Container("error", "Invalid data!");
    }

    private Container handleDeleteUser(Object data) {
        Map<String, String> params;
        boolean success = false;
        String tmpUser = username;

        try {
            if (username == null) {
                return new Container("error", "User not logged in!");
            } else if (data instanceof String) {
                // Extract the parameters from the data object
                params = parseData((String) data);

                // Check if the parameters are empty
                if (params.keySet().isEmpty()) {
                    System.out.println("Parsed params are null");
                    return new Container("error", "Invalid data!");
                }

                // Retreive the result of the query pass/fail
                success = accountHandler.deleteUser(username, params.get("password"));
                if (success) {
                    // NOTE: Temporarly it is enough just to set the values to null
                    username = null;
                    chatroom = null;
                    // TODO: Make sure to unsubsribe from the current channel you are listening to

                    return new Container("delete-user-success", "User deleted: " + tmpUser);
                } else {
                    return new Container("error", "Invalid credentials!");
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }

        return new Container("error", "Invalid data!");
    }

    // Handle chatroom manipulation
    private Container handleCreateChat(Object data) {
        Map<String, String> params;
        boolean success = false;

        try {
            if (username == null) { // Check if user is signed in
                return new Container("error", "User not logged in!");
            } else if (data instanceof String) { // Check if data is of correct type
                System.out.println("Received data: " + data);
                params = parseData((String) data);
                if (params.keySet().isEmpty()) { // Check that the user entered correct parameters
                    System.out.println("Parsed params are null");
                    return new Container("error", "Invalid data!");
                }
                success = chatHandler.createChat(params.get("chatname"), username);
                if (success) { 
                    return new Container("create-chat-success",
                            "Chat created successfully with name: " + params.get("chatname"));
                } else {
                    return new Container("error", "Chatroom already exist");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }

        return new Container("error", "Invalid data!");
    }

    private Container handleFindChat(Object data) {
        // TODO: implement this method
        return null;

    }

    private Container handleJoinChat(Object data) {
        // TODO: implement this method
        return null;

    }

    private Container handleQuitChat(Object data) {
        // TODO: implement this method
        return null;

    }

    // Handle message sending and receiving
    private Container handleSendMessage(Object data) {
        // TODO: implement this method
        return null;

    }

    private Container handleGetHistory(Object data) {
        // TODO: implement this method
        return null;

    }

    private Map<String, String> parseData(String s) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (s == null || s.isEmpty()) {
            return params;
        }
        String[] pairs = s.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            String key = keyValue[0];
            String value = keyValue[1];
            params.put(key, value);
        }
        return params;
    }

}