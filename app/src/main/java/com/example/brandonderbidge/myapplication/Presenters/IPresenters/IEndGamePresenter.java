package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

import java.util.List;

import Communication.Player;

/**
 * Created by emilyprigmore on 11/8/17.
 */

public interface IEndGamePresenter {

    void updateUI();

    List<Player> getPlayers();
}
