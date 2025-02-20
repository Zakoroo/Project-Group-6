import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.sql.*;

public class ClientHandler implements Runnable {
    private AccountHandler accountHandler;
    private ChatHandler chatHandler;
    private Socket clientSocket;
    private ChatRoom chatroom;
    private String username;

    public ClientHandler(AccountHandler accountHandler, ChatHandler chatHandler, Socket clientSocket) {
        this.accountHandler = accountHandler;
        this.chatHandler = chatHandler;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                // Set up input and output stream for communication
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ) {
            Object object;
            Container container;

            while ((object = ois.readObject()) != null) {
                if (object instanceof Container) {
                    container = (Container) object;
                    oos.writeObject(executeCommand(container));
                    oos.flush();
                } else {
                    oos.writeObject(object);
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
                return handleLogin(data);
            default:
                return new Container("error", "Command nonexistent!");
        }
    }

    private Container handleLogin(Object data) {
        Map<String, String> params;
        boolean success = false;
        
        try {
            if (data instanceof String) {
                params = parseData((String) data);  
                success = accountHandler.signin(params.get("username"), params.get("password"));
                if(success) {
                    username = params.get("username");
                    return new Container("signin-success", "You are now signed in as user: " + username);
                } else {
                    return new Container("error", "Invalid credentials!");
                }
            }
        } catch (IOException | SQLException e) {
           e.printStackTrace();
        }
        
        return new Container("error", "Invalid data!");
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


class Container implements Serializable {
    private static final long serialVersionUID = 404;
    
    private String command;
    private Object data; 

    public Container(String command, Object data) {
        this.command = command;
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public Object getData() {
        return data;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Container{" +
                "command='" + command + '\'' +
                ", data=" + data +
                '}';
    }
}
