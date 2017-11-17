package com.example.brandonderbidge.myapplication.Views.IViews;

import java.util.List;

import Communication.GameState;
import Communication.Player;

/**
 * Created by pbstr on 10/31/2017.
 */

public interface IGameStatusView {

    void updateGameStatus(List<Player> playerList);

    void updatePlayerWithLongestRoute(List<String> playerNames);

    void displayMessage(String msg);
}
