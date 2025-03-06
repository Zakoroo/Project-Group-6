package client.controllers;

import java.io.File;
import java.io.IOException;
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
import shared.ChatRoom;

public class MainController extends BaseController {
    
    private final static long SIZE_LIMIT = 10 * 1024 * 1024; // 10 MB

    @FXML
    private Label currentUserLabel;
    
    @FXML
    private Button SendBtn;

    @FXML
    private Button SignOutBtn;

    @FXML
    private Button addGroupBtn;

    @FXML
    private Button attachImage;

    @FXML
    private ListView<ChatRoom> chatListView;

    @FXML
    private VBox chatContainer;

    @FXML
    private TextField textMessageField;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() {
        
    }

    @Override
    public void setDependencies() {
        if (clientModel == null) {
            System.err.println("ERROR: client model is not initialized yet!");
            return;
        }

        currentUserLabel.textProperty().bind(clientModel.usernameProperty());
        chatListView.setItems(clientModel.getJoinedChatRooms());

        chatListView.getSelectionModel().selectedItemProperty().addListener((observable, oldChat, newChat) -> {
            if (newChat != null) {
                try {
                    clientSender.connectChat(newChat.name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                clientModel.setConnectedChatRoom(newChat.name());
                System.out.println("Connected chat updated: " + newChat.name());
            }
        });
    }

    public void render() {
        try {
            clientSender.getJoinedChats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddGroup(ActionEvent event) throws IOException {
        sceneManager.showPopup("/fxml/searchView.fxml", "Search tool");
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chatBox.fxml"));
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
                String textMessage = textMessageField.getText();
                System.out.println("this is what is gonna be sent"+textMessage);
                clientSender.sendMessage(textMessage);
                textMessageField.clear();
            } catch (Exception e) {
                errorLabel.setText("Failed to send message.");
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void handleReceiveMessage(String sender, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/otherUserChatBox.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/imageChatBox.fxml"));
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
            clientSender.sendMessage(image);
        } catch (Exception e) {
            errorLabel.setText("Failed to send image.");
            e.printStackTrace();
        }
    }

    public void handleReceiveImage(Image image) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/otherUserImageChatBox.fxml"));
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
}
