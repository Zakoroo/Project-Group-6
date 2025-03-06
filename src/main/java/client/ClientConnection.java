package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private static ClientConnection instance;
    
    public static ClientConnection getInstance() {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }
    
    private ClientConnection() {
    }
    
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        if (oos == null || ois == null) {
            System.out.println("Streams are not initialize properly!");
        }
        System.out.println("Connected to server at " + host + ":" + port);
    }

    public ObjectOutputStream getOutputStream() {
        return oos;
    }

    public ObjectInputStream getInputStream() {
        return ois;
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Disconnected from server.");
        }
    }
}
