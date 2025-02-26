package client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import shared.Container;

public class Client {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    boolean running = true;

    private ExecutorService senderPool = Executors.newFixedThreadPool(10);

    public Client(String serverAddress, int port) throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected to the server at " + serverAddress + ":" + port);

        startReciever();
    }

    private void startReciever() {
        Thread receiver = new Thread(() -> {
            try {
                while (running) {
                    Container response = (Container) ois.readObject();
                    System.out.println("Received: " + response);
                    handleIncommingMessage(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        receiver.setDaemon(true);
        receiver.start();
    }

    private void handleIncommingMessage(Container response) {
        javafx.application.Platform.runLater(() -> {
            // Update the UI
            System.out.println("Received: " + response);
        });
    }

    public void sendRequest(Container request) {
        senderPool.submit(() -> {
            try {
                oos.writeObject(request);
                oos.flush();
                System.out.println("Sent: " + request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() throws IOException {
        running = false;
        senderPool.shutdown();
        oos.close();
        ois.close();
        socket.close();
        System.out.println("Connection closed.");
    }
}
