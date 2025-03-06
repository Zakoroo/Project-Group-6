package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SearchController extends BaseController {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> searchResultsList;

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
        if (searchQuery.isEmpty()) {
            errorLabel.setText("Please enter a search query");
            return;
        }
        
        try {
            clientSender.findChat(searchQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
