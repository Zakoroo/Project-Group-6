package client.controllers;

import client.ClientConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import shared.Container;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;

public class SignInController {

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink register;

    @FXML
    private Button signin;

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

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        try {
            Container response = clientConnection
                    .sendRequest(new Container("signin", buildParamString(params)))
                    .get();
            if ("signin-success".equals(response.getCommand())) {
                errorLabel.setText("Login successful!");
                // Switch to the chat view
                switchToMainView(event);
            } else {
                errorLabel.setText((String) response.getData());
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            errorLabel.setText("Server error: " + e.getMessage());
        }
    }

    private String buildParamString(Map<String, String> params) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return joiner.toString();
    }

    /**
     * Switches from the sign-in screen to the chat screen.
     */
    @FXML
    void switchToMainView(ActionEvent event) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainView.fxml"));
        Parent root = loader.load();
        ChatController chatController = loader.getController();
        chatController.setClientConnection(this.clientConnection);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void switchToSignUp(ActionEvent event) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signUpView.fxml"));
        Parent root = loader.load();
        SignUpController signUpController = loader.getController();
        signUpController.setClientConnection(this.clientConnection);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
