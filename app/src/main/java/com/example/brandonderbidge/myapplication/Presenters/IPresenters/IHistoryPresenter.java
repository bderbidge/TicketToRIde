package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

/**
 * Created by pbstr on 10/25/2017.
 */

public interface IHistoryPresenter {
    void refreshView(); //repopulates views with the latest data from the model
    public void onViewDestroy();
    //for now, the history presenter does not need to recieve any input from the user, so there are no methods to implement
}
