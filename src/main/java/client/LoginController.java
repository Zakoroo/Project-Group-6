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
        Container request = new Container("login", "username=" + username + "&password=" + password);
        client.sendRequest(request);
    }
    

    public void handleSignup(String username, String password) {
        Container request = new Container("signup", "username=" + username + "&password=" + password);
        client.sendRequest(request);
    }
}
