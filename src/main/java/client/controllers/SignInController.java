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
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;
import shared.Container;

public class SignInController {

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
    private ClientReceiver clientReceiver;

    private String buildParamString(Map<String, String> params) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return joiner.toString();
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password required!");
            return;
        }

        try {
            clientSender.signin(username, password);
    }
    
    @FXML
    private void handleRegisterNow(ActionEvent event) {
        //TODO: Implement handle register now
    }
    @FXML
    private void handleSettings(ActionEvent event) {
    
    }
    
   
    private void switchToMainView (String username) {
        // Load the main view FXML
        Stage stage = (Stage) usernameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainView.fxml"));
        Parent root = loader.load();
        // TODO: Inject the client model into the MainController
    }


    private public void switchToSignUp(ActionEvent event) {

    }

    @FXML
    private void switchToSettings(){
        //TODO: Implement switch to sign-up view
    }
}

