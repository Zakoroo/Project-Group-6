package client.controllers;

import client.SceneManager;
import client.models.ClientModel;
import client.models.SearchModel;


public abstract class BaseController {
    protected SceneManager sceneManager;
    protected ClientSender clientSender;
    protected ClientModel clientModel;
    protected SearchModel searchModel;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setClientSender(ClientSender clientSender) {
        this.clientSender = clientSender;
    }

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    public void setSearchModel(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    public abstract void initialize();

    public abstract void setDependencies();

    public abstract void render();
}