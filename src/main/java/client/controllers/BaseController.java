package client.controllers;

import client.SceneManager;
import client.models.ClientModel;


public class BaseController {
    protected SceneManager sceneManager;
    protected ClientSender clientSender;
    protected ClientModel clientModel;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setClientSender(ClientSender clientSender) {
        this.clientSender = clientSender;
    }

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    public void setDependencies(){}

    public void render() {}
}