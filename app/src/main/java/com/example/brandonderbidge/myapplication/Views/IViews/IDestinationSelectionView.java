package com.example.brandonderbidge.myapplication.Views.IViews;

import java.util.List;

import Communication.DestinationCard;

/**
 * Created by pbstr on 10/28/2017.
 */

public interface IDestinationSelectionView {

    public void updateDestinationCards(List<DestinationCard> availableCards);
    public void displayMessage(String msg);
}
