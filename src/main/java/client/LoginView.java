package client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    private Stage stage;
    private LoginController controller;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.controller = new LoginController(this);
    }

    public void show() {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Signup");

        loginButton.setOnAction(event -> controller.handleLogin(usernameField.getText(), passwordField.getText()));
        signupButton.setOnAction(event -> controller.handleSignup(usernameField.getText(), passwordField.getText()));

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, signupButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 400 );
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public void switchToChat() {
        new ChatView(stage).show();
    }
}
