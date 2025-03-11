package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.controllers.ClientReceiver;

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
        if (socket != null && !socket.isClosed()) {
            disconnect();
        }

        socket = new Socket(host, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        if (oos == null || ois == null) {
            System.out.println("Streams are not initialize properly!");
        }

        System.out.println("Connected to server at " + host + ":" + port);
    }

    public boolean connected() {
        if (socket != null) {
            return !socket.isClosed();
        } else {
            return false;
        }
    }

    public ObjectOutputStream getOutputStream() {
        return oos;
    }

    public ObjectInputStream getInputStream() {
        return ois;
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            if (ClientReceiver.getInstance() != null) {
                ClientReceiver.getInstance().stop();
            }

            if (oos != null) {
                oos.close();
            }
            if (ois != null) {
                ois.close();
            }
            socket.close();

            System.out.println("Disconnected from server.");
        }
    }
}
