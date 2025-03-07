package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


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

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @Override
    public void setDependencies() {}

    @Override
    public void render() {}

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
        } catch (Exception e) {
            errorLabel.setText("Sign in failed!");
        }
    }

    @FXML
    public void handleRegisterNow(ActionEvent event) {
        sceneManager.switchScene("/fxml/signupView.fxml");
    }

    @FXML
    public void handleSettings(ActionEvent event) {
        sceneManager.switchScene("/fxml/settingsView.fxml");
    }
}
