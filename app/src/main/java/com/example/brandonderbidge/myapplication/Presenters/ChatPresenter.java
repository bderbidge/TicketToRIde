package com.example.brandonderbidge.myapplication.Presenters;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IChatPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IChatView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Communication.Chat;
import Model.IGUIFacade;
import Model.ModelSubject;

/**
 * Created by pbstr on 10/16/2017.
 */

public class ChatPresenter implements IChatPresenter, Observer {


    private IChatView mView;
    private IGUIFacade mGUIFacade;
    private ModelSubject mModelSubject; // TODO: make this an interface

    public ChatPresenter(IChatView view, IGUIFacade GUIFacade, ModelSubject subject) {
        mView = view;
        mGUIFacade = GUIFacade;
        mModelSubject = subject;
        mModelSubject.attach(this);
        updateChat();
    }

    public void postChat(String message) {
       mGUIFacade.postChat(message);
    }

    public void refreshView() {
       updateChat();
    }

    public void onViewDestroy() {
        mModelSubject.detach(this);
    }

    //when the model updates, this will be called, so it can get the latest changes from the model
    public void update(Observable observable, Object object) {
        String msg = (String) object;
        //if the model updated, it will send a msg update
        if (msg.contains("update")) {
            updateChat();
            //dont need the message for passoff
            //mView.displayMessage("Got an update in the chat presenter");

        } else { //if no update message, then an error was given and must be displayed
            mView.displayMessage(msg);
        }

    }

    private void updateChat() {
        List<Chat> allChat = mGUIFacade.getChat();
        mView.updateChat(allChat);
    }

}
