package com.example.brandonderbidge.myapplication.Presenters.TrainCardYardStates;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ITrainCardYardPresenter;
import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardPresenter;

import Communication.TrainCard;
import Model.GUIFacade;
import Model.IGUIFacade;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public abstract class State {

    protected ITrainCardYardPresenter mPresenter;
    IGUIFacade mFacade = GUIFacade.instance();

    public State(ITrainCardYardPresenter presenter) {
        mPresenter = presenter;
    }

    public State() {}

    abstract public void drawCard(TrainCard trainCard);

    abstract public void drawFaceDownCard();

    abstract public boolean onBackPressed();
}
