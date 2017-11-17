package com.example.brandonderbidge.myapplication.Views.IViews;

import Communication.GameState;

/**
 * Created by pbstr on 10/6/2017.
 */

public interface IGameLobbyView {

    /**
     * updates the gameLobby with the latest gamestate, which contains the playerlist
     * @param gameState the current gameState of the game, which contains the playerList
     */
    public void updatePlayerList(GameState gameState);

    /**
     * tells the view to start the game view
     */
    public void startGameView();

    /**
     * displays a message to the screen
     * @param msg the message to be displayed on the UI
     */
    public void DisplayMessage(String msg);
}
