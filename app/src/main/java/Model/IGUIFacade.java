package Model;

import java.util.List;
import java.util.Map;

import Communication.Chat;
import Communication.DestinationCard;
import Communication.Game;
import Communication.GameState;
import Communication.GamesList;
import Communication.History;
import Communication.Player;
import Communication.Route;
import Communication.TrainCard;
import Communication.User;
import Model.Exceptions.InvalidOperationException;
import Model.Exceptions.PasswordMismatchException;

/**
 * Created by emmag on 10/9/2017.
 */

public interface IGUIFacade {

    void setServerInfo(String serverIP, String port);

    /**
     * registers a user by sending a request to the server via Server Proxy
     * @param username
     * @param password
     * @param passwordRepeat
     * @throws InvalidOperationException
     * @throws PasswordMismatchException
     */
    public void register(String username, String password, String passwordRepeat) throws InvalidOperationException, PasswordMismatchException;

    /**
     * logs in a user by sending a request to the server via the Server Proxy
     * @param username
     * @param password
     * @throws InvalidOperationException
     */
    public void login(String username, String password) throws InvalidOperationException;

    /**
     * check to see if the user is logged in
     * @return boolean
     */
    public boolean isLoggedIn();

    /**
     * get the current games list from the model
     * @return GamesList
     */
    public GamesList getGamesList();

    /**
     * get a specific game from the GamesList
     * @param gameID
     * @return Game
     */
    public Game queryGamesList(String gameID);

    /**
     * create a game by sending a createGameCommand to Server Proxy
     * @param gameName
     * @param numberOfPlayers
     */
    public void createGame(String gameName, int numberOfPlayers);

    /**
     * get the gamestate of a specified game
     * @param gameID
     * @return GameState
     */
    public GameState getGameState(String gameID);

    /**
     * join the specified game in the said color via a joinGameCommand to the Server Proxy
     * @param gameID
     * @param playerColor
     */
    public void joinGame(String gameID, String playerColor);

    /**
     * get the player's game state
     * @return GAMEJOIN enum specifying if a player is in no game, pending on a join game request, or succesfully in a game
     */
    public ModelRoot.GAMEJOIN isGameJoined();

    /**
     * start the current game via a startGameCommand to the server proxy
     * @throws InvalidOperationException
     */
    public void startCurrentGame() throws InvalidOperationException;

    /**
     * get the current game state version
     * if the player is not in a game, -1 will be returned
     * @return int gameVersion number
     */
    public int getCurrentGameVersion();

    /**
     * get the id of the current game, returns null if in no game
     * @return string game id
     */
    public String getCurrentGameID();

    /**
     * gets the user's authToken
     * @return string
     */
    public String getAuthToken();

    public boolean isGameStarted();


    public Player getPlayerWhoseTurn();

    public boolean isMyTurn();

    public boolean isFinalRound();

    public boolean isGameOver();

    public void endTurn();


    public void throwAwayDestination(List<DestinationCard> toKeep, List<DestinationCard> toDitch);

    public void drawNewDestination();

    public List<DestinationCard> getNewDestinationOptions();

    public List<DestinationCard> getMyDestinationCards();

    public int getNumberDestinationDeck();


    public void drawFaceDownTrain();

    public void drawFaceUpTrain(TrainCard trainCard);

    public TrainCard[] getFaceUpTrains();

    public int getNumberFaceUpTrains();

    public int getNumberTrainDeck();

    public Map<String, Integer> getMyTrainCards();


    public List<String> canClaimRoute(Route route);

    public void claimRoute(Route route, String color);

    public Map<String, List<Route>> getAllClaimedRoutes();

    public Map<String, Route> getNewClaimedRoutes();

    public void clearNewClaimedRoutes();


    public List<String> getPlayerWithLongestRoute();


    public void postChat(String message);

    public List<Chat> getChat();

    public List<History> getHistory();



    public String getPlayerColor(String username);

    public Player getCurrentPlayer();

    public User getUser();

    public List<Player> getPlayers();


}
