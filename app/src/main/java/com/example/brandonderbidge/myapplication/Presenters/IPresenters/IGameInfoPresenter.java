package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

/**
 * Created by pbstr on 10/28/2017.
 */

public interface IGameInfoPresenter {
    void updateUIButtonClicked();

    void refreshView(); //refreshes view with the latest data from the model

    void onViewDestroy(); //prepares the presenter for the view being destroyed like detaching from the model
}
