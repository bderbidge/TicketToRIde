package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Command.Command;
import Command.ChatRequestCommandData;
import Command.CreateGameCommandData;
import Command.JoinGameCommandData;
import Command.DestinationRemovalRequestCommandData;
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
import Model.Exceptions.InvalidOperationException;
import Model.Exceptions.PasswordMismatchException;
import Command.StartGameRequestCommandData;
import Command.DrawDestinationRequestCommandData;
import Command.EndTurnRequestCommandData;
import Command.DrawFaceDownTCardRequestCommandData;
import Command.DrawFaceUpTCardRequestCommandData;
import Command.ClaimRouteRequestCommandData;

/**
 * Created by emmag on 9/27/2017.
 */

public class GUIFacade implements IGUIFacade {
    private static IGUIFacade mInstance = new GUIFacade();
    private ModelRoot mModelRoot;
    private IServer mServerProxy;

    /**
     * private constructor for singleton pattern
     */
    private GUIFacade() {
        mModelRoot = ModelRoot.instance();
        mServerProxy = new ServerProxy();
    }

    /**
     *
     */
    public void setServerInfo(String serverIP, String serverPort) {
        mServerProxy = new ServerProxy(serverIP, serverPort);
    }

    /**
     * gets instance of class using singleton pattern
     * @return GUIFacade
     */
    public static IGUIFacade instance() {
        return mInstance;
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
        //check to see if user is already logged in
        if(isLoggedIn()) {
            //if already logged in, throw invalid operation exception
            throw new InvalidOperationException("register");
        }
        //check to see if the password's match
        if(User.checkPasswordsMatch(password, passwordRepeat)) {
            //check to see if both the password and username contain chars
            if(password.length() > 0 && username.length() > 0) {
                //if yes, register
                mServerProxy.register(username, password);
            }
            else {
                //throw exception
            }
        }
        else{

            throw new PasswordMismatchException();
        }
    }

    /**
     * logs in a user by sending a request to the server via the Server Proxy
     * @param username
     * @param password
     * @throws InvalidOperationException
     */
    @Override
    public void login(String username, String password) throws InvalidOperationException {
        //check and make sure they are not already logged in
        if(isLoggedIn()) {
            throw new InvalidOperationException("login");
        }
        mServerProxy.login(username, password);
    }

