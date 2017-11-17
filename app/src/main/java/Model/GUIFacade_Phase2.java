package Model;

import java.util.List;
import java.util.Map;

import Command.Command;
import Command.CreateGameCommandData;
import Command.JoinGameCommandData;
import Communication.Chat;
import Communication.DestinationCard;
import Communication.Game;
import Communication.GameState;
import Communication.GamesList;
import Communication.History;
import Communication.IServer;
import Communication.Player;
import Communication.Route;
import Communication.ServerProxy;
import Communication.TrainCard;
import Communication.User;
import Command.StartGameRequestCommandData;
import Command.ChatRequestCommandData;
import Model.Exceptions.InvalidOperationException;
import Model.Exceptions.PasswordMismatchException;

/**
 * Created by emmag on 9/27/2017.
 */

public class GUIFacade_Phase2 implements IGUIFacade {
    private static IGUIFacade mInstance = new GUIFacade_Phase2();
    private ModelRoot mModelRoot;
    private IServer mServerProxy;


    /**
     * private constructor for singleton pattern
     */
    private GUIFacade_Phase2() {
        mModelRoot = ModelRoot.instance();
        mServerProxy = new ServerProxy();
    }

    /**
     * gets instance of class using singleton pattern
     * @return GUIFacade
     */
    public static IGUIFacade instance() {
        return mInstance;
    }

    @Override
    public void setServerInfo(String serverIP, String serverPort) {
        GUIFacade.instance().setServerInfo(serverIP, serverPort);
    }
    /**
     * registers a user by sending a request to the server via Server Proxy
     * @param username
     * @param password
     * @param passwordRepeat
     * @throws InvalidOperationException
     * @throws PasswordMismatchException
     */
    @Override
    public void register(String username, String password, String passwordRepeat) throws InvalidOperationException, PasswordMismatchException {
        //just use the normal GUIFacades functionality
        GUIFacade.instance().register(username, password, passwordRepeat);
    }

    /**
     * logs in a user by sending a request to the server via the Server Proxy
     * @param username
     * @param password
     * @throws InvalidOperationException
     */
    @Override
    public void login(String username, String password) throws InvalidOperationException {
        //use normal guifacades functionality for now
        GUIFacade.instance().login(username, password);
    }

    /**
     * check to see if the user is logged in
     * @return boolean
     */
    @Override
    public boolean isLoggedIn() {
        //use normal guifacades functionality for now
        return GUIFacade.instance().isLoggedIn();
    }

    @Override
    public boolean isGameStarted() {
        return GUIFacade.instance().isGameStarted();
    }

    @Override
    public void throwAwayDestination(List<DestinationCard> toKeep, List<DestinationCard> toDitch) {
        GUIFacade.instance().throwAwayDestination(toKeep, toDitch);
    }

    //Create game stuff

    /**
     * get the current games list from the model
     * @return GamesList
     */
    @Override
    public GamesList getGamesList() {
        //use normal guifacades functionality for now
        return GUIFacade.instance().getGamesList();
    }

    /**
     * get a specific game from the GamesList
     * @param gameID
     * @return Game
     */
    @Override
    public Game queryGamesList(String gameID) {
        //use normal guifacade's functionality for now
        return GUIFacade.instance().queryGamesList(gameID);
    }

    /**
     * create a game by sending a createGameCommand to Server Proxy
     * @param gameName
     * @param numberOfPlayers
     */
    @Override
    public void createGame(String gameName, int numberOfPlayers) {
        //use normal guifacade's functionality for now
        GUIFacade.instance().createGame(gameName, numberOfPlayers);
    }

    /**
     * get the gamestate of a specified game
     * @param gameID
     * @return GameState
     */
    @Override
    public GameState getGameState(String gameID) {
        //use normal guifacade's functionality for now
        return GUIFacade.instance().getGameState(gameID);
    }

    //join game stuff

    /**
     * join the specified game in the said color via a joinGameCommand to the Server Proxy
     * @param gameID
     * @param playerColor
     */
    @Override
    public void joinGame(String gameID, String playerColor) {
        //use normal guifacade's functionality for now
        GUIFacade.instance().joinGame(gameID, playerColor);
    }

    /**
     * get the player's game state
     * @return GAMEJOIN enum specifying if a player is in no game, pending on a join game request, or succesfully in a game
     */
    @Override
    public ModelRoot.GAMEJOIN isGameJoined() {
        return GUIFacade.instance().isGameJoined();
    }

