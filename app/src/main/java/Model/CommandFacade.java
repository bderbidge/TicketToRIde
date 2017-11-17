package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Communication.Chat;
import Communication.DestinationCard;
import Communication.Game;
import Communication.GameState;
import Communication.GamesList;
import Communication.History;
import Communication.IClient;
import Communication.Player;
import Communication.Route;
import Communication.TrainCard;
import Communication.User;
import RequestResult.LoginRegisterResult;
import Poller.Poller;

/**
 * Created by emmag on 9/30/2017.
 * Facade for Client Model for calls from Commands and other server responses
 */

public class CommandFacade implements IClient, ICommandFacade {

    private static CommandFacade mInstance = new CommandFacade();
    private ModelRoot mModelRoot;

    private CommandFacade() {
        mModelRoot = ModelRoot.instance();
    }

    public static CommandFacade instance() {
        return mInstance;
    }

    /**
     * updates the model's game list
     * @param gamesList list to update to
     */
    @Override
    public void updateGamesList(GamesList gamesList) {
        mModelRoot.setGamesList(gamesList);
        //notify observers of change in game list state
        mModelRoot.notifyObservers();
    }

    /**
     * updates the specified game's state with new game state
     * @param gameID game to be updated
     * @param gameState state to update game to
     */
    @Override
    public void updateGameState(String gameID, GameState gameState) {
        //if i in the game or trying to get in the game
        if(mModelRoot.getCurrentGameID() != null && mModelRoot.getCurrentGameID().equals(gameID)) {
            //if I am trying to get in the game
            if( mModelRoot.getInGame() == ModelRoot.GAMEJOIN.PENDING) {
                //check if I got in the game
                if(gameState.isPlayerInGame(mModelRoot.getUsername())) {
                    //confirm I got in
                    mModelRoot.setInGame(ModelRoot.GAMEJOIN.JOINED);
                    //set my current game to be this one
                    mModelRoot.setCurrentGameID(gameID);
                    Game gameToUpdate = mModelRoot.getGamesList().queryOpenGames(gameID);
                    gameToUpdate.setCurrentGameState(gameState);
                    mModelRoot.setCurrentGame(gameToUpdate);
                    //update my player
                    mModelRoot.setPlayer(gameState.getPlayer(mModelRoot.getUsername()));
                }
                else {
                    //set that I did not get in
                    mModelRoot.setInGame(ModelRoot.GAMEJOIN.NO_GAME);
                    //reset my currentGameID
                    mModelRoot.setCurrentGameID(null);
                    //reset my player
                    mModelRoot.setPlayer(null);
                }
            }
            else { //I am already in the game
                mModelRoot.getCurrentGame().setCurrentGameState(gameState);
            }
        }
        //I am not actively in the game or trying to get in
        else {
            Game gameToUpdate = mModelRoot.getGamesList().queryOpenGames(gameID);
            //if i am in the game but don't have it recorded
            if(gameState.isPlayerInGame(mModelRoot.getUsername())) {
                //tell myself i am in a game
                mModelRoot.setInGame(ModelRoot.GAMEJOIN.JOINED);
                //set my current game to be this one
                mModelRoot.setCurrentGameID(gameID);
                gameToUpdate.setCurrentGameState(gameState);
                mModelRoot.setCurrentGame(gameToUpdate);
                //update my player
                mModelRoot.setPlayer(gameState.getPlayer(mModelRoot.getUsername()));
            }
            //i am not in the game
            else {
                if(gameState.isGameStarted()) {
                    //the game started so remove it from the games list
                    mModelRoot.getGamesList().removeGame(gameID);
                }
                else {
                    //something happened to the game, such as someone joining it
                    mModelRoot.getGamesList().updateSpecifiedGameState(gameID, gameState);
                }
            }
        }
        //notify the observers of changes
        mModelRoot.notifyObservers();
    }

    /**
     * set the state of the model's current selected game
     * @param gameState new game state
     */
    private void setCurrentGameState(GameState gameState) {
        //update current game's state
        mModelRoot.getCurrentGame().setCurrentGameState(gameState);
        mModelRoot.notifyObservers();
    }

