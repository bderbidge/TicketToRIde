package com.example.brandonderbidge.myapplication.Presenters.TrainCardYardStates;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ITrainCardYardPresenter;
import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardPresenter;

import Communication.TrainCard;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class NotMyTurnState extends State {

    public NotMyTurnState (ITrainCardYardPresenter presenter) {
        super(presenter);
    }

    public void drawCard(TrainCard card) {
        mPresenter.displayMessage("Not your turn");
    }

    public void drawFaceDownCard() {
        mPresenter.displayMessage("Not your turn");
    }

    public boolean onBackPressed() {
        return true;
    }
}
