package client;

import java.util.concurrent.Future;

import shared.Container;

public class LoginController {
    private LoginView view;
    private ClientConnection client;

    public LoginController(LoginView view) {
        this.view = view;
        try {
            this.client = new ClientConnection("127.0.0.1", 8005);
        } catch (Exception e) {
            e.printStackTrace();
            view.showAlert("Unable to connect to server.");
        }
    }

    public void handleLogin(String username, String password) {
        Future<Container> futureResponse = client.sendRequestWithResponse(
            new Container("login", "username=" + username + "&password=" + password)
        );
    
        new Thread(() -> {
            try {
                Container response = futureResponse.get();
                javafx.application.Platform.runLater(() -> {
                    if ("success".equals(response.getMessage())) {
                        view.switchToChat();
                    } else {
                        view.showAlert("Login failed: " + response.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    

    public void handleSignup(String username, String password) {
        Container request = new Container("signup", "username=" + username + "&password=" + password);
        client.sendRequest(request);
    }
}
