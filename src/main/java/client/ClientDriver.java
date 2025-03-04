package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import client.controllers.*;
import client.models.ClientModel;

public class ClientDriver extends Application {
    private static ClientConnection connection;
    private static ClientSender clientSender;
    private static ClientReceiver clientReceiver;
    private static ClientModel clientModel;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the sign-in view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signinView.fxml"));
            Scene scene = new Scene(loader.load());

            // Initialize the controller and inject dependencies
            SignInController controller = loader.getController();
            controller.setClientSender(clientSender);
            controller.setClientReceiver(clientReceiver);
            controller.setClientModel(clientModel);

            // Set the controller in ClientReceiver
            clientReceiver.setCurrentController(controller);

            primaryStage.setTitle("Chat Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Failed to start client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // connect to the server
        connection = ClientConnection.getInstance();
        try {
            connection.connect("localhost", 8005);
            clientSender = new ClientSender(connection.getOutputStream());
            clientReceiver = new ClientReceiver(connection.getInputStream(), null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // launch the GUI
        launch(args);
    }
}
