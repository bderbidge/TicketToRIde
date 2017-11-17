package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

import android.content.Context;

import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardStates.State;

import Communication.TrainCard;

/**
 * Created by emilyprigmore on 10/30/17.
 */

public interface ITrainCardYardPresenter {

    void drawCard(TrainCard trainCard);

    void drawFaceDownCard();

    void endMyTurn();

    void setState(State state);

    boolean onBackPressed();

    void displayMessage(String msg);

    void updateUIState();

    void refreshView();

    void destroyView();

    void onViewDestroy();

    void runUITests();
}
