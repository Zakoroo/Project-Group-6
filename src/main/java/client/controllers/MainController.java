package client.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import client.models.ClientModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
    
    private final static long SIZE_LIMIT = 10 * 1024 * 1024; // 10 MB
    
    @FXML
    private Button SendBtn;

    @FXML
    private Button SignOutBtn;

    @FXML
    private Button addGroupBtn;

    @FXML
    private Button attachImage;

    @FXML
    private ListView<String> chatListView;

    @FXML
    private VBox chatContainer;

    @FXML
    private TextField textMessageField;

    @FXML
    private Label errorLabel;

    @FXML
    void handleAddGroup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/searchView.fxml"));
        Parent root = loader.load();

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add Group");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    @FXML
    void handleAttachImage(ActionEvent event) {
        Stage stage = (Stage) attachImage.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image to Attach");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        File file = fileChooser.showOpenDialog(stage);

        if (file == null) {
            errorLabel.setText("No file selected.");
            return;
        }

        if (file.length() > SIZE_LIMIT) {
            errorLabel.setText("File exceeds size limit of " + SIZE_LIMIT + " bytes.");
            return;
        }

        String extension = getFileExtension(file);
        if (isValidImageFile(extension)) {
            try {
                Image image = new Image(file.toURI().toString());
                handleSendImage(image);
            } catch (Exception e) {
                errorLabel.setText("Failed to load image.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid file type: " + extension);
        }
    }

    private String getFileExtension(File file) {
        String filename = file.getName();
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex != -1) ? filename.substring(dotIndex + 1).toLowerCase() : "";
    }

    private boolean isValidImageFile(String extension) {
        return extension.equals("png") || extension.equals("jpg") || extension.equals("gif");
    }

    @FXML
    public void handleSendMessage(ActionEvent event) {
        if (!textMessageField.getText().isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatBox.fxml"));
                HBox chatBox = loader.load();

                Label messageLabel = (Label) chatBox.lookup("#messageLabel");
                Label userNameLabel = (Label) chatBox.lookup("#userNameLabel");
                Label timeStampLabel = (Label) chatBox.lookup("#timeStampLabel");
                ImageView profileImage = (ImageView) chatBox.lookup("#profileImage");

                messageLabel.setText(textMessageField.getText());
                userNameLabel.setText("Me");
                timeStampLabel.setText(LocalTime.now().toString());
                profileImage.setImage(new Image(getClass().getResource("/pictures/user_1.png").toExternalForm()));

                chatContainer.getChildren().add(chatBox);
                textMessageField.clear();

                handleReceiveMessage("Other User", "This is a response message.");

            } catch (Exception e) {
                errorLabel.setText("Failed to send message.");
                e.printStackTrace();
            }
        }
    }

    public void handleReceiveMessage(String sender, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OtherUserChatBox.fxml"));
            HBox chatBox = loader.load();

            Label messageLabel = (Label) chatBox.lookup("#messageLabel");
            Label userNameLabel = (Label) chatBox.lookup("#userNameLabel");
            Label timeStampLabel = (Label) chatBox.lookup("#timeStampLabel");
            ImageView profileImage = (ImageView) chatBox.lookup("#profileImage");

            messageLabel.setText(message);
            userNameLabel.setText(sender);
            timeStampLabel.setText(LocalTime.now().toString());
            profileImage.setImage(new Image(getClass().getResource("/pictures/user_1.png").toExternalForm()));

            chatContainer.getChildren().add(chatBox);
        } catch (Exception e) {
            errorLabel.setText("Failed to receive message.");
            e.printStackTrace();
        }
    }

    public void handleSendImage(Image image) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ImageChatBox.fxml"));
            HBox chatBox = loader.load();

            Label userNameLabel = (Label) chatBox.lookup("#userNameLabel");
            Label timeStampLabel = (Label) chatBox.lookup("#timeStampLabel");
            ImageView profileImage = (ImageView) chatBox.lookup("#profileImage");
            ImageView messageImage = (ImageView) chatBox.lookup("#messageImage");

            userNameLabel.setText("Me");
            timeStampLabel.setText(LocalTime.now().toString());
            profileImage.setImage(new Image(getClass().getResource("/pictures/user_1.png").toExternalForm()));
            messageImage.setImage(image);

            chatContainer.getChildren().add(chatBox);

            handleReceiveImage(new Image(getClass().getResource("/pictures/pigeon.jpg").toExternalForm()));
        } catch (Exception e) {
            errorLabel.setText("Failed to send image.");
            e.printStackTrace();
        }
    }

    public void handleReceiveImage(Image image) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/otherUserChatBox.fxml"));
            HBox chatBox = loader.load();

            Label userNameLabel = (Label) chatBox.lookup("#userNameLabel");
            Label timeStampLabel = (Label) chatBox.lookup("#timeStampLabel");
            ImageView profileImage = (ImageView) chatBox.lookup("#profileImage");
            ImageView messageImage = (ImageView) chatBox.lookup("#messageImage");

            userNameLabel.setText("Other User");
            timeStampLabel.setText(LocalTime.now().toString());
            profileImage.setImage(new Image(getClass().getResource("/pictures/user_1.png").toExternalForm()));
            messageImage.setImage(image);

            chatContainer.getChildren().add(chatBox);
        } catch (Exception e) {
            errorLabel.setText("Failed to receive image.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleSignOut(ActionEvent event) {
        chatContainer.getChildren().clear();
    }

    @FXML
    public void populateChatListView() {
        List<String> chatGroups = Arrays.asList("General Chat", "Project Team", "Friends", "Study Group");
        ObservableList<String> chatList = FXCollections.observableArrayList(chatGroups);
        chatListView.setItems(chatList);
    }
}
