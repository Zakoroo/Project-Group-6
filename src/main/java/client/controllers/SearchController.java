package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import shared.ChatRoom;

public class SearchController extends BaseController {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<ChatRoom> searchResultsList;

    @FXML
    public void initialize() {}

    @Override
    public void setDependencies() {
        // check model is not null
        if (searchModel == null) {
            System.err.println("ERROR: Search model is not initialized yet!");
            return;
        }

        // bind properties
        searchResultsList.setItems(searchModel.getResultSet());

        searchResultsList.getSelectionModel().selectedItemProperty().addListener((observable, oldChat, newChat) -> {
            if (newChat != null) {
                try {
                    clientSender.joinChat(newChat.name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // clientModel.addChatRoom(newChat);
                System.out.println("Joined to chat updated: " + newChat.name());
            }
        });
    }


    @Override
    public void render() {}

    @FXML
    void btnHandleAdd(ActionEvent event) {

        String chatname = searchField.getText();
        try {
            clientSender.createChat(chatname);
            clientSender.getJoinedChats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnHandleSearch(ActionEvent event) {
        String searchQuery = searchField.getText();
        
        try {
            if (searchQuery.isEmpty()) {
                clientSender.findChat(".");
            } else {
                clientSender.findChat(searchQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
