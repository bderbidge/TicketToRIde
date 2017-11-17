package com.example.brandonderbidge.myapplication.Presenters;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameLobbyPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameLobbyView;

import java.util.Observable;
import java.util.Observer;

import Model.Exceptions.InvalidOperationException;
import Model.GUIFacade;
import Model.IGUIFacade;
import Model.ModelRoot;
import Model.ModelSubject;

/**
 * Created by pbstr on 10/6/2017.
 */

public class GameLobbyPresenter implements IGameLobbyPresenter, Observer {

    private IGameLobbyView mView;
    private IGUIFacade mGUIFacade;
    private ModelSubject mModelSubject; // TODO: make this an interface

    public GameLobbyPresenter(IGameLobbyView view, IGUIFacade facade) {
        mView = view;
        mGUIFacade = facade;
        mModelSubject = ModelRoot.instance(); //TODO: make this an interface and have it passed in the constructor
        mModelSubject.attach(this);

        //this will skip scrren if the game has already started
        update(mModelSubject, "update");
    }

    public void startGame() {
        try {
            mGUIFacade.startCurrentGame();
        }
        catch (InvalidOperationException ex) {
           mView.DisplayMessage("Got an exception: " + ex.getMessage());
        }
    }

    //when the model updates, this will be called, so it can get the latest changes from the model
    public void update(Observable observable, Object object) {
        String msg = (String) object;
        //if the model updated, it will send a msg update
        if (msg.contains("update")) {
            if (/*mGUIFacade.isGameStarted()*/mGUIFacade.getGameState(mGUIFacade.getCurrentGameID()).isGameStarted()) {
                mModelSubject.detach(this);
                mView.startGameView();
            }
            else {
                mView.updatePlayerList(GUIFacade.instance().getGameState(GUIFacade.instance().getCurrentGameID()));
                mView.DisplayMessage("Got an update");
            }

        } else { //if no update message, then an error was given and must be displayed
            mView.DisplayMessage(msg);
        }

    }
}