    /**
     * updates the model based on the login or register result from the Server
     * @param loginRegisterResult
     */
    @Override
    public void resultReturn(LoginRegisterResult loginRegisterResult) {
        //check to see if you correctly logged in
        if(loginRegisterResult.isSuccess()) {
            //set the authToken and user from the response
            mModelRoot.setAuthToken(loginRegisterResult.getAuthToken());
            mModelRoot.setUser(loginRegisterResult.getUser());
            mModelRoot.setUsername(loginRegisterResult.getUser().getUsername());
            //notify the observers of changes
            mModelRoot.notifyObservers();
            //create a poller
            new Poller();
        }
        else {
            //if not notify of an error
            notifyOfCommandError(loginRegisterResult.getMessage());
        }

    }

    /**
     * notify the observers of the specified error
     * @param errorMessage
     */
    @Override
    public void notifyOfCommandError(String errorMessage) {
        mModelRoot.notifyObserversOfError(errorMessage);
    }

    @Override
    public void updateMyPlayer(Player player) {
        mModelRoot.setPlayer(player);
    }

    /*@Override
    public boolean updatePlayerInfo(String username, int points, int numCards, Set<Route> claimedRoutes) {
        return false;
    }*/

    @Override
    public void startGame(User filledUser, Game filledGame) {
        if(mModelRoot.getCurrentGame() != null) {
            //make sure i am in the game
            mModelRoot.setGameStarted(true);
            if (filledUser != null) {
                if (filledUser.getUsername().equals(mModelRoot.getUsername())) {
                    mModelRoot.setDestinationOptions(filledUser.getDestCards());
                    filledUser.setDestinationCards(new ArrayList<DestinationCard>());
                    mModelRoot.setUser(filledUser);
                }
            }
            if (filledGame != null) {
                if (filledGame.getGameID().equals(mModelRoot.getCurrentGame().getGameID())) {
                    mModelRoot.setCurrentGame(filledGame);
                    mModelRoot.getCurrentGame().getCurrentGameState().rebootCurrentPlayers();
                    mModelRoot.setPlayer(filledGame.getCurrentGameState().getPlayer(mModelRoot.getUsername()));
                    mModelRoot.setPlayerOrder(filledGame.getCurrentGameState().getOrderedPlayers());
                    //mModelRoot.setPersonTakingTurn(filledGame.getCurrentGameState().getPlayerWhoseTurn());
                    
                    startNextPlayersTurn(filledGame.getCurrentGameState().getPlayerWhoseTurn().getUsername());
                    updateMyPlayer(filledGame.getCurrentGameState().getPlayer(mModelRoot.getUsername()));
                }
            }
            mModelRoot.notifyObservers();
        }
    }

    @Override
    public void setDestinationOptions(List<DestinationCard> drawnDestinations, int destDeckCount) {
        mModelRoot.setDestinationOptions(drawnDestinations);
        mModelRoot.getCurrentGame().setDestinationDeckCount(destDeckCount);
        mModelRoot.notifyObservers();
    }

    @Override
    public void finishRemovingDestinations(int myDestCount, int destDeckCount) {
        mModelRoot.addDestinationOptionsToMyDestinations();
        mModelRoot.getCurrentGame().setDestinationDeckCount(destDeckCount);
        mModelRoot.getPlayerInGame(mModelRoot.getUsername()).setNumDestCards(myDestCount);
        mModelRoot.setDestinationOptions(null);
        mModelRoot.notifyObservers();
    }

    @Override
    public void addMyRoute(Route route, List<TrainCard> updatedHand, int deckCardCount, List<String> longestPlayer) {
        mModelRoot.getUser().setTrainCards(updatedHand);
        mModelRoot.calculateMyTrainCardCounts();
        addPlayerClaimedRoute(mModelRoot.getUsername(), route, deckCardCount, longestPlayer);
    }

    @Override
    public void addPlayerClaimedRoute(String username, Route route, int deckCardCount, List<String> longestPlayer) {
        Player player = mModelRoot.getPlayerInGame(username);
        player.claimRoute(route);
        route.setClaimed(true);
        mModelRoot.addClaimedRoute(username, route);
        mModelRoot.getCurrentGame().setTrainDeckCount(deckCardCount);
        mModelRoot.getCurrentGame().getCurrentGameState().setLongestUser(longestPlayer);
        mModelRoot.notifyObservers();
    }


    @Override
    public void addChat(Chat chat) {
        mModelRoot.addChat(chat);
        mModelRoot.notifyObservers();
    }

