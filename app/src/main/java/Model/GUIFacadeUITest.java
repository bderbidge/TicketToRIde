package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Communication.Chat;
import Communication.DestinationCard;
import Communication.Game;
import Communication.GameState;
import Communication.GamesList;
import Communication.History;
import Communication.IServer;
import Communication.Player;
import Communication.Route;
import Communication.TrainCard;
import Communication.User;
import Model.Exceptions.InvalidOperationException;
import Model.Exceptions.PasswordMismatchException;
import RequestResult.LoginRegisterResult;

/**
 * Created by emmag on 9/27/2017.
 */

public class GUIFacadeUITest implements IGUIFacade {
    private static IGUIFacade mInstance = new GUIFacadeUITest();
    private ModelRoot mModelRoot;
    private IServer mServerProxy;

    /**
     * private constructor for singleton pattern
     */
    private GUIFacadeUITest() {
        mModelRoot = ModelRoot.instance();
        //mServerProxy = new ServerProxy();
        // since this is just for UI, we don't need a server proxy, we will be hardcoding all data into the model
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
        User newUser = new User(username, password);
        LoginRegisterResult registerResult = new LoginRegisterResult(true, null, "myauthtoken", newUser);
        //CommandFacade.instance().resultReturn(registerResult); not using commandFacade because the commandFacade creates a poller

        mModelRoot.setAuthToken(registerResult.getAuthToken());
        mModelRoot.setUser(registerResult.getUser());
        //notify the observers of changes
        mModelRoot.notifyObservers();
    }

    /**
     * logs in a user by sending a request to the server via the Server Proxy
     * @param username
     * @param password
     * @throws InvalidOperationException
     */
    @Override
    public void login(String username, String password) throws InvalidOperationException {

        User newUser = new User(username, password);
        LoginRegisterResult loginResult = new LoginRegisterResult(true, null, "myauthtoken", newUser);
        //CommandFacade.instance().resultReturn(registerResult); not using commandFacade because the commandFacade creates a poller

        mModelRoot.setAuthToken(loginResult.getAuthToken());
        mModelRoot.setUsername(username);
        mModelRoot.setUser(loginResult.getUser());
        //notify the observers of changes

        //this is to skip going through the UI each time
        createGame("testGame", 5);
        joinGame("0", "red");
        startCurrentGame();
        mModelRoot.notifyObservers();
    }

    /**
     * check to see if the user is logged in
     * @return boolean
     */
    @Override
    public boolean isLoggedIn() {
        //use normal guifacades functionality for now
        return GUIFacade.instance().isLoggedIn(); //this works for the testing environment
    }


    //Create game stuff

    /**
     * get the current games list from the model
     * @return GamesList
     */
    @Override
    public GamesList getGamesList() {
        //use normal guifacades functionality for now
        return GUIFacade.instance().getGamesList(); //this works for the UI test
    }

    /**
     * get a specific game from the GamesList
     * @param gameID
     * @return Game
     */
    @Override
    public Game queryGamesList(String gameID) {
        //use normal guifacade's functionality for now
        return GUIFacade.instance().queryGamesList(gameID); //this works for the UI test
    }

    /**
     * create a game by sending a createGameCommand to Server Proxy
     * @param gameName
     * @param numberOfPlayers
     */
    @Override
    public void createGame(String gameName, int numberOfPlayers) {
        //use normal guifacade's functionality for now
        //GUIFacade.instance().createGame(gameName, numberOfPlayers);
        GameState newGameState = new GameState(false, new HashMap<String, Player>(), 0);
        Game newGame = new Game("0", gameName, numberOfPlayers, newGameState);

        GamesList oldGamesList = getGamesList();
        Map<String, Game> newOpenGames = oldGamesList.getOpenGames();
        newOpenGames.put(newGame.getGameID(), newGame);
        GamesList newGamesList = new GamesList(newOpenGames);

        CommandFacade.instance().updateGamesList(newGamesList);
    }

