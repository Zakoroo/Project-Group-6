package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {
    private Stage primaryStage;
    private Scene loginScene, chatScene;
    private String username;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createLoginScene();
        createChatScene();

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Chat Application");
        primaryStage.show();
    }

    private void createLoginScene() {
        VBox loginLayout = new VBox(10);
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label title = new Label("Enter your name:");
        TextField nameField = new TextField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            if (!nameField.getText().isEmpty()) {
                username = nameField.getText();
                primaryStage.setScene(chatScene);
            } else {
                showAlert("Error", "Please enter a name.");
            }
        });

        loginLayout.getChildren().addAll(title, nameField, loginButton);
        loginScene = new Scene(loginLayout, 400, 300);
    }

    private void createChatScene() {
        BorderPane chatLayout = new BorderPane();

        ListView<String> chatRooms = new ListView<>();
        chatRooms.getItems().addAll("General", "Sports", "Gaming", "Tech");
        chatRooms.setPrefWidth(100);

        VBox chatBox = new VBox(10);
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        TextField messageField = new TextField();
        Button sendButton = new Button("Send");

        sendButton.setOnAction(e -> {
            if (!messageField.getText().isEmpty()) {
                chatArea.appendText(username + ": " + messageField.getText() + "\n");
                messageField.clear();
            }
        });

        HBox inputBox = new HBox(10, messageField, sendButton);
        inputBox.setStyle("-fx-padding: 10;");
        chatBox.getChildren().addAll(chatArea, inputBox);

        chatLayout.setLeft(chatRooms);
        chatLayout.setCenter(chatBox);

        chatScene = new Scene(chatLayout, 600, 400);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