    /**
     * start the current game via a startGameCommand to the server proxy
     * @throws InvalidOperationException
     */
    @Override
    public void startCurrentGame() throws InvalidOperationException {
        //use normal guifacade's functionality for now
        GUIFacade.instance().startCurrentGame();
    }

    /**
     * get the current game state version
     * if the player is not in a game, -1 will be returned
     * @return int gameVersion number
     */
    @Override
    public int getCurrentGameVersion() {
        //use normal guifacade's functionality for now
        return GUIFacade.instance().getCurrentGameVersion();
    }

    /**
     * get the id of the current game, returns null if in no game
     * @return string game id
     */
    @Override
    public String getCurrentGameID() {
        //use normal guifacade's functionality for now
        return GUIFacade.instance().getCurrentGameID();
    }

    /**
     * gets the user's authToken
     * @return string
     */
    @Override
    public String getAuthToken() {
        //use normal guifacade's functionality for now
        return GUIFacade.instance().getAuthToken();
    }


    //PHASE 2


    @Override
    public void drawNewDestination() {
    }

    @Override
    public void drawFaceDownTrain() {
        //fake code for phase 2

    }

    @Override
    public void drawFaceUpTrain(TrainCard trainCard) {

    }

    @Override
    public List<String> canClaimRoute(Route route) {
        return GUIFacade.instance().canClaimRoute(route);
    }

    @Override
    public void claimRoute(Route route, String color) {
        GUIFacade.instance().claimRoute(route, color);
    }


    @Override
    public void postChat(String message) {
        Chat chat = new Chat(message, mModelRoot.getUser().getUsername(), mModelRoot.getPlayer().getColor());
        Command command = new ChatRequestCommandData(mModelRoot.getAuthToken(), chat, mModelRoot.getCurrentGameID());
        mServerProxy.command(command);
    }

    @Override
    public String getPlayerColor(String username) {
        return GUIFacade.instance().getPlayerColor(username);
    }

    @Override
    public User getUser() {
        return GUIFacade.instance().getUser();
    }

    @Override
    public Player getCurrentPlayer() {
        return GUIFacade.instance().getCurrentPlayer();
    }

    @Override
    public List<Player> getPlayers() {
        return GUIFacade.instance().getPlayers();
    }

    @Override
    public List<Chat> getChat() {
        return GUIFacade.instance().getChat();
    }

    @Override
    public List<History> getHistory() {
        return GUIFacade.instance().getHistory();
    }

    @Override
    public int getNumberTrainDeck() {
        return GUIFacade.instance().getNumberTrainDeck();
    }

    @Override
    public int getNumberDestinationDeck() {
        return GUIFacade.instance().getNumberDestinationDeck();
    }

    @Override
    public Map<String, Integer> getMyTrainCards() {
        return GUIFacade.instance().getMyTrainCards();
    }

    @Override
    public List<DestinationCard> getMyDestinationCards() {
        return GUIFacade.instance().getMyDestinationCards();
    }

    @Override
    public List<DestinationCard> getNewDestinationOptions() {
        return GUIFacade.instance().getNewDestinationOptions();
    }

    @Override
    public TrainCard[] getFaceUpTrains() {
        return GUIFacade.instance().getFaceUpTrains();
    }

    @Override
    public int getNumberFaceUpTrains() {
        return GUIFacade.instance().getNumberFaceUpTrains();
    }

    @Override
    public Player getPlayerWhoseTurn() {
        return GUIFacade.instance().getPlayerWhoseTurn();
    }

    @Override
    public boolean isMyTurn() {
        return mModelRoot.isMyTurn();
    }

    @Override
    public boolean isFinalRound() {
        return GUIFacade.instance().isFinalRound();
    }

    @Override
    public boolean isGameOver() {
        return GUIFacade.instance().isGameOver();
    }

    @Override
    public void endTurn() {
        GUIFacade.instance().endTurn();
    }

    @Override
    public Map<String, List<Route>> getAllClaimedRoutes() {
        return mModelRoot.getAllClaimedRoutes();
    }

    @Override
    public Map<String, Route> getNewClaimedRoutes() {
        return mModelRoot.getNewClaimedRoutes();
    }

    @Override
    public void clearNewClaimedRoutes() {
        GUIFacade.instance().clearNewClaimedRoutes();
    }

    @Override
    public List<String> getPlayerWithLongestRoute() {
        return GUIFacade.instance().getPlayerWithLongestRoute();
    }


    /*@Override
    public void clearNewClaimedRoutes() {
        mModelRoot.clearNewClaimedRouteIDs();
    }
    */

}

