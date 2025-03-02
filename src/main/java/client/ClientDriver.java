package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.controllers.SignInController;

import java.io.IOException;

public class ClientDriver extends Application {
    private ClientConnection clientConnection;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Connect to the server
            clientConnection = new ClientConnection("127.0.0.1", 8005);

            // Load the sign-in view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signInView.fxml"));
            Scene scene = new Scene(loader.load());

            // Inject client connection into the SignInController
            SignInController controller = loader.getController();
            controller.setClientConnection(clientConnection);

            // (Optional) load stylesheet
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            primaryStage.setTitle("Chat Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to start client: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