    /**
     * get the gamestate of a specified game
     * @param gameID
     * @return GameState
     */
    @Override
    public GameState getGameState(String gameID) {
        //use normal guifacade's functionality for now
        return GUIFacade.instance().getGameState(gameID); //this still works for UI
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
        //GUIFacade.instance().joinGame(gameID, playerColor);


        //create a player to join the game
        Player myPlayer = new Player(mModelRoot.getUser().getUsername(), playerColor);
        //set the id of the game you are trying to join
        mModelRoot.setCurrentGameID(gameID);
        //set the player's state within a game to be Pending
        mModelRoot.setInGame(ModelRoot.GAMEJOIN.PENDING);

        GameState currentGameState = getGameState(gameID);
        currentGameState.addPlayer(myPlayer);

        CommandFacade.instance().updateGameState(gameID, currentGameState);
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

        //startGame is broken in GUIFacade, going to bypass server
        GameState currentGameState = GUIFacade.instance().getGameState(GUIFacade.instance().getCurrentGameID());
        GameState startGameState = new GameState(true, currentGameState.getCurrentPlayers(), currentGameState.getCurrentNumberOfPlayers());

        User u = getUser();
        u.setDestinationCards(new ArrayList<DestinationCard>()); //makes the destinationCards non-null for later funtion calls

        TrainCard t1 = new TrainCard("green");
        TrainCard t2 = new TrainCard("blue");
        TrainCard t3 = new TrainCard("black");
        TrainCard t4 = new TrainCard("red");
        TrainCard t5 = new TrainCard("purple");
        TrainCard t6 = new TrainCard("purple");
        TrainCard t7 = new TrainCard("black");
        TrainCard t8 = new TrainCard("black");
        TrainCard t9 = new TrainCard("black");
        TrainCard t10 = new TrainCard("wild");

        TrainCard trainCards[] = {t1, t2, t3, t4, t5, t6, t7, t8, t9, t10};
        u.setTrainCards(Arrays.asList(trainCards));
        mModelRoot.setUser(u);

        Player p2 = new Player("peter", "Green");
        Player p3 = new Player("emma", "Yellow");
        Player p4 = new Player("brandon", "Blue");
        //Player p5 = new Player("emily", "Black");

        mModelRoot.getCurrentGame().getCurrentGameState().addPlayer(p2);
        mModelRoot.getCurrentGame().getCurrentGameState().addPlayer(p3);
        mModelRoot.getCurrentGame().getCurrentGameState().addPlayer(p4);
        //mModelRoot.getCurrentGame().getCurrentGameState().addPlayer(p5);

        TrainCard t11 = new TrainCard("green");
        TrainCard t12 = new TrainCard("blue");
        TrainCard t13 = new TrainCard("black");
        TrainCard t14 = new TrainCard("red");
        TrainCard t15 = new TrainCard("purple");
        TrainCard trainCardsYard[] = {t11, t12, t13, t14, t15};
        mModelRoot.getCurrentGame().setUpDeck(trainCardsYard);
        mModelRoot.getCurrentGame().setTrainDeckCount(55);

        List<Player> playerOrder = new ArrayList<>();
        playerOrder.add(mModelRoot.getPlayer());
        playerOrder.add(p2);
        playerOrder.add(p3);
        playerOrder.add(p4);
        //playerOrder.add(p5);
        mModelRoot.setPlayerOrder(playerOrder);
        //add destination cards for mock start game

        DestinationCard dCard1 = new DestinationCard("Atlanta", "Orlando", 15);
        DestinationCard dCard2 = new DestinationCard("Denver", "New York", 5000);
        DestinationCard dCard3 = new DestinationCard("Seattle", "Los Angeles", 10);
        List<DestinationCard> drawnCards = new ArrayList<>();
        drawnCards.add(dCard1);
        drawnCards.add(dCard2);
        drawnCards.add(dCard3);
        mModelRoot.setDestinationOptions(drawnCards);

        //add history
        Player currentPlayer = mModelRoot.getPlayer();
        History history1 = new History("Test History 1", u.getUsername(), currentPlayer.getColor());
        History history2 = new History("Test History for Player2", p2.getUsername(), p2.getColor());

        mModelRoot.addHistory(history1);
        mModelRoot.addHistory(history2);

        CommandFacade.instance().updateGameState(getCurrentGameID(), startGameState);


        mModelRoot.addClaimedRoute("peter", new Route(true, 3, "Vancouvar", "Calgary", "Blank", false, 1));
        mModelRoot.addClaimedRoute("emma", new Route(true, 2, "Montreal", "Boston", "Blank", true, 10));
        mModelRoot.addClaimedRoute("brandon", new Route(true, 4, "Toronto", "Chicago", "White", false, 16));
        mModelRoot.addClaimedRoute("emma", new Route(true, 3, "Montreal", "New York", "Blue", false, 11));
        //mModelRoot.addClaimedRoute("emily", new Route(true, 3, "Montreal", "New York", "Blue", false, 11));

        mModelRoot.setPersonTakingTurn(p4);
    }

