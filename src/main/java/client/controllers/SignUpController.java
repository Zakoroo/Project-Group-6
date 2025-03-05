package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignUpController extends BaseController {

    @FXML
    private Hyperlink alreadyRegistered;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField enterPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField nicknameField;

    @FXML
    private Button settingsBtn;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField usernameField;

    private ClientSender clientSender;

    // Setter for ClientSender injection
    public void setClientSender(ClientSender clientSender) {
        this.clientSender = clientSender;
        System.out.println("ClientSender has been set in SignUpController!"); // Debug message
    }

    @FXML
    void handleAlreadyRegistered(ActionEvent event) {
        switchScene("/fxml/signinView.fxml", event, clientSender);
    }

    @FXML
    void handleSettings(ActionEvent event) {
        // Implementation for settings (currently empty)
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        System.out.println("Sign up button clicked");
        
        if (clientSender == null) {
            System.err.println("Error: ClientSender is not initialized!");
            errorLabel.setText("Internal error: ClientSender not set");
            return;
        }
        
        String nickname = nicknameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = enterPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate fields
        if (nickname.isEmpty() || username.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("All fields are required!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match!");
            return;
        }

        // Call the signup method via the clientSender instance
        try {
            clientSender.signup(nickname, username, email, password);
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Sign-up failed: " + e.getMessage());
            return;
        }

        // Navigate to the sign-in view upon successful sign-up
        switchScene("/fxml/signinView.fxml", event, clientSender);
    }
}
