package com.example.brandonderbidge.myapplication.Presenters;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IEndGamePresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IEndGameView;

import java.util.List;

import Communication.Player;
import Model.IGUIFacade;
import Model.ModelSubject;

/**
 * Created by emilyprigmore on 11/8/17.
 */

public class EndGamePresenter implements IEndGamePresenter {

    private IEndGameView mView;
    private IGUIFacade mFacade;

    public EndGamePresenter(IEndGameView view, IGUIFacade facade) {
        mView = view;
        mFacade = facade;
    }

    public void updateUI() {
        mView.displayPlayerInfo(getPlayers());
        mView.updatePlayersLongestRoad(updatePlayerLongestRoad());
    }

    public List<Player> getPlayers(){
        return mFacade.getPlayers();
    }

    private List<String> updatePlayerLongestRoad() {
        return mFacade.getPlayerWithLongestRoute();
    }
}
