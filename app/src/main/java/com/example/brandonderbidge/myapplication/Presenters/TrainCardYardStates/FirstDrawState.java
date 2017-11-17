package com.example.brandonderbidge.myapplication.Presenters.TrainCardYardStates;


import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ITrainCardYardPresenter;
import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardPresenter;

import Communication.TrainCard;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class FirstDrawState extends State {

    public FirstDrawState(ITrainCardYardPresenter presenter) {
        super(presenter);
    }

    public void drawCard(TrainCard card) {
        mFacade.drawFaceUpTrain(card);
        if (card.getColor().toLowerCase().equals("wild")) {
            mPresenter.endMyTurn();
            mPresenter.setState(new NotMyTurnState(mPresenter));
            mPresenter.destroyView();
        } else {
            mPresenter.setState(new SecondDrawState(mPresenter));
        }
    }

    public void drawFaceDownCard() {
        mFacade.drawFaceDownTrain();
        mPresenter.setState(new SecondDrawState(mPresenter));
    }

    public boolean onBackPressed() {
        return true; //we want users to be able to view the trainyard without necessarily drawing cards
    }
}
