package client.controllers;
import client.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import client.SceneManager;  // Import the SceneManager class

public class SettingsController {

    // Declare FXML components
    @FXML
    private TextField ipAdress;  // The TextField for IP Address
    @FXML
    private TextField port;      // The TextField for Port
    @FXML
    private Label errorLabel;    // The label for error messages

    // This method is called when the "Confirm" button is clicked
    @FXML
    private void handleConfirm(ActionEvent event) {
        // Get the values from the TextFields
        String ip = ipAdress.getText();
        String portText = port.getText();

        // Simple validation example
            // Proceed with further logic, e.g., saving settings, etc.
            errorLabel.setText(""); // Clear error label
            try {
                ClientConnection.getInstance().connect(ip, Integer.parseInt(portText));
            } catch (Exception e) {
                errorLabel.setText("connection failed");
            }

            // Switch scene to the SignIn view using SceneManager singleton
            SceneManager.getInstance().switchScene("/fxml/signinView.fxml");
        
    }
}
