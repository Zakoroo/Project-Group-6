package server;

import java.io.*;
import java.net.*;
import java.util.*;
import com.zaxxer.hikari.*;
import java.sql.*;
import shared.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private AccountHandler accountHandler;
    private ChatHandler chatHandler;
    private NotificationHandler notificationHandler;
    private ChatRoom chatroom;
    private String username;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientHandler(Socket clientSocket, HikariDataSource dataSource, NotificationHandler notificationHandler) {
        this.clientSocket = clientSocket;
        this.accountHandler = new AccountHandler(dataSource);
        this.chatHandler = new ChatHandler(dataSource);
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void run() {
        try {
            // Do not change order: it will cause deadlock
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
            Object object;
            Container container;

            while ((object = ois.readObject()) != null) {
                if (object instanceof Container) {
                    container = (Container) object;
                    oos.writeObject(executeCommand(container));
                    oos.flush();
                } else {
                    oos.writeObject(new Container("error", "The object is not of type container."));
                    oos.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected: " + clientSocket.getRemoteSocketAddress());
            // Remove user from notification service on disconnect
            if (username != null && chatroom != null) {
                notificationHandler.removeListener(chatroom.name(), oos);
            }
        } finally {
            try {
                if (oos != null)
                    oos.close();
                if (ois != null)
                    ois.close();
                if (clientSocket != null)
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
            case "connect-chat":
                return handleConnectChat(data);
            case "find-chat":
                return handleFindChat(data);
            case "quit-chat":
                return handleQuitChat(data);
            case "send-message":
                return handleSendMessage(data);
            case "get-history":
                return handleGetHistory(data);
            default:
                System.out.println("Received unknown command: " + command);
                return new Container("error", "Unknown command: " + command);
        }
    }

    // User Account Management
    private Container handleSignup(Object data) {
        if (!(data instanceof String) || data == null) {
            return new Container("error", "Invalid data!");
        }
        try {
            String dataStr = (String) data;
            System.out.println("Received data: " + dataStr);
            Map<String, String> params = parseData(dataStr);
            if (params.isEmpty()) {
                System.out.println("Parsed params are empty");
                return new Container("error", "Invalid data!");
            }
            boolean success = accountHandler.signup(
                    params.get("nickname"),
                    params.get("username"),
                    params.get("email"),
                    params.get("password"));
            if (success) {
                username = params.get("username");
                return new Container("signup-success", "user: " + username);
            } else {
                return new Container("error", "Something went wrong during signup");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }
    }

    private Container handleSignin(Object data) {
        if (!(data instanceof String) || data == null) {
            return new Container("error", "Invalid data!");
        }
        try {
            Map<String, String> params = parseData((String) data);
            if (params.isEmpty()) {
                System.out.println("Parsed params are empty");
                return new Container("error", "Invalid data!");
            }
            boolean success = accountHandler.signin(params.get("username"), params.get("password"));
            if (success) {
                username = params.get("username");
                return new Container("signin-success", "You are now signed in as user: " + username);
            } else {
                return new Container("error", "Invalid credentials!");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }
    }

    private Container handleDeleteUser(Object data) {
        if (username == null) {
            return new Container("error", "User not logged in!");
        }
        if (!(data instanceof String) || data == null) {
            return new Container("error", "Invalid data!");
        }
        try {
            Map<String, String> params = parseData((String) data);
            if (params.isEmpty()) {
                System.out.println("Parsed params are empty");
                return new Container("error", "Invalid data!");
            }
            boolean success = accountHandler.deleteUser(username, params.get("password"));
            if (success) {
                if (chatroom != null) {
                    notificationHandler.removeListener(chatroom.name(), oos);
                }
                String tmpUser = username;
                username = null;
                chatroom = null;
                return new Container("delete-user-success", "User deleted: " + tmpUser);
            } else {
                return new Container("error", "Invalid credentials!");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }
    }

    // Chatroom Management
    private Container handleCreateChat(Object data) {
        if (username == null) {
            return new Container("error", "User not logged in!");
        }
        if (!(data instanceof String) || data == null) {
            return new Container("error", "Invalid data!");
        }
        try {
            Map<String, String> params = parseData((String) data);
            if (params.isEmpty()) {
                System.out.println("Parsed params are empty");
                return new Container("error", "Invalid data!");
            }
            boolean success = chatHandler.createChat(params.get("chatname"), username);
            if (success) {
                return new Container("create-chat-success",
                        "Chat created successfully with name: " + params.get("chatname"));
            } else {
                return new Container("error", "Chatroom already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }
    }

    private Container handleJoinChat(Object data) {
        if (username == null) {
            return new Container("error", "User not logged in!");
        }
        if (!(data instanceof String) || data == null) {
            return new Container("error", "Invalid data!");
        }
        try {
            Map<String, String> params = parseData((String) data);
            if (params.isEmpty()) {
                System.out.println("Parsed params are empty");
                return new Container("error", "Invalid data!");
            }
            boolean success = chatHandler.joinChat(username, params.get("chatname"));
            if (success) {
                return new Container("join-chat-success",
                        "User " + username + " joined chat: " + params.get("chatname"));
            } else {
                return new Container("error", "Failed to join chat: " + params.get("chatname"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }
    }

    private Container handleConnectChat(Object data) {
        if (username == null) {
            return new Container("error", "User not logged in!");
        }
        if (!(data instanceof String) || data == null) {
            return new Container("error", "Invalid data!");
        }
        try {
            Map<String, String> params = parseData((String) data);
            if (params.isEmpty()) {
                System.out.println("Parsed params are empty");
                return new Container("error", "Invalid data!");
            }
            notificationHandler.addListener(params.get("chatname"), oos);
            return new Container("connect-chat-success",
                    "User " + username + " joined chat: " + params.get("chatname"));
        } catch (IOException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }
    }

    private Container handleFindChat(Object data) {
        // Not implemented yet.
        return new Container("error", "Feature not implemented yet: find-chat");
    }

    private Container handleQuitChat(Object data) {
        Map<String, String> params;
    
        try {
            if (username == null) {
                return new Container("error", "User not logged in!");
            } else if (data instanceof String) {
                params = parseData((String) data);
                if (params.keySet().isEmpty()) {
                    System.out.println("Parsed params are null");
                    return new Container("error", "Invalid data!");
                }
    
                boolean quit = chatHandler.quitChat(params.get("username"), params.get("chatname"));
                if (quit) {
                    return new Container("quit-chat-success", "User " + params.get("username") + " quit chat: " + params.get("chatname"));
                } else {
                    return new Container("error", "Failed to quit chat: " + params.get("chatname"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Container("error", "An error occurred: " + e.getMessage());
        }
    
        return new Container("error", "Invalid data!");
    }

    // Message Handling
    private Container handleSendMessage(Object data) {
        // Not implemented yet.
        return new Container("error", "Feature not implemented yet: send-message");
    }

    private Container handleGetHistory(Object data) {
        // Not implemented yet.
        return new Container("error", "Feature not implemented yet: get-history");
    }

    private Map<String, String> parseData(String s) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (s == null || s.isEmpty()) {
            return params;
        }
        String[] pairs = s.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length < 2) {
                continue; // Skip malformed pair
            }
            params.put(keyValue[0], keyValue[1]);
        }
        return params;
    }
}
