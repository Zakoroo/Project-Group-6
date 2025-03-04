package client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SearchController extends BaseController {

    @FXML
    private Label errorLabel;

    @FXML
    private Button searchBtn;

    @FXML
    private Button searchBtn1;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> searchResultsList;

    @FXML
    void handleFindChatRooms(ActionEvent event) {
        
    }
    

}
