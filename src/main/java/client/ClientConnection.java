package client;

import shared.Container;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private volatile boolean running = true;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    // Map to track pending requests by correlation ID
    private final ConcurrentHashMap<String, CompletableFuture<Container>> pendingRequests = new ConcurrentHashMap<>();

    public ClientConnection(String serverAddress, int port) throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.oos.flush();
        this.ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected to the server at " + serverAddress + ":" + port);
        startReceiver();
    }

    /**
     * Sends a Container request to the server and returns a Future for the
     * response.
     * This method assigns a unique correlation ID to the request.
     */
    public Future<Container> sendRequest(Container request) {
        // Generate a unique ID and assign it to the request
        String correlationId = UUID.randomUUID().toString();
        request.setId(correlationId);

        // Create a CompletableFuture to hold the response
        CompletableFuture<Container> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        // Submit the sending task asynchronously
        threadPool.submit(() -> {
            try {
                oos.writeObject(request);
                oos.flush();
                System.out.println("Sent: " + request);
            } catch (IOException e) {
                System.err.println("Failed to send request: " + e.getMessage());
                pendingRequests.remove(correlationId);
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    /**
     * Continuously receives responses from the server.
     * When a response is received, its correlation ID is used to complete the
     * matching future.
     */
    private void startReceiver() {
        Thread receiver = new Thread(() -> {
            try {
                while (running) {
                    Container response = (Container) ois.readObject();
                    System.out.println("Received: " + response);
                    String id = response.getId();
                    if (id != null) {
                        CompletableFuture<Container> future = pendingRequests.remove(id);
                        if (future != null) {
                            future.complete(response);
                        } else {
                            System.out.println("No pending request for id: " + id);
                        }
                    } else {
                        // Handle asynchronous notifications if necessary
                        System.out.println("Async notification: " + response);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if (running) {
                    System.err.println("Receiver thread error: " + e.getMessage());
                }
            }
        });
        receiver.setDaemon(true);
        receiver.start();
    }

    public void close() {
        if (!running)
            return;
        running = false;
        threadPool.shutdown();
        try {
            if (oos != null)
                oos.close();
            if (ois != null)
                ois.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        System.out.println("Connection closed.");
    }
}
