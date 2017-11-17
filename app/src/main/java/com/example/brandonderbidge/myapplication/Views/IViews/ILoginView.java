package com.example.brandonderbidge.myapplication.Views.IViews;

import Communication.GamesList;

/**
 * Created by pbstr on 9/28/2017.
 */

public interface ILoginView {

    /**
     * returns the username for login
     */
    String getLoginUserName();
    /**
     * returns the password for login
     */
    String getLoginPassword();
    /**
     * returns the username for registering
     */
    String getRegisterUserName();
    /**
     * returns the password for registering
     */
    String getRegisterPassword();
    /**
     * returns the confirming password for registering
     */
    String getRegisterPasswordConfirm();
    /**
     * starts the gameLobbyView with the gamesList of available games
     * @param gamesList the available games to be displayed in the gameSelectionView
     */
    void StartJoinGameView(GamesList gamesList);
    /**
     * displays a message to the screen
     * @param msg the message to be displayed on the UI
     */
    void DisplayMessage(String msg);

}
