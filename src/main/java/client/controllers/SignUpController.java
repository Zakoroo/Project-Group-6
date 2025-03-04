package client.controllers;

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

   public SignUpController (ClientSender clientSender) {
        this.clientSender = clientSender;
   }
    public SignUpController() {
        //Empty constructor
    }

    @FXML
    void handleAlreadyRegistered(ActionEvent event) {
        switchScene("/fxml/signinView.fxml", event);
    }

    @FXML
    void handleSettings(ActionEvent event) {
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        String nickname = nicknameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = enterPasswordField.getText();
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
        
        switchScene("fxml/signinView.fxml", event);
    }

    /*void switchToSignInSuccess(String username) throws IOException {
        Stage stage = (Stage) nicknameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signinView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        errorLabel.setText("Sign up successful as user: " + username + "!");
        stage.setScene(scene);
        stage.show();
    }*/
}
