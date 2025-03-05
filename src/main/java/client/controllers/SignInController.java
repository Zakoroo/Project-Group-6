package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import shared.Container;

public class SignInController extends BaseController {

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink registerNow;

    @FXML
    private Button settingsBtn;

    @FXML
    private Button signInBtn;

    @FXML
    private TextField usernameField;

    private ClientSender clientSender;

    public SignInController(){
        //Empty constructor
    }

    // Setter methods to initialize dependencies
    public void setClientSender(ClientSender clientSender) {
        this.clientSender = clientSender;
    }

    @FXML
    public void handleSignIn(ActionEvent event) {
        errorLabel.setText(""); // Clear error message on each attempt

        if (clientSender == null) {
            System.err.println("Error: ClientSender is not initialized!");
            errorLabel.setText("Internal error: ClientSender not set");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password required!");
            return;
        }

        try {
            clientSender.signin(username, password);
            switchScene("/fxml/mainView.fxml", event, clientSender);
        } catch (Exception e) {
            errorLabel.setText("Sign in failed!");
        }
    }

    @FXML
    public void handleRegisterNow(ActionEvent event) {
        switchScene("/fxml/signupView.fxml", event, clientSender);
    }

    @FXML
    public void handleSettings(ActionEvent event) {
        switchScene("/fxml/settingsView.fxml", event, clientSender);
    }
}
