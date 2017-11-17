package com.example.brandonderbidge.myapplication.Views.IViews;

import Communication.TrainCard;

/**
 * Created by emilyprigmore on 10/30/17.
 */

public interface ITrainCardYardView {

    void updateCards(TrainCard[] faceUp, int cardsInDeck);

    void setCanGoBack(boolean canGoBack);

    void destroyView(); //will be called when the presenter receives input (like drawing two traincards) that will send this view back / destroy it

    void displayMessage(String msg);

    void endGame();
}
