package com.example.brandonderbidge.myapplication.Presenters;


import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameSelectionPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameSelectionView;

import java.util.Observable;
import java.util.Observer;

import Communication.Game;
import Communication.GameState;
import Model.GUIFacade;
import Model.IGUIFacade;
import Model.ModelRoot;
import Model.ModelSubject;

/**
 * Created by pbstr on 10/6/2017.
 */

public class GameSelectionPresenter implements IGameSelectionPresenter, Observer {

    private IGameSelectionView mView;
    private IGUIFacade mGUIFacade;
    private ModelSubject mModelSubject; // TODO: make this an interface

    public GameSelectionPresenter(IGameSelectionView view, IGUIFacade facade) {
        mView = view;
        mGUIFacade = facade;
        mModelSubject = ModelRoot.instance(); //TODO: make this an interface and have it passed in the constructor
        mModelSubject.attach(this);

        //this will skip scrren if the user has already joined a game
        update(mModelSubject, "update");
    }

    public void joinGame(String gameId, String color)
    {
        //sends info to the GUIFacade to handle it in the model layer
        mGUIFacade.joinGame(gameId, color);
    }

    public void createGame(String gameName, int numPlayers)
    {
        //sends info to the GUIFacade to handle it in the model layer
        mGUIFacade.createGame(gameName, numPlayers);
    }

    public Game getGame(String gameID) {
        return mGUIFacade.getGamesList().getOpenGames().get(gameID);
    }

    public void update(Observable observable, Object object) {

        String msg = (String) object;
        if (msg.contains("update")) {
            if (GUIFacade.instance().isGameJoined() == ModelRoot.GAMEJOIN.JOINED) {
                //TODO: i should not access the Modoel Root, I should only go thorugh GUIFacade, but there is not function to get current gameState
                mModelSubject.detach(this);
                mView.startGameLobbyView(GUIFacade.instance().getGameState(ModelRoot.instance().getCurrentGameID()));
            }
            else {
                mView.updateAvailableGames(GUIFacade.instance().getGamesList());
                mView.DisplayMessage("Got an update from server model");
            }


        } else {
            mView.DisplayMessage(msg);
        }

    }

}
