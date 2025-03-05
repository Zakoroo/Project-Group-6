package client.controllers;

import java.io.IOException;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import client.models.*;

public abstract class BaseController {
    protected ClientSender clientSender;
    protected ClientReceiver clientReceiver;
    protected ClientModel clientModel;

    public void setClientSender(ClientSender clientSender) {
        this.clientSender = clientSender;
    }

    public void setClientReceiver(ClientReceiver clientReceiver) {
        this.clientReceiver = clientReceiver;
    }

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    protected void switchScene(String name, Object source) {
        switchScene(name, source, null); // Call the other method with null as ClientSender
    }
    
    protected void switchScene(String name, Object source, ClientSender clientSender) {
        Stage stage = null;
        if (source instanceof ActionEvent) {
            stage = (Stage)((Node)((ActionEvent) source).getSource()).getScene().getWindow();
        } else if (source instanceof Node) {
            stage = (Stage)((Node) source).getScene().getWindow();
        } else {
            System.err.println("Error: No valid source provided to switch scene.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(name.startsWith("/") ? name : "/" + name));
            Parent root = loader.load();
    
            // Pass the ClientSender if applicable
            Object controller = loader.getController();
            if (clientSender != null && controller instanceof SignUpController) {
                ((SignUpController) controller).setClientSender(clientSender);
            } else if (clientSender != null && controller instanceof SignInController) {
                ((SignInController) controller).setClientSender(clientSender);
            }
    
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}

