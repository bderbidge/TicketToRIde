package com.example.brandonderbidge.myapplication.Views.IViews;

import Communication.GameState;
import Communication.GamesList;

/**
 * Created by pbstr on 10/6/2017.
 */

public interface IGameSelectionView {

    /**
     * updates the view with the latest available games
     * @param currentGames the GamesList object that has all the details of all the available games
     */
    public void updateAvailableGames(GamesList currentGames);

    /**
     * starts the gameLobby view with the gameState given
     * @param gameState the gameState of the game to be started, includes the list of players
     */
    public void startGameLobbyView(GameState gameState);
    /**
     * displays a message to the screen
     * @param msg the message to be displayed on the UI
     */
    public void DisplayMessage(String msg);
}
