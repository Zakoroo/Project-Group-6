package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import client.controllers.BaseController;
import client.models.ClientModel;
import client.models.SearchModel;
import client.controllers.ClientSender;
import client.controllers.ClientReceiver;

public class SceneManager {
    private static SceneManager instance;
    private BaseController currentController;
    private Stage primaryStage;

    public static void initialize(Stage primaryStage) {
        if (instance == null) {
            instance = new SceneManager(primaryStage);
        } else {
            System.out.println("SceneManager is already initialized!");
        }
    }
    
    public static SceneManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SceneManager may have not been initialized!");
        }
        return instance;
    }

    private SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());

            // Get the controller and inject SceneManager
            Object controller = loader.getController();
            if (controller instanceof BaseController && controller != null) {
                BaseController baseController = (BaseController) controller;
                baseController.setSceneManager(this);
                baseController.setClientModel(ClientModel.getInstance());
                baseController.setClientSender(ClientSender.getInstance());
                baseController.setDependencies();
                baseController.render();
                currentController = baseController;
            }

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPopup(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());

            Stage popupStage = new Stage();
            popupStage.initOwner(primaryStage);
            popupStage.setTitle(title);
            popupStage.setScene(scene);
            popupStage.setResizable(false);

            // Get the controller and inject SceneManager
            Object controller = loader.getController();
            if (controller instanceof BaseController && controller != null) {
                BaseController baseController = (BaseController) controller;
                baseController.setSceneManager(this);
                baseController.setClientModel(ClientModel.getInstance());
                baseController.setClientSender(ClientSender.getInstance());
                baseController.setSearchModel(SearchModel.getInstance());
                baseController.setDependencies();
                baseController.render();
            }

            popupStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BaseController getCurrenController() {
        return this.currentController;
    }

    public void setCurrenController(BaseController currenController) {
        this.currentController = currenController;
    }
}