    /**
     * check to see if the user is logged in
     * @return boolean
     */
    @Override
    public boolean isLoggedIn() {
        if(mModelRoot.hasAuthToken() && mModelRoot.getUser() != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isGameStarted() {
        return mModelRoot.isGameStarted();
    }



    //Create game stuff

    /**
     * get the current games list from the model
     * @return GamesList
     */
    @Override
    public GamesList getGamesList() {
        return mModelRoot.getGamesList();
    }

    /**
     * get a specific game from the GamesList
     * @param gameID
     * @return Game
     */
    @Override
    public Game queryGamesList(String gameID) {
        return mModelRoot.getGamesList().queryOpenGames(gameID);
    }

    /**
     * create a game by sending a createGameCommand to Server Proxy
     * @param gameName
     * @param numberOfPlayers
     */
    @Override
    public void createGame(String gameName, int numberOfPlayers) {
        CreateGameCommandData command = new CreateGameCommandData(gameName, numberOfPlayers, getAuthToken());
        mServerProxy.command(command);
    }

    /**
     * get the gamestate of a specified game
     * @param gameID
     * @return GameState
     */
    @Override
    public GameState getGameState(String gameID) {
        return mModelRoot.getGameState(gameID);
    }


    //join game stuff

    /**
     * join the specified game in the said color via a joinGameCommand to the Server Proxy
     * @param gameID
     * @param playerColor
     */
    @Override
    public void joinGame(String gameID, String playerColor) {
        //create a player to join the game
        Player myPlayer = new Player(mModelRoot.getUser().getUsername(), playerColor);
        //set the id of the game you are trying to join
        mModelRoot.setCurrentGameID(gameID);
        //set the player's state within a game to be Pending
        mModelRoot.setInGame(ModelRoot.GAMEJOIN.PENDING);
        //create a JoinGameCommand with the create player and id of game to join
        JoinGameCommandData command = new JoinGameCommandData(myPlayer, gameID, getAuthToken());
        //send command to ServerProxy
        mServerProxy.command(command);
    }

    /**
     * get the player's game state
     * @return GAMEJOIN enum specifying if a player is in no game, pending on a join game request, or succesfully in a game
     */
    @Override
    public ModelRoot.GAMEJOIN isGameJoined() {
        return mModelRoot.getInGame();
    }



    //Start game

    /**
     * start the current game via a startGameCommand to the server proxy
     * @throws InvalidOperationException
     */
    @Override
    public void startCurrentGame() throws InvalidOperationException {
        //make sure that user is in a game to start
        if(mModelRoot.getInGame() != ModelRoot.GAMEJOIN.JOINED) {
            throw new InvalidOperationException("StartGame");
        }
        else {
            //create a start game command with the current game id
            //check to see if the game is full
            StartGameRequestCommandData command = new StartGameRequestCommandData(mModelRoot.getCurrentGameID(), getAuthToken());
            mServerProxy.command(command);
        }
    }

    /**
     * get the current game state version
     * if the player is not in a game, -1 will be returned
     * @return int gameVersion number
     */
    @Override
    public int getCurrentGameVersion() {
        return mModelRoot.getCurrentGameVersion();
    }



    //draw destinations

    @Override
    public void throwAwayDestination(List<DestinationCard> toKeep, List<DestinationCard> toRemove) {
        mModelRoot.setDestinationOptions(toKeep);
        if(toRemove == null || toRemove.size() == 0) {
            //add cards to my destinations
            if(mModelRoot.getPlayerInGame(mModelRoot.getUsername()).getNumDestCards() == 3) {
                mModelRoot.getPlayerInGame(mModelRoot.getUsername()).setNumDestCards(0);
            }
            mModelRoot.addDestinationOptionsToMyDestinations();
            mModelRoot.notifyObservers();
        }
        else {
            DestinationRemovalRequestCommandData command = new DestinationRemovalRequestCommandData(getAuthToken(), toRemove);
            mServerProxy.command(command);
        }
    }

    @Override
    public void drawNewDestination() {
        if(mModelRoot.isMyTurn()) {
            DrawDestinationRequestCommandData command = new DrawDestinationRequestCommandData(getAuthToken(), getCurrentGameID());
            mServerProxy.command(command);
        }
    }

    @Override
    public List<DestinationCard> getNewDestinationOptions() {
        return mModelRoot.getDestinationOptions();
    }

    @Override
    public int getNumberDestinationDeck() {
        return mModelRoot.getCurrentGame().getDestinationDeckCount();
    }

    @Override
    public List<DestinationCard> getMyDestinationCards() {
        return mModelRoot.getMyDestinationCards();
    }



    //draw train cards

    @Override
    public void drawFaceDownTrain() {
        DrawFaceDownTCardRequestCommandData command = new DrawFaceDownTCardRequestCommandData(getAuthToken(), getCurrentGameID());
        mServerProxy.command(command);
    }

    @Override
    public void drawFaceUpTrain(TrainCard trainCard) {
        DrawFaceUpTCardRequestCommandData command = new DrawFaceUpTCardRequestCommandData(getAuthToken(), getCurrentGameID(), trainCard);
        mServerProxy.command(command);
    }

    @Override
    public int getNumberTrainDeck() {
        return mModelRoot.getCurrentGame().getTrainDeckCount();
    }

    @Override
    public Map<String, Integer> getMyTrainCards() {
        return mModelRoot.getMyTrainCardCounts();
    }

    @Override
    public int getNumberFaceUpTrains() {
        int num = 0;
        TrainCard[] yard = getFaceUpTrains();
        for(int i = 0; i < yard.length; i++) {
            if(yard[i] != null) {
                num++;
            }
        }
        return num;
    }

    @Override
    public TrainCard[] getFaceUpTrains() {
        return mModelRoot.getCurrentGame().getFaceUpTrainDeck();
    }


    //claim route

    @Override
    public List<String> canClaimRoute(Route route) {
        List<String> colorOptions = null;

        if(!mModelRoot.checkIfRouteClaimed(route.getId())) {
            if(route.getLength() > mModelRoot.getPlayer().getTrains()) {
                return null;
            }

            if(route.isDoubleRoute()) {
                for(Route r : mModelRoot.getPlayer().getRoutes()) {
                    if(r.getId() == route.getDoubleID()) {
                        return null;
                    }
                }
            }

            Map<String, Integer> myCards = mModelRoot.getMyTrainCardCounts();
            int wildCount = mModelRoot.getMyTrainCardCounts().get("wild");

            //does the route have a specified color?
            if(route.getColor().equals("blank")){
                for(Map.Entry<String, Integer> entry : myCards.entrySet()) {
                    if((!entry.getKey().equals("wild") && entry.getValue() > 0 &&      // don't allow wilds to add to each other
                            entry.getValue() + wildCount >= route.getLength()) ||
                            (entry.getKey().equals("wild") && wildCount >= route.getLength())) {   // allow user to use all wilds
                        if(colorOptions == null) {
                            colorOptions = new ArrayList<String>();
                        }
                        colorOptions.add(entry.getKey());
                    }
                }
            }
            else {
                if (myCards.get(route.getColor()) != null &&
                        myCards.get(route.getColor()) + wildCount >= route.getLength()) {
                    if (colorOptions == null) {
                        colorOptions = new ArrayList<String>();
                    }
                    colorOptions.add(route.getColor());
                }
            }
        }
        return colorOptions;
    }

    @Override
    public void claimRoute( Route route, String color) {
        ClaimRouteRequestCommandData command = new ClaimRouteRequestCommandData(getAuthToken(), getCurrentGameID(), route, color);
        mServerProxy.command(command);

    }

    public Map<String, List<Route>> getAllClaimedRoutes() {
        return mModelRoot.getAllClaimedRoutes();
    }

    @Override
    public Map<String, Route> getNewClaimedRoutes() {
        return mModelRoot.getNewClaimedRoutes();
    }

    @Override
    public void clearNewClaimedRoutes() {
        mModelRoot.clearNewClaimedRoutes();
    }

    @Override
    public List<String> getPlayerWithLongestRoute() {
        return mModelRoot.getPlayerWithLongestRoute();
    }


    //chat

    @Override
    public void postChat(String message) {
        Chat chat = new Chat(message, mModelRoot.getUser().getUsername(), mModelRoot.getPlayer().getColor());
        Command command = new ChatRequestCommandData(mModelRoot.getAuthToken(), chat, mModelRoot.getCurrentGameID());
        mServerProxy.command(command);
    }

    @Override
    public List<Chat> getChat() {
        return mModelRoot.getChats();
    }




    @Override
    public String getPlayerColor(String username) {
        for (Player p : mModelRoot.getPlayerOrder()) {
            if(p.getUsername().equals(username)) {
                return p.getColor();
            }
        }
        return null;
    }

    @Override
    public User getUser() {
        return mModelRoot.getUser();
    }

    @Override
    public Player getCurrentPlayer() {
        return mModelRoot.getPlayer();
    }

    @Override
    public List<Player> getPlayers() {
        return mModelRoot.getPlayerOrder();
    }


    @Override
    public List<History> getHistory() {
        return mModelRoot.getHistory();
    }



    @Override
    public Player getPlayerWhoseTurn() {
        return mModelRoot.getPersonTakingTurn();
    }

    @Override
    public boolean isMyTurn() { return mModelRoot.isMyTurn(); }

    @Override
    public boolean isFinalRound() {
        return mModelRoot.isFinalRound();
    }

    @Override
    public boolean isGameOver() {
        return mModelRoot.isGameOver();
    }

    @Override
    public void endTurn() {
        EndTurnRequestCommandData command = new EndTurnRequestCommandData(mModelRoot.getAuthToken(), mModelRoot.getCurrentGameID());
        mServerProxy.command(command);
    }

    /**
     * get the id of the current game, returns null if in no game
     * @return string game id
     */
    @Override
    public String getCurrentGameID() {
        return mModelRoot.getCurrentGameID();
    }

    /**
     * gets the user's authToken
     * @return string
     */
    @Override
    public String getAuthToken() {
        return mModelRoot.getAuthToken();
    }

}