    @Override
    public void addHistory(History history) {
        mModelRoot.addHistory(history);
        mModelRoot.notifyObservers();
    }


    @Override
    public void updateFaceUpTrains(TrainCard[] faceUpTrains) {
        mModelRoot.getCurrentGame().setUpDeck(faceUpTrains);
        mModelRoot.notifyObservers();
    }


    @Override
    public void addTrainCardFromDeck(TrainCard trainCard, int userCount, int deckCount) {
        mModelRoot.getUser().addTrainCard(trainCard);
        Player player = mModelRoot.getPlayerInGame(mModelRoot.getUsername());
        player.setNumTrainCards(userCount);
        mModelRoot.getCurrentGame().setTrainDeckCount(deckCount);
        mModelRoot.calculateMyTrainCardCounts();
        mModelRoot.notifyObservers();
    }

    @Override
    public void addTrainCardFromYard(TrainCard trainCard, int userCount, int downDeckCount, TrainCard[] updatedYard) {
        mModelRoot.getUser().addTrainCard(trainCard);
        Player player = mModelRoot.getPlayerInGame(mModelRoot.getUsername());
        player.setNumTrainCards(userCount);
        mModelRoot.getCurrentGame().setTrainDeckCount(downDeckCount);
        mModelRoot.getCurrentGame().setUpDeck(updatedYard);
        mModelRoot.calculateMyTrainCardCounts();
        mModelRoot.notifyObservers();
    }


    //update other players


    @Override
    public void updatePlayersTrainCardFromDeck(String username, int numberOfTrainCards, int trainDeckCount) {
        Player player = mModelRoot.getPlayerInGame(username);
        player.setNumTrainCards(numberOfTrainCards);
        mModelRoot.getCurrentGame().setTrainDeckCount(trainDeckCount);
        mModelRoot.notifyObservers();
    }

    @Override
    public void updatePlayersTrainCardFromTrainYard(String username, int numberOfTrainCards, int trainDeckCount, TrainCard[] yard) {
        Player player = mModelRoot.getPlayerInGame(username);
        player.setNumTrainCards(numberOfTrainCards);
        mModelRoot.getCurrentGame().setUpDeck(yard);
        mModelRoot.getCurrentGame().setTrainDeckCount(trainDeckCount);
        mModelRoot.notifyObservers();
    }

    @Override
    public void updatePlayersDestinationCardNumber(String username, int numberOfDestinationCards, int destDeckCount) {
        Player player = mModelRoot.getPlayerInGame(username);
        player.setNumDestCards(numberOfDestinationCards);
        mModelRoot.getCurrentGame().setDestinationDeckCount(destDeckCount);
        mModelRoot.notifyObservers();
    }


    @Override
    public void startNextPlayersTurn(String username) {
        mModelRoot.setPersonTakingTurn(mModelRoot.getPlayerInGame(username));
        if(mModelRoot.getPersonTakingTurn()== null || mModelRoot.getPersonTakingTurn().getUsername().equals(mModelRoot.getUsername())){
            mModelRoot.setMyTurn(true);
        }
        else {
            mModelRoot.setMyTurn(false);
        }
        mModelRoot.notifyObservers();
    }

    @Override
    public void startLastRound() {
        mModelRoot.setFinalRound(true);
        mModelRoot.notifyObservers();
    }

    @Override
    public void endGame(List<Player> finalPlayers) {
        mModelRoot.setGameOver(true);
        for (Player p : finalPlayers) {
            updatePlayerPoints(p);
        }
        mModelRoot.notifyObservers();
    }

    private void updatePlayerPoints(Player player) {
        Player existing = mModelRoot.getPlayerInGame(player.getUsername());
        existing.setRoutePoints(player.getRoutePoints());
        existing.setDestPoints(player.getDestPoints());
        existing.setNegDestPoints(player.getNegDestPoints());
        //existing.setLongestRoutePoints(player.getLongestRoutePoints());
        existing.setTotalPoints(player.getTotalPoints());
    }


    /**
     * exception thrown in login or register is unsuccessful
     */
    public class BadLoginRegisterException extends Exception {
        private String mMessage;
        public BadLoginRegisterException(String message) {
            mMessage = message;
        }
        public String getMessage() {
            return mMessage;
        }
    }
}
