package com.example.brandonderbidge.myapplication.Presenters;

import android.content.Context;
import android.widget.Toast;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.ITrainCardYardPresenter;
import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardStates.FirstDrawState;
import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardStates.NotMyTurnState;
import com.example.brandonderbidge.myapplication.Presenters.TrainCardYardStates.State;
import com.example.brandonderbidge.myapplication.Views.IViews.ITrainCardYardView;

import java.util.Observable;
import java.util.Observer;

import Communication.TrainCard;
import Model.CommandFacade;
import Model.GUIFacade_Phase2;
import Model.IGUIFacade;
import Model.ModelRoot;
import Model.ModelSubject;

/**
 * Created by emilyprigmore on 10/30/17.
 */

public class TrainCardYardPresenter implements ITrainCardYardPresenter, Observer {

    private ITrainCardYardView mTrainCardYardView;
    private IGUIFacade mFacade;
    private ModelSubject mModelSubject;
    private State mState;

    public TrainCardYardPresenter(ITrainCardYardView view, IGUIFacade facade) {
        mTrainCardYardView = view;
        mFacade = facade;
        mModelSubject = ModelRoot.instance();
        mModelSubject.attach(this);
        if(mFacade.isMyTurn()) {
            mState = new FirstDrawState(this);
            displayMessage("Pick your train cards.");
        } else {
            mState = new NotMyTurnState(this);
        }
    }

    public void updateUIState() {
        TrainCard[] faceUpTrains = mFacade.getFaceUpTrains();
        int numInDeck = mFacade.getNumberTrainDeck();
        mTrainCardYardView.updateCards(faceUpTrains, numInDeck);
        mTrainCardYardView.setCanGoBack(onBackPressed());
        isGameOver();
        isFinalRound();
    }

    private void isGameOver() {
        if(mFacade.isGameOver()) {
            mTrainCardYardView.endGame();
        }
    }

    private void isFinalRound() {
        if(mFacade.isFinalRound()) {
            mTrainCardYardView.displayMessage("THIS IS THE FINAL ROUND!");
        }
    }

    public void setState(State state) {
        mState = state;
    }

    public void endMyTurn() {
        mFacade.endTurn();
    }

    public void drawCard(TrainCard trainCard) {
        mState.drawCard(trainCard);
    }

    public void drawFaceDownCard() {
        mState.drawFaceDownCard();
    }

    public boolean onBackPressed() {
        return mState.onBackPressed();
    }

    public void displayMessage(String msg) {
        mTrainCardYardView.displayMessage(msg);
    }

    public void refreshView() {
        updateUIState();
    }

    //called to remove the view, usually happens when the player has already drawn two cards
    public void destroyView() {
        mTrainCardYardView.destroyView();
    }

    public void onViewDestroy() {
        mModelSubject.detach(this);
    }

    public void update(Observable observable, Object object) {
        String msg = (String) object;
        if (msg.contains("update")) {
            updateUIState();
        }
        else {
            //mMapTempView.displayMessage(msg);
        }
    }

    public void runUITests() {
        mTrainCardYardView.displayMessage("Adding, removing cards in the yard. Updating number of cards in the deck");

        ModelRoot.instance().getCurrentGame().setTrainDeckCount(45);
        mTrainCardYardView.displayMessage("Changing opponent's number of train cards and train yard ");
        TrainCard[] yard = GUIFacade_Phase2.instance().getFaceUpTrains();
        yard[3] = new TrainCard("wild");
        yard[1] = new TrainCard("green");
        CommandFacade.instance().updateFaceUpTrains(yard);
    }
}
