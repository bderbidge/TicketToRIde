package com.example.brandonderbidge.myapplication.Views.IViews;

import java.util.List;

import Communication.History;

/**
 * Created by pbstr on 10/25/2017.
 */

public interface IHistoryView {

    void updateHistory(List<History> history);
    void displayMessage(String msg);
}
