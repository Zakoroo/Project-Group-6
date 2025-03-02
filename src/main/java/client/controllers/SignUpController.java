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

public class SignUpController {

    @FXML
    private Hyperlink alreadyregistered;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField nicknameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUp;

    @FXML
    private TextField usernameField;

    private ClientConnection clientConnection;

    public void setClientConnection(ClientConnection connection) {
        this.clientConnection = connection;
    }

    @FXML
    void btnSignUp(ActionEvent event) {
        String nickname = nicknameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (nickname.isEmpty() || username.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("All fields are required!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match!");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);

        try {
            Container response = clientConnection
                    .sendRequest(new Container("signup", buildParamString(params)))
                    .get();
            if ("signup-success".equals(response.getCommand())) {
                errorLabel.setText("Registration successful! " + response.getData());
                // Optionally, switch back to sign-in view:
            } else {
                errorLabel.setText((String) response.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
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

    @FXML
    void switchToSignIn(ActionEvent event) throws IOException {
        Stage stage = (Stage) nicknameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signInView.fxml"));
        Parent root = loader.load();
        // Pass along the clientConnection back to sign-in controller
        client.controllers.SignInController signInController = loader.getController();
        signInController.setClientConnection(this.clientConnection);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
