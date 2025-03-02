package client;

import shared.Container;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.*;

public class RequestHandler {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public RequestHandler(ObjectOutputStream oos, ObjectInputStream ois) {
        this.oos = oos;
        this.ois = ois;
    }

    /**
     * Sends a Container request to the server and returns a Future of the Container
     * response.
     */
    public Future<Container> sendRequestWithResponse(Container request) {
        return executor.submit(() -> {
            if (request == null) {
                return new Container("error", "Null request provided");
            }
            try {
                // Send the request
                oos.writeObject(request);
                oos.flush();

                // Read the server response (blocking)
                Container response = (Container) ois.readObject();
                return response;
            } catch (IOException | ClassNotFoundException e) {
                return new Container("error", "Failed to send request: " + e.getMessage());
            }
        });
    }

    /**
     * Convenience method to build a Container request from a command and a map of
     * parameters.
     */
    public Future<Container> sendRequestWithResponse(String command, Map<String, String> params) {
        // Build key=value string (separated by &)
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        Container request = new Container(command, joiner.toString());
        return sendRequestWithResponse(request);
    }

    /**
     * Example method to perform sign-in.
     */
    public Container signin(String username, String password) throws InterruptedException, ExecutionException {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        Future<Container> futureResponse = sendRequestWithResponse("signin", params);
        Container response = futureResponse.get();
        System.out.println("Signin response: " + response);
        return response;
    }

    /**
     * Example method to perform sign-up.
     */
    public Container signup(String nickname, String username, String email, String password)
            throws InterruptedException, ExecutionException {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);
        Future<Container> futureResponse = sendRequestWithResponse("signup", params);
        Container response = futureResponse.get();
        System.out.println("Signup response: " + response);
        return response;
    }

    /**
     * Example method to create a chat room.
     */
    public Container createChat(String chatName) throws InterruptedException, ExecutionException {
        Map<String, String> params = new HashMap<>();
        params.put("chatName", chatName);
        Future<Container> futureResponse = sendRequestWithResponse("createChat", params);
        Container response = futureResponse.get();
        System.out.println("Create chat response: " + response);
        return response;
    }

    /**
     * Example method to join a chat room.
     */
    public Container joinChat(String chatName) throws InterruptedException, ExecutionException {
        Map<String, String> params = new HashMap<>();
        params.put("chatName", chatName);
        Future<Container> futureResponse = sendRequestWithResponse("joinChat", params);
        Container response = futureResponse.get();
        System.out.println("Join chat response: " + response);
        return response;
    }

    // Add more methods as needed, e.g.:
    // connectChat(String chatName), findChat(String chatPartialName),
    // sendMessage(String text), sendMessage(byte[] imageData),
    // getHistory(Timestamp since), quitChat(String chatName), deleteUser(String
    // password), etc.

    /**
     * Clean up resources.
     */
    public void close() {
        executor.shutdown();
        try {
            if (oos != null)
                oos.close();
            if (ois != null)
                ois.close();
        } catch (IOException e) {
            System.err.println("Error closing streams: " + e.getMessage());
        }
    }
}
