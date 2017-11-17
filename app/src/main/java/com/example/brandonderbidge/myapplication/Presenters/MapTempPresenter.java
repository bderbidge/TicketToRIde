package com.example.brandonderbidge.myapplication.Presenters;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IMapTempPresenter;
import com.example.brandonderbidge.myapplication.Views.IViews.IMapTempView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import Communication.Chat;
import Communication.DestinationCard;
import Communication.History;
import Communication.Player;
import Communication.Route;
import Communication.TrainCard;
import Communication.User;
import Model.CommandFacade;
import Model.GUIFacade;
import Model.GUIFacade_Phase2;
import Model.IGUIFacade;
import Model.ModelRoot;
import Model.ModelSubject;

/**
 * Created by emilyprigmore on 10/28/17.
 */

public class MapTempPresenter implements IMapTempPresenter, Observer {

    private IMapTempView mMapTempView;
    private IGUIFacade mFacade;
    private ModelSubject mModelSubject;
    private int mNumDestinationsReqd;

    public MapTempPresenter(IMapTempView view, IGUIFacade facade){
        mMapTempView = view;
        mFacade = facade;
        mModelSubject = ModelRoot.instance();
        mModelSubject.attach(this);
        mNumDestinationsReqd = 2;   // at start of game it is 2, in game play it is 1
    }

    @Override
    public void updateUIState() {
        isGameOver();
        isFinalRound();
        mMapTempView.initializePlayers(getPlayers());
        mMapTempView.updatePlayerWhosTurn(getPlayerWhoseTurn());
        mMapTempView.updatePlayerTrainCards(getPlayerTrainCards());
        mMapTempView.drawClaimedRoutes(getClaimedRoutes());
        mMapTempView.setPlayer(getPlayer());
        mMapTempView.updatePlayerDestinationCards(mFacade.getMyDestinationCards());
        mMapTempView.updateNumDestDeck(mFacade.getNumberDestinationDeck());
        if(mFacade.getNewDestinationOptions() != null) {
            mMapTempView.startDestinationSelectionView(mNumDestinationsReqd);
        }
        mMapTempView.updateIsMyTurn(isMyTurn());
    }

    @Override
    public List<String> canClaimRoute(Route route) {
        return mFacade.canClaimRoute(route);
    }

    @Override
    public void claimRoute(Route route, String color) {
        mFacade.claimRoute(route, color);
    }

    @Override
    public Map<String, List<Route>> getClaimedRoutes() {
        return mFacade.getAllClaimedRoutes();
    }

    @Override
    public Player getPlayer() {
        return mFacade.getCurrentPlayer();
    }

    @Override
    public Map<String, Integer> getPlayerTrainCards() {
        return mFacade.getMyTrainCards();
    }

    @Override
    public List<Player> getPlayers() {
        return mFacade.getPlayers();
    }

    @Override
    public Player getPlayerWhoseTurn() {
        return mFacade.getPlayerWhoseTurn();
    }

    @Override
    public void drawDestinationCards() {
        mNumDestinationsReqd = 1;
        mFacade.drawNewDestination();
        if (mFacade.getNewDestinationOptions() != null) {
            mMapTempView.startDestinationSelectionView(mNumDestinationsReqd);
        }
    }

    public boolean enableDestBtn() {
        if(mFacade.getNumberDestinationDeck() == 0 || !mFacade.isMyTurn())
            return false;
        else
            return true;
    }

    public boolean enableTrainBtn() {
        if((mFacade.getNumberTrainDeck() == 0 && mFacade.getNumberFaceUpTrains() < 1)
                || !mFacade.isMyTurn())
            return false;
        else
            return true;
    }

    @Override
    public boolean isMyTurn() {
        return mFacade.isMyTurn();
    }

    @Override
    public void endMyTurn() {
        mFacade.endTurn();
        mMapTempView.enableInfoButtons(true);
    }

    private void isGameOver() {
        if(mFacade.isGameOver()) {
            mMapTempView.endGame();
        }
    }

    private void isFinalRound() {
        if(mFacade.isFinalRound()) {
            mMapTempView.displayMessage("THIS IS THE FINAL ROUND!");
        }
    }

    public void refreshViews() {
        updateUIState();
    }

    public void onViewDestroy() {
        mModelSubject.detach(this);
    }

