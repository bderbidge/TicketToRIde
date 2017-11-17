package com.example.brandonderbidge.myapplication.Presenters;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IHistoryPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IHistoryView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Communication.History;
import Model.IGUIFacade;
import Model.ModelSubject;

/**
 * Created by pbstr on 10/25/2017.
 */

public class HistoryPresenter implements IHistoryPresenter, Observer {

    //for now, the history presenter does not need to recieve any input from the user, so there are no methods to implement
    private IHistoryView mView;
    private IGUIFacade mGUIFacade;
    private ModelSubject mModelSubject; // TODO: make this an interface

    public HistoryPresenter(IHistoryView view, IGUIFacade GUIFacade, ModelSubject subject) {
        mView = view;
        mGUIFacade = GUIFacade;
        mModelSubject = subject;
        mModelSubject.attach(this);
        updateHistory(); //populates the view with the latest history upon creation
    }

    public void refreshView() {
        updateHistory();
    }

    public void onViewDestroy() {
        mModelSubject.detach(this);
    }

    //when the model updates, this will be called, so it can get the latest changes from the model
    public void update(Observable observable, Object object) {
        String msg = (String) object;
        //if the model updated, it will send a msg update
        if (msg.contains("update")) {
            updateHistory();
            //dont need the message for passoff
            //mView.displayMessage("Got an update in the history presenter");

        } else { //if no update message, then an error was given and must be displayed
            mView.displayMessage(msg);
        }

    }

    private void updateHistory() {
        List<History> updatedHistory = mGUIFacade.getHistory();
        mView.updateHistory(updatedHistory);
    }
}
