package com.example.brandonderbidge.myapplication.Presenters.IPresenters;

import android.content.Context;

import java.util.List;
import java.util.Map;

import Communication.Player;
import Communication.Route;

/**
 * Created by emilyprigmore on 10/28/17.
 */

public interface IMapTempPresenter {

    void updateUIState();

    void claimRoute(Route route, String color);

    List<Player> getPlayers();

    Map<String, List<Route>> getClaimedRoutes();

    Map<String, Integer> getPlayerTrainCards();

    Player getPlayer();

    Player getPlayerWhoseTurn();

    void drawDestinationCards();

    void endMyTurn();

    void refreshViews(); //re-updates the ui with data from the model

    void onViewDestroy(); //does all functionality that must be done before the presenters view is
    // destroyed like detatching from the modelsubject

    void runUITests();

    boolean isMyTurn();

    List<String> canClaimRoute(Route route);

    boolean enableDestBtn();

    boolean enableTrainBtn();
}

