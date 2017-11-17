package com.example.brandonderbidge.myapplication.Views.IViews;

import java.util.List;

import Communication.Chat;

/**
 * Created by pbstr on 10/16/2017.
 */

public interface IChatView {

    //public void updateChat(List<Chat> allChat); TODO: uncomment this line when I get the chat models
    public void updateChat(List<Chat> allChat);

    public void displayMessage(String message);
}