    @Override
    public boolean isGameStarted() {
        return GUIFacade.instance().isGameStarted();
    }

    /**
     * get the current game state version
     * if the player is not in a game, -1 will be returned
     * @return int gameVersion number
     */
    @Override
    public int getCurrentGameVersion() {
        return GUIFacade.instance().getCurrentGameVersion();
    }

    /**
     * get the id of the current game, returns null if in no game
     * @return string game id
     */
    @Override
    public String getCurrentGameID() {
        return GUIFacade.instance().getCurrentGameID();
    }

    /**
     * gets the user's authToken
     * @return string
     */
    @Override
    public String getAuthToken() {
        return GUIFacade.instance().getAuthToken();
    }


    //PHASE 2


    @Override
    public void throwAwayDestination(List<DestinationCard> destinationCardsKeep, List<DestinationCard> destinationCardsDitch) {

        mModelRoot.setDestinationOptions(destinationCardsKeep);
        if(destinationCardsDitch == null || destinationCardsDitch.size() == 0) {
            //add cards to my destinations
            mModelRoot.addDestinationOptionsToMyDestinations();
            mModelRoot.setDestinationOptions(null);
            mModelRoot.notifyObservers();
        }
        else {
            CommandFacade.instance().finishRemovingDestinations(destinationCardsKeep.size(), 30);
        }
    }

     //this method needs to return a list which can then be sent to the View
    @Override
    public void drawNewDestination() {
        //GUIFacade.instance().drawNewDestination();

    }

    @Override
    public void drawFaceDownTrain() {

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
        String userName = getUser().getUsername();
        Chat newChat = new Chat(message, userName, getGameState(getCurrentGameID()).getPlayer(userName).getColor());
        CommandFacade.instance().addChat(newChat);
    }

    @Override
    public String getPlayerColor(String username) {
        return GUIFacade.instance().getPlayerColor(username);
    }


    @Override
    public Player getCurrentPlayer() {
        return GUIFacade.instance().getCurrentPlayer();
    }

    @Override
    public User getUser() {
        return mModelRoot.getUser();
    }

    public List<Player> getPlayers() {
        //return GUIFacade.instance().getPlayers();
        return new ArrayList<Player>(mModelRoot.getCurrentGame().getCurrentGameState().getCurrentPlayers().values());
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
        // this method needs to get train cards from real GUIFacade, for now it is hard code
        TrainCard trainCards[] = ModelRoot.instance().getCurrentGame().getFaceUpTrainDeck();
        return trainCards;
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
        return GUIFacade.instance().isMyTurn();
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
        Map<String, Route> claims = GUIFacade.instance().getNewClaimedRoutes();
        return claims;
    }

    @Override
    public void clearNewClaimedRoutes() {
        GUIFacade.instance().clearNewClaimedRoutes();
    }

    @Override
    public List<String> getPlayerWithLongestRoute() {
        return GUIFacade.instance().getPlayerWithLongestRoute();
    }

}

