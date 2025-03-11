package client.controllers;

// Import the SceneManager class
import client.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class SettingsController extends BaseController {

    // Declare FXML components
    @FXML
    private TextField ipAdress; // The TextField for IP Address
    @FXML
    private TextField port; // The TextField for Port
    @FXML
    private Label errorLabel; // The label for error messages

    @Override
    public void initialize() {
    }

    @Override
    public void setDependencies() {
    }

    @Override
    public void render() {
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        String ip = ipAdress.getText();
        String portText = port.getText();
        if (ip.isEmpty() || portText.isEmpty()) {
            System.err.println("Fields empty!");
            return;
        }
        errorLabel.setText(""); // Clear error label
        try {
            ClientReceiver.getInstance().stop();
            ClientConnection.getInstance().connect(ip, Integer.parseInt(portText));
            ClientReceiver.getInstance().reset();
            new Thread(ClientReceiver.getInstance()).start();

            SceneManager.getInstance().switchScene("/fxml/signinView.fxml");
        } catch (Exception e) {
            errorLabel.setText("Connection failed: " + e.getMessage());
        }
    }

}
