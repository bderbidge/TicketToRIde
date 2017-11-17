package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

/**
 * Created by pbstr on 10/31/2017.
 */

public interface IGameStatusPresenter {

    void refreshView(); //refresh the views with the latest data from the mode

    void onViewDestroy();
}
