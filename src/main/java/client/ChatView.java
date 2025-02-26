package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatView {
    private Stage stage;
    private ChatController controller;
    private ObservableList<String> messages;
    private ListView<String> messageListView;
    private TextField messageField;
    private TextField chatroomField;
    private Button enterChatButton;
    private Button sendButton;
    private Button logoutButton;

    public ChatView(Stage stage) {
        this.stage = stage;
        this.controller = new ChatController(this);
        this.messages = FXCollections.observableArrayList();
    }

    public void show() {
        Label chatLabel = new Label("Enter Chatroom:");
        chatroomField = new TextField();
        chatroomField.setPromptText("Chatroom Name");
        enterChatButton = new Button("Enter Chat");

        messageListView = new ListView<>(messages); // Display messages
        messageListView.setPrefHeight(400);
        messageField = new TextField();
        messageField.setPromptText("Type a message...");
        sendButton = new Button("Send");
        logoutButton = new Button("Logout");

        // Disable message field and send button until a chatroom is entered
        messageField.setDisable(true);
        sendButton.setDisable(true);

        // Enter Chatroom
        enterChatButton.setOnAction(event -> {
            String chatroom = chatroomField.getText().trim();
            if (!chatroom.isEmpty()) {
                controller.setCurrentChatRoom(chatroom);
                controller.handleEnterChatroom(chatroom);
                chatLabel.setText("Chatroom: " + chatroom);
                chatroomField.setDisable(true);
                enterChatButton.setDisable(true);
                messageField.setDisable(false);
                sendButton.setDisable(false);
            } else {
                showAlert("Please enter a chatroom name.");
            }
        });

        // Send message when button is clicked
        sendButton.setOnAction(event -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                controller.handleSendMessage(message);
                messageField.clear();
            }
        });

        // Allow sending message using ENTER key
        messageField.setOnAction(event -> sendButton.fire());

        // Logout button action
        logoutButton.setOnAction(event -> controller.handleLogout());

        VBox layout = new VBox(10, chatLabel, chatroomField, enterChatButton, messageListView, messageField, sendButton, logoutButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Chat Room");
        stage.show();
    }


    public void addMessage(String message) {
        Platform.runLater(() -> messages.add(message));
    }

    public void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
        });
    }

    public void returnToLogin() {
        new LoginView(stage).show();
    }
}
