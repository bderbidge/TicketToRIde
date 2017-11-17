package com.example.brandonderbidge.myapplication.Presenters.TrainCardYardStates;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ITrainCardYardPresenter;
import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardPresenter;

import Communication.TrainCard;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class SecondDrawState extends State {

    public SecondDrawState (ITrainCardYardPresenter presenter) {
        super(presenter);
    }

    public void drawCard(TrainCard card) {
        if(card.getColor().toLowerCase().equals("wild")) {
            mPresenter.displayMessage("You cannot take that card.");
        }
        else {
            mFacade.drawFaceUpTrain(card);
            drawCardHelper();
        }
    }

    public void drawFaceDownCard() {
        mFacade.drawFaceDownTrain();
        drawCardHelper();
    }

    private void drawCardHelper() {
        mPresenter.endMyTurn();
        mPresenter.setState(new NotMyTurnState(mPresenter));
        mPresenter.destroyView(); //go back to the main map view

    }

    public boolean onBackPressed() {
        return false;
    }
}
