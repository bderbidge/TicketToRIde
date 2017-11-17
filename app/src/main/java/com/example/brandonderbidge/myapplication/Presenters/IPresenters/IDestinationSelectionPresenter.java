package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

import java.util.List;

import Communication.DestinationCard;

/**
 * Created by pbstr on 10/28/2017.
 */

public interface IDestinationSelectionPresenter {
    void selectDestinationCards(List<DestinationCard> selectedCards,
                                       List<DestinationCard> notSelectedCards, int minNumCards);
    void onViewDestroy();

    void fetchGivenDestinationCards();
}