    public void update(Observable observable, Object object) {
        String msg = (String) object;
        if (msg.contains("update")) {
            updateUIState();
        }
        else {
            mMapTempView.displayMessage(msg);
        }
    }

    @Override
    public void runUITests() {
        mMapTempView.displayMessage("Adding a new player");
        Player other = new Player("Opponent", "green");
        other.setNumDestCards(2);
        other.setNumTrainCards(4);
        ModelRoot.instance().getCurrentGame().getCurrentGameState().addPlayer(other);
        ModelRoot.instance().getPlayerOrder().add(other);

        runDestinationTest();
        runRouteTests();
        runPlayerTrainCardsTest();
        runChatHistoryTest();
        runChangePlayerTurnTest();
    }

    private void runChatHistoryTest() {
        mMapTempView.displayMessage("Adding Fake History");
        CommandFacade.instance().addHistory(new History("The Battle of Waterloo occurred in 7815", "Opponent", "green"));
        CommandFacade.instance().addHistory(new History("The Archduke of South Belgium became Supreme Chancellor in 1808", "Opponent", "green"));
        mMapTempView.displayMessage("Adding Fake Chat");
        String un = ModelRoot.instance().getUsername();
        CommandFacade.instance().addChat(new Chat("It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.", un ,GUIFacade.instance().getPlayerColor(un)));
        CommandFacade.instance().addChat(new Chat("Or a stiff drink.", "Opponent", "green"));
        CommandFacade.instance().addChat(new Chat("Quiet Mary", un, GUIFacade.instance().getPlayerColor(un)));
    }

    private void runDestinationTest() {
        mMapTempView.displayMessage("Adding Destination Card. Look at your destinations");
        DestinationCard newCard = new DestinationCard("Seattle", "Salt Lake", 3);
        ModelRoot.instance().getUser().getDestCards().add(newCard);
        int n = ModelRoot.instance().getPlayerInGame(ModelRoot.instance().getUsername()).getNumDestCards();
        ModelRoot.instance().getPlayerInGame(ModelRoot.instance().getUsername()).setNumDestCards(n + 1);
        ModelRoot.instance().notifyObservers();

        mMapTempView.displayMessage("Adding Destination Card for Opponent");
        CommandFacade.instance().updatePlayersDestinationCardNumber("Opponent", 5, GUIFacade_Phase2.instance().getNumberDestinationDeck() - 3);
    }

    private void runRouteTests() {
        /*mMapTempView.displayMessage("Adding claimed routes by user.");
        CommandFacade.instance().addPlayerClaimedRoute(ModelRoot.instance().getUsername(), new Route(true, 4, "Omaha", "Denver", "purple", false, 66, 7));
        CommandFacade.instance().addPlayerClaimedRoute(ModelRoot.instance().getUsername(), new Route(true, 6, "El Paso", "Los Angeles", "black", false, 81, 15));
        CommandFacade.instance().addPlayerClaimedRoute("Opponent", new Route(true, 5, "Atlanta", "Miami", "blue", false, 37, 10));
        CommandFacade.instance().addPlayerClaimedRoute("Opponent", new Route(true, 1, "Seattle", "Portland", "Blank", true, 92, 3));
        CommandFacade.instance().addPlayerClaimedRoute("Opponent", new Route(true, 4, "Kansas City", "Denver", "Black", true, 69, 7));*/
    }

    private void runPlayerTrainCardsTest() {
        mMapTempView.displayMessage("Changing the player's train cards");

        CommandFacade.instance().addTrainCardFromDeck(new TrainCard("yellow"), 4, 17);
        CommandFacade.instance().addTrainCardFromDeck(new TrainCard("wild"), 5, 16);
        CommandFacade.instance().addTrainCardFromDeck(new TrainCard("blue"), 6, 15);

        //CommandFacade.instance().updatePlayersTrainCardNumber("Opponent", 6, GUIFacade_Phase2.instance().getNumberTrainDeck() - 2);
    }

    private void runChangePlayerTurnTest() {
        mMapTempView.displayMessage("Changing player's turn");
        List<Player> players = mFacade.getPlayers();
        if(players.size() > 1) {
            ModelRoot.instance().setPersonTakingTurn(players.get(1));
        }
        mModelSubject.notifyObservers();
    }

}
