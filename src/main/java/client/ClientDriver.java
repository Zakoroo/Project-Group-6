package client;

import javafx.application.Application;
import javafx.stage.Stage;
import client.controllers.*;
import client.models.ClientModel;
import client.models.SearchModel;


public class ClientDriver extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Chat Application");

            // Initialize and create scene manager
            SceneManager.initialize(primaryStage);
            SceneManager.getInstance().switchScene("/fxml/signinView.fxml");

            // set fields for receiver
            ClientReceiver.getInstance().setSceneManager(SceneManager.getInstance());
            ClientReceiver.getInstance().setClientModel(ClientModel.getInstance());
            ClientReceiver.getInstance().setSearchModel(SearchModel.getInstance());
        } catch (Exception e) {
            System.err.println("Failed to start client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // connect to the server
        ClientConnection connection = ClientConnection.getInstance();
        try {
            ClientSender.initialize(connection);
            ClientReceiver.initialize(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // launch the GUI
        launch(args);
    }
}
