package com.example.brandonderbidge.myapplication.Presenters;

import android.graphics.ColorSpace;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IDestinationSelectionPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IDestinationSelectionView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Communication.DestinationCard;
import Model.GUIFacade;
import Model.IGUIFacade;
import Model.ModelRoot;
import Model.ModelSubject;

/**
 * Created by pbstr on 10/29/2017.
 */

public class DestinationSelectionPresenter implements IDestinationSelectionPresenter, Observer {

    private IDestinationSelectionView mView;
    private IGUIFacade mGUIFacade;
    private ModelSubject mModelSubject;

    public DestinationSelectionPresenter(IDestinationSelectionView view, IGUIFacade GUIFacade, ModelSubject subject) {
        mView = view;
        mGUIFacade = GUIFacade;
        mModelSubject = subject;
        mModelSubject.attach(this);
        fetchGivenDestinationCards();
    }

    public void fetchGivenDestinationCards() {
        List<DestinationCard> givenCards = mGUIFacade.getNewDestinationOptions();
        mView.updateDestinationCards(givenCards);
    }

    public void selectDestinationCards(List<DestinationCard> selectedCards,
                                       List<DestinationCard> notSelectedCards, int minNumCards) {
        //give cards to facade, not sure how though yet
        mGUIFacade.throwAwayDestination(selectedCards, notSelectedCards);
        if(minNumCards == 1) {  // 1 means game play (reqd to take 1), 2 means start game
            mGUIFacade.endTurn();
        }
        //mModelSubject.detach(this); //TODO: remove this line, replace with correct code for selecting destination cards
    }

    public void onViewDestroy() {
       mModelSubject.detach(this);
    }

    //when the model updates, this will be called, so it can get the latest changes from the model
    public void update(Observable observable, Object object) {
        String msg = (String) object;
        if (msg.contains("update")) {
            if (mGUIFacade.getNewDestinationOptions() != null)
                mModelSubject.detach(this);
        } else { // there was an error
            mView.displayMessage(msg);
        }

    }
}
