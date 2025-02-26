package client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import shared.Container;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean running = true;
    private final ExecutorService senderPool = Executors.newFixedThreadPool(10);

    public ClientConnection(String serverAddress, int port) throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.oos.flush();
        this.ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected to the server at " + serverAddress + ":" + port);

        startReceiver();
    }

    private void startReceiver() {
        Thread receiver = new Thread(() -> {
            try {
                while (running) {
                    Container response = (Container) ois.readObject();
                    System.out.println("Received: " + response);
                    handleIncomingMessage(response);
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Connection lost. Trying to reconnect...");
                    try {
                        Thread.sleep(2000);
                        reconnect();
                    } catch (InterruptedException | IOException ex) {
                        System.err.println("Failed to reconnect: " + ex.getMessage());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        receiver.setDaemon(true);
        receiver.start();
    }

    private void reconnect() throws IOException {
        close();
        System.out.println("Reconnecting...");
        this.socket = new Socket("127.0.0.1", 8005);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
        running = true;
        startReceiver();
        System.out.println("Reconnected successfully!");
    }

    private void handleIncomingMessage(Container response) {
        javafx.application.Platform.runLater(() -> {
            switch (response.getCommand()) {
                case "error":
                    System.out.println("Error: " + response.getData());
                    break;
                case "success":
                    System.out.println("Success: " + response.getData());
                    break;
                case "message":
                    System.out.println(" New Message: " + response.getData());
                    break;
                default:
                    System.out.println("Unknown Response: " + response);
                    break;
            }
        });
    }

    public void sendRequest(Container request) {
        senderPool.submit(() -> {
            try {
                oos.writeObject(request);
                oos.flush();
                System.out.println("Sent: " + request);
            } catch (IOException e) {
                System.err.println("Failed to send request: " + e.getMessage());
            }
        });
    }

    public Future<Container> sendRequestWithResponse(Container request) {
        return senderPool.submit(() -> {
            try {
                oos.writeObject(request);
                oos.flush();
                System.out.println("Sent: " + request);
                return (Container) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to send request: " + e.getMessage());
                return new Container("error", "Failed to send request: " + e.getMessage());
            }
        });
    }

    public void close() {
        if (!running) return;
        running = false;
        senderPool.shutdown();
        try {
            if (oos != null) oos.close();
            if (ois != null) ois.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        System.out.println("Connection closed.");
    }
}
