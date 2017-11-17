package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

import Communication.Game;
import Communication.GameState;

/**
 * Created by pbstr on 10/6/2017.
 */

public interface IGameSelectionPresenter {


    /**
     * notfiies the presenter that the create game button has been pressed, passing in the parameters that have been passed
     *
     * @param gameName the name of the game to be created
     * @param numPlayers the max number of players for the game
     */
    public void createGame(String gameName, int numPlayers);


    /**
     * notifies the presenter that a game has been selected to be joined
     *
     * @param gameId the id of the game to be joined
     * @param color the color the player selected for joining the game
     */
    public void joinGame(String gameId, String color);

    /**
     * This retuns the gameStaste of the selected game so that the colors can be populated correctly
     * @param gameID
     * @return
     */
    Game getGame(String gameID);
}
