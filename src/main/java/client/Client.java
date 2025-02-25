package client;

import java.io.*;
import java.net.Socket;
import shared.Container;

public class Client {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public Client(String serverAddress, int port) throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected to the server at " + serverAddress + ":" + port);
    }

    public Container sendRequest(Container request) {
        try {
            oos.writeObject(request);
            oos.flush();
            return (Container) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Container("error", "Failed to communicate with the server.");
        }
    }

    public void close() throws IOException {
        oos.close();
        ois.close();
        socket.close();
        System.out.println("Connection closed.");
    }
}
