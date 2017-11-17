package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

import java.io.Serializable;

/**
 * Created by pbstr on 10/16/2017.
 */

public interface IChatPresenter extends Serializable{

    public void postChat(String message);

    void refreshView(); //repopulates the view with the latest data from the model

    public void onViewDestroy(); //prepares the presenter for the view being destroyed, like detaching from the model
}
