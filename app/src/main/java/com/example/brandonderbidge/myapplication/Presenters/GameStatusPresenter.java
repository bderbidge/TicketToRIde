package com.example.brandonderbidge.myapplication.Presenters;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IGameStatusPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IGameStatusView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Communication.Player;
import Model.IGUIFacade;
import Model.ModelSubject;

/**
 * Created by pbstr on 10/31/2017.
 */

public class GameStatusPresenter implements IGameStatusPresenter, Observer {

    private IGameStatusView mView;
    private IGUIFacade mGUIFacade;
    private ModelSubject mModelSubject; // TODO: make this an interface

    public GameStatusPresenter(IGameStatusView view, IGUIFacade GUIFacade, ModelSubject subject) {
        mView = view;
        mGUIFacade = GUIFacade;
        mModelSubject = subject;
        mModelSubject.attach(this);
        updateViewPlayerList();
        updatePlayerLongestRoad();
    }

    public void refreshView() {
        updateViewPlayerList();
    }

    public void onViewDestroy() {
        mModelSubject.detach(this);
    }

    //when the model updates, this will be called, so it can get the latest changes from the model
    public void update(Observable observable, Object object) {
        String msg = (String) object;
        //if the model updated, it will send a msg update
        if (msg.contains("update")) {
            updateViewPlayerList();
            updatePlayerLongestRoad();
            //dont need the message for passoff
            //mView.displayMessage("Got an update in the game status presenter");

        } else { //if no update message, then an error was given and must be displayed
            mView.displayMessage(msg);
        }

    }

    private void updateViewPlayerList() {
        List<Player> playerList = mGUIFacade.getPlayers();
        mView.updateGameStatus(playerList);
    }

    private void updatePlayerLongestRoad() {
        List<String> playerNames = mGUIFacade.getPlayerWithLongestRoute();
        mView.updatePlayerWithLongestRoute(playerNames);
    }
}
