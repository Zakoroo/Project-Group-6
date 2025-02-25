package client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatView {
    private Stage stage;
    private ChatController controller;

    public ChatView(Stage stage) {
        this.stage = stage;
        this.controller = new ChatController(this);
    }

    public void show() {
        Label chatLabel = new Label("Chatroom Name:");
        TextField chatField = new TextField();
        Button createChatButton = new Button("Create Chat");
        Button logoutButton = new Button("Logout");

        createChatButton.setOnAction(event -> controller.handleCreateChat(chatField.getText()));
        logoutButton.setOnAction(event -> controller.handleLogout());

        VBox layout = new VBox(10, chatLabel, chatField, createChatButton, logoutButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Chat Room");
        stage.show();
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public void returnToLogin() {
        new LoginView(stage).show();
    }
}
