package client;

import javafx.application.Application;
import javafx.stage.Stage;
import client.controllers.*;
import client.models.ClientModel;

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

            // start the receiver
            new Thread(ClientReceiver.getInstance()).start();

        } catch (Exception e) {
            System.err.println("Failed to start client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // connect to the server
        ClientConnection connection = ClientConnection.getInstance();
        try {
            connection.connect("localhost", 8005);
            ClientSender.initialize(connection.getOutputStream());
            ClientReceiver.initialize(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // launch the GUI
        launch(args);
    }
}
