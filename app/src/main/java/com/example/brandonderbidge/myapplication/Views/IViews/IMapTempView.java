package com.example.brandonderbidge.myapplication.Views.IViews;

import java.util.List;
import java.util.Map;

import Communication.DestinationCard;
import Communication.Player;
import Communication.Route;

/**
 * Created by emilyprigmore on 10/28/17.
 */

// temporary class
public interface IMapTempView {

    void drawClaimedRoutes(Map<String, List<Route>> claims);

    void updateClaimedRoutes(Map<String, List<Route>> claims);

    void updatePlayerWhosTurn(Player player);

    void setPlayer(Player player);

    void updateIsMyTurn(boolean isMyTurn);

    void initializePlayers(List<Player> players);

    void updatePlayerTrainCards(Map<String, Integer> map);

    void updateNumDestDeck(int numInDeck);

    void updatePlayerDestinationCards(List<DestinationCard> cards);

    void startDestinationSelectionView(int numDestsReqd);

    void displayMessage(String msg);

    void enableAllButtons(boolean enabled);

    void enableTrainBtn(boolean enabled);

    void enableDestBtn(boolean enabled);

    void enableInfoButtons(boolean enabled);

    void endGame();
}
