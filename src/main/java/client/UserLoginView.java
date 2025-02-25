package client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserLoginView implements ChatView {
    private Scene scene;
    private ChatModel chatModel;
    private UserController userController;
    private Stage primaryStage;

    public UserLoginView(Stage primaryStage, ChatModel chatModel) {
        this.primaryStage = primaryStage;
        this.chatModel = chatModel;
        this.userController = new UserController(primaryStage, chatModel);
        createLoginScene();
    }

    private void createLoginScene() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label title = new Label("Enter your username and password:");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Login button action 
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (userController.loginUser(username, password)) {
                primaryStage.setScene(new ChatListView(primaryStage, chatModel).getScene());
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        });

        // Navigate to Sign Up screen
        signupButton.setOnAction(e -> {
            primaryStage.setScene(new SignupView(primaryStage, chatModel).getScene());
        });

        layout.getChildren().addAll(title, usernameField, passwordField, loginButton, signupButton, errorLabel);
        scene = new Scene(layout, 400, 300);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void update() {}
}
