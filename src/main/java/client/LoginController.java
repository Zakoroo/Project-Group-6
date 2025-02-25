package client;

import shared.Container;

public class LoginController {
    private LoginView view;
    private Client client;

    public LoginController(LoginView view) {
        this.view = view;
        try {
            this.client = new Client("127.0.0.1", 8005);
        } catch (Exception e) {
            e.printStackTrace();
            view.showAlert("Unable to connect to server.");
        }
    }

    public void handleLogin(String username, String password) {
        try {
            Container request = new Container("signin", "username=" + username + "&password=" + password);
            Container response = client.sendRequest(request);

            if (response.getMessage().equals("success")) {
                view.switchToChat();
            } else {
                view.showAlert("Login failed: " + response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showAlert("Server communication error.");
        }
    }

    public void handleSignup(String username, String password) {
        try {
            Container request = new Container("signup", "username=" + username + "&password=" + password);
            Container response = client.sendRequest(request);

            if (response.getMessage().equals("success")) {
                view.showAlert("Signup successful! You can now log in.");
            } else {
                view.showAlert("Signup failed: " + response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showAlert("Server communication error.");
        }
    }
}
