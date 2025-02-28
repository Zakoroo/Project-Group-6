package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import client.ClientConnection;
import shared.Container;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SignInController {

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink register;

    @FXML
    private Button signIn;

    @FXML
    private TextField usernameField;

    private ClientConnection clientConnection;

    public void setClientConnection(ClientConnection connection) {
        this.clientConnection = connection;
    }

    @FXML
    void btnSignIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password required!");
            return;
        }

        Container request = new Container("signin", "username=" + username + "&password=" + password);
        try {
            Container response = clientConnection.sendRequestWithResponse(request).get();
            if ("signin-success".equals(response.getCommand())) {
                errorLabel.setText("Login successful!");
                //switchToChat();
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        } catch (InterruptedException | ExecutionException e) {
            errorLabel.setText("Server error: " + e.getMessage());
        }
    }

    private void switchToChat() throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/chatView.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void switchToSignUp(ActionEvent event) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/signUpView.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
