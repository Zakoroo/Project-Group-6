package client.controllers;

import client.ClientConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shared.Container;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;

public class ChatController {

    @FXML
    private Button addGroupButton;

    @FXML
    private HBox addGroupHBox;

    @FXML
    private Button attachFileButton;

    @FXML
    private VBox bottomVBox;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox chatVBox;

    @FXML
    private Label currentUserLabel;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private ListView<?> groupsListView;

    @FXML
    private ScrollPane groupsScrollPane;

    @FXML
    private HBox imageChatHBox;

    @FXML
    private HBox imageMessageHeaderHBox;

    @FXML
    private ImageView imageMessageImageView;

    @FXML
    private Label imageMessageNameLabel;

    @FXML
    private Label imageMessageTimestampLabel;

    @FXML
    private VBox imageMessageVBox;

    @FXML
    private VBox leftVBox;

    @FXML
    private HBox member1HBox;

    @FXML
    private ImageView member1ImageView;

    @FXML
    private HBox member1NameHBox;

    @FXML
    private Label member1NameLabel;

    @FXML
    private HBox member2HBox;

    @FXML
    private ImageView member2ImageView;

    @FXML
    private HBox member2NameHBox;

    @FXML
    private Label member2NameLabel;

    @FXML
    private ScrollPane membersScrollPane;

    @FXML
    private VBox membersVBox;

    @FXML
    private HBox messageInputHBox;

    @FXML
    private TextField messageTextField;

    @FXML
    private HBox myChatHBox;

    @FXML
    private HBox myMessageHeaderHBox;

    @FXML
    private ImageView myMessageImageView;

    @FXML
    private Label myMessageLabel;

    @FXML
    private VBox myMessageVBox;

    @FXML
    private Label myNameLabel;

    @FXML
    private Label myTimestampLabel;

    @FXML
    private HBox otherChatHBox;

    @FXML
    private HBox otherMessageHeaderHBox;

    @FXML
    private ImageView otherMessageImageView;

    @FXML
    private Label otherMessageLabel;

    @FXML
    private VBox otherMessageVBox;

    @FXML
    private Label otherNameLabel;

    @FXML
    private Label otherTimestampLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private VBox rightVBox;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView sentImageView;

    @FXML
    private Button signOutButton;

    // Client connection for sending requests to the server.
    private ClientConnection clientConnection;

    public void setClientConnection(ClientConnection connection) {
        this.clientConnection = connection;
    }

    // Helper method to build parameter string from a map.
    private String buildParamString(Map<String, String> params) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return joiner.toString();
    }

    @FXML
    void handleAddGroup(ActionEvent event) {
        // For demonstration, we'll use a fixed group name.
        Map<String, String> params = new HashMap<>();
        params.put("group", "NewGroup"); // You might instead get this from a dialog
        try {
            Container response = clientConnection
                    .sendRequest(new Container("add-group", buildParamString(params)))
                    .get();
            if ("add-group-success".equals(response.getCommand())) {
                errorMessageLabel.setText("Group added successfully!");
                // update groupsListView here.
            } else {
                errorMessageLabel.setText((String) response.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            errorMessageLabel.setText("Server error: " + e.getMessage());
        }
    }

    final private static long SIZE_LIMIT = 22000; // in bytes

    @FXML
    void handleAttachFile(ActionEvent event) {
        // Get the stage from the event source
        Stage stage = (Stage) attachFileButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Attach");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            // Check if file size is valid
            long size = file.length();
            if (size > SIZE_LIMIT) {
                errorMessageLabel.setText("File exceeds the size limit: " + SIZE_LIMIT + " bytes");
                return;
            }

            // Extract the file extension
            String filename = file.getName();
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex < filename.length() - 1) {
                String extension = filename.substring(dotIndex + 1).toLowerCase();

                // Filter out unwanted file types
                switch (extension) {
                    case "png":
                    case "jpg":
                    case "gif":
                        // Valid file type; continue processing
                        break;
                    default:
                        errorMessageLabel.setText("Invalid file type! Only png, jpg, and gif are allowed.");
                        return;
                }
            } else {
                errorMessageLabel.setText("No file extension found.");
                return;
            }

            try {
                // Read file data into byte array
                byte[] fileData = java.nio.file.Files.readAllBytes(file.toPath());
                // Send the file data with the "send-message" command.
                Container response = clientConnection.sendRequest(new Container("send-message", fileData)).get();
                if ("send-message-success".equals(response.getCommand())) {
                    errorMessageLabel.setText("File attached and sent successfully!");
                    // Create a new message bubble and add it to the chatVBox.
                } else {
                    errorMessageLabel.setText((String) response.getData());
                }
            } catch (IOException | InterruptedException | ExecutionException e) {
                errorMessageLabel.setText("Server error: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleSendMessage(ActionEvent event) {
        String message = messageTextField.getText().trim();
        if (message.isEmpty()) {
            errorMessageLabel.setText("Please enter a message!");
            return;
        }
        // Clear previous error message.
        errorMessageLabel.setText("");

        Map<String, String> params = new HashMap<>();
        params.put("message", message);
        // Optionally, include sender information if needed.
        try {
            Container response = clientConnection
                    .sendRequest(new Container("send-message", buildParamString(params)))
                    .get();
            if ("send-message-success".equals(response.getCommand())) {
                errorMessageLabel.setText("Message sent!");
                // Create a new message bubble and add it to the chatVBox.
                HBox messageBubble = new HBox();
                messageBubble.setSpacing(10.0);
                messageBubble.setPadding(new Insets(10));
                messageBubble.setStyle("-fx-background-color: #e6e6e6; -fx-background-radius: 8px;");
                Label messageLabel = new Label(message);
                messageLabel.setWrapText(true);
                messageBubble.getChildren().add(messageLabel);
                chatVBox.getChildren().add(messageBubble);
                // Scroll to the bottom of the chat.
                chatScrollPane.setVvalue(1.0);
                messageTextField.clear();
            } else {
                errorMessageLabel.setText((String) response.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            errorMessageLabel.setText("Server error: " + e.getMessage());
        }
    }

    @FXML
    void handleSignOut(ActionEvent event) {
        try {
            Container response = clientConnection
                    .sendRequest(new Container("signout", ""))
                    .get();
            if ("signout-success".equals(response.getCommand())) {
                errorMessageLabel.setText("Signed out successfully!");
                // Switch to the sign-in view
                // switchToSignInView(event);
            } else {
                errorMessageLabel.setText((String) response.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            errorMessageLabel.setText("Server error: " + e.getMessage());
        }
    }
}
