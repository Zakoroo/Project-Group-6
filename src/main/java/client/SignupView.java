package client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SignupView implements ChatView {
    private Scene scene;
    private ChatModel chatModel;
    private UserController userController;
    private Stage primaryStage;

    public SignupView(Stage primaryStage, ChatModel chatModel) {
        this.primaryStage = primaryStage;
        this.chatModel = chatModel;
        this.userController = new UserController(primaryStage, chatModel);
        createSignupScene();
    }

    private void createSignupScene() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label title = new Label("Create a new account:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        Button signupButton = new Button("Sign Up");
        Button backButton = new Button("Back to Login");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        // Sign Up button action
        signupButton.setOnAction(_ -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }

            if (userController.signupUser(username, email, password, confirmPassword)) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Signup successful! Redirecting to login...");
                primaryStage.setScene(new UserLoginView(primaryStage, chatModel).getScene());
            } else {
                messageLabel.setText("Signup failed. User may already exist.");
            }
        });

        // Back to Login button action
        backButton.setOnAction(_ -> {
            primaryStage.setScene(new UserLoginView(primaryStage, chatModel).getScene());
        });

        layout.getChildren().addAll(title, usernameField, emailField, passwordField, confirmPasswordField, signupButton, backButton, messageLabel);
        scene = new Scene(layout, 400, 300);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void update() {}
}
