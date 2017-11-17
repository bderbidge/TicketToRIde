package com.example.brandonderbidge.myapplication.Views.IViews;

import java.util.List;
import java.util.Map;

import Communication.Player;

/**
 * Created by emilyprigmore on 11/8/17.
 */

public interface IEndGameView {

    void displayPlayerInfo(List<Player> players);

    void updatePlayersLongestRoad(List<String> playerNames);
}
