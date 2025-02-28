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

public class SignUpController {

    @FXML
    private Hyperlink alreadyRegistered;

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
        String username = usernameField.getText();
        String email = emailField.getText();
        String nickname = nicknameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || nickname.isEmpty() 
                || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match!");
            return;
        }

        Container request = new Container("signup", "nickname=" + nickname + 
            "&username=" + username + "&email=" + email + "&password=" + password);
        try {
            Container response = clientConnection.sendRequestWithResponse(request).get();
            if ("signup-success".equals(response.getCommand())) {
                errorLabel.setText("Registration successful!");
                //switchToSignIn(event);
            } else {
                errorLabel.setText("User already exists!");
            }
        } catch (InterruptedException | ExecutionException e) {
            errorLabel.setText("Server error: " + e.getMessage());
        }
    }

    @FXML
    void switchToSignIn(ActionEvent event) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/signInView.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
