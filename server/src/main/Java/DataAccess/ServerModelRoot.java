package DataAccess;

/**
 * Created by brandonderbidge on 9/28/17.
 */

import java.util.ArrayList;
import java.util.HashMap;
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
import RequestResult.LoginRegisterResult;



public class ServerModelRoot {


    private UserDAO userTable = new UserDAO(this);

    private GameDAO gameTable = new GameDAO(this);

    private ChatDAO chatTable = new ChatDAO(this);

    private DCardDAO dCardTable = new DCardDAO(this);

    private TCardDAO tCardTable = new TCardDAO(this);

    private HistoryDAO historyTable = new HistoryDAO(this);

    private static ServerModelRoot mInstance = new ServerModelRoot();

    public static ServerModelRoot instance() { return mInstance; }

    private Map<String, List<Chat>> conversations = new HashMap<>();//cleared at end game

    private Map<String, List<DestinationCard>> GamesToDDecks = new HashMap<>();//cleared at end game

    private Map<String, Game> gameList = new HashMap<>();

    private Map<String, Game> gameListStarted = new HashMap<>();//cleared at end game

    private Map<String, GameState> playerToState = new HashMap<>();

    private Map<String, Game> userToGameList = new HashMap<>();//cleared at end game

    private Map<String, List<User>> GameToUsers = new HashMap<>();//cleared at end game

    private Map<String, List<History>> gameLogs = new HashMap<>();//cleared at end game

    private Map<String, List<TrainCard>> downDeck = new HashMap<>();//cleared at end game

    private Map<String, List<TrainCard>> upDeck = new HashMap<>();//cleared at end game

    private Map<String, User> registeredUsers = new HashMap<>();

    private Map<String, String> userAuthtokens = new HashMap<>();

    private Map<String, String> authtokenUsers = new HashMap<>();

    private Map<String, List<Player>> endGameData = new HashMap<>();

    public ServerModelRoot() {
    }

    public Map<String, List<Chat>> getConversations() {
        return conversations;
    }

    public void setConversations(Map<String, List<Chat>> conversations) {
        this.conversations = conversations;
    }

    public Map<String, Game> getGameList() {
        return gameList;
    }

    public void setGameList(Map<String, Game> gameList) {
        this.gameList = gameList;
    }

    public Map<String, Game> getGameListStarted() {
        return gameListStarted;
    }

    public void setGameListStarted(Map<String, Game> gameListStarted) {
        this.gameListStarted = gameListStarted;
    }

    public Map<String, GameState> getPlayerToState() {
        return playerToState;
    }

    public void setPlayerToState(Map<String, GameState> playerToState) {
        this.playerToState = playerToState;
    }

    public Map<String, Game> getUserToGameList() {
        return userToGameList;
    }

    public void setUserToGameList(Map<String, Game> userToGameList) {
        this.userToGameList = userToGameList;
    }

    public Map<String, List<User>> getGameToUsers() {
        return GameToUsers;
    }

    public void setGameToUsers(Map<String, List<User>> gameToUsers) {
        GameToUsers = gameToUsers;
    }

    public Map<String, List<History>> getGameLogs() {
        return gameLogs;
    }

    public void setGameLogs(Map<String, List<History>> gameLogs) {
        this.gameLogs = gameLogs;
    }

    public Map<String, List<TrainCard>> getDownDeck() {
        return downDeck;
    }

    public void setDownDeck(Map<String, List<TrainCard>> downDeck) {
        this.downDeck = downDeck;
    }

    public Map<String, List<TrainCard>> getUpDeck() {
        return upDeck;
    }

    public void setUpDeck(Map<String, List<TrainCard>> upDeck) {
        this.upDeck = upDeck;
    }

    public Map<String, User> getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(Map<String, User> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public Map<String, String> getUserAuthtokens() {
        return userAuthtokens;
    }

    public void setUserAuthtokens(Map<String, String> userAuthtokens) {
        this.userAuthtokens = userAuthtokens;
    }

    public Map<String, String> getAuthtokenUsers() {
        return authtokenUsers;
    }

    public void setAuthtokenUsers(Map<String, String> authtokenUsers) {
        this.authtokenUsers = authtokenUsers;
    }

    public Map<String, List<DestinationCard>> getGamesToDDecks() {
        return GamesToDDecks;
    }

    public void setGamesToDDecks(Map<String, List<DestinationCard>> gamesToDDecks) {
        GamesToDDecks = gamesToDDecks;
    }

    public Player getPlayerFromUserName(String userName, String gameID){
        Game game = gameListStarted.get(gameID);
        GameState gs = game.getCurrentGameState();
        return gs.getCurrentPlayers().get(userName);
    }


    /**
     * Registers a user in the game
     *
     * @param username of user registering
     * @param password of user registering
     * {pre username != null}
     * {pre password != null}
     * {post loginRegisterResult == a new registered user}
     * @return LoginRegisterResult
     */
    public LoginRegisterResult register(String username, String password)  {

        LoginRegisterResult loginRegisterResult;


        loginRegisterResult = userTable.register(username, password);



        return loginRegisterResult;
    }

    /**
     * Login an already existing user
     *
     * @param username of user logging in
     * @param password of user logging in
     * {pre username != null}
     * {pre password != null}
     * {post loginRegisterResult == previously registered user}
     * @return LoginRegisterResult
     */
    public LoginRegisterResult login(String username, String password){

        LoginRegisterResult loginRegisterResult;

        loginRegisterResult = userTable.login( username,  password);


        return loginRegisterResult;
    }

    /**
     * Joins a game that was previously created
     *
     * {pre player != null }
     * {pre gameID != null && must be a previously created game}
     * @param player that needs to be added to the game
     * @param gameID of the game that the player is trying to join
     * @return game that has a gameID == to the one passed in
     */
    public Game joinGame(Player player, String gameID){

        Game game;

        game = gameTable.joinGame(player, gameID);

        return game;
    }

    /**
     * @return gameList containing a list of games that were created and not started yet.
     *
     * {pre none}
     * {post games == a list of games in the gameTable}
     */
    public GamesList getGamesList() {


        GamesList games = gameTable.getGameList();

        return games;
    }


    /**
     * @return gameID of the game that was created
     *
     * @param name is the name of the game to be created
     * @param numPlayers is the number of players to in the game
     * {pre name != null && name != already exist}
     * {pre 2 <= numPlayers <= 5}
     * {post gameID == the game created and a new game is added to the game list }
     */
    public String createGame(String name, int numPlayers){

        String gameID = gameTable.addGame(name, numPlayers);

        return gameID;

    }

    /**
     * @return GameState of the gameID you passed in
     *
     * @param ID is the gameID of the GameState you are retrieving
     * {pre  ID != null && ID must be the ID of a game that has already been created}
     * {post state == the state with the associated gameID passed in }
     */

    public GameState getGameState(String ID){
        GameState state = gameTable.getGameState(ID);

        return state;
    }

    /**
     * @return the Game with the associated ID passed in and the destination and train cards are
     * initialized as lists in the game object
     *
     * @param ID is the gameID of the Game you are trying to start
     * {pre ID != null && is an existing game that has not already been started }
     * {post both the destination and train decks are initialized and the game is started}
     */
    public Game startGame(String ID){
        dCardTable.initializeDeck(ID);
        tCardTable.initializeDecks(ID);
        try{
            Game game = gameTable.startGame(ID);
            return game;
        } catch(Exception e){
            return null;
        }
    }

    /**
     * This gets the username of the player associated with the authtoken and then gets the
     * game the player is associated with.
     *
     * @param authToken is associated with the user and verifies the user is associated with the game
     * @return the game that the user is associated with
     *
     * {pre authToken != null && User with the authToken must already exist.}
     * {post the user with the associated authToken is found and the game that the user is in is
     * also returned}
     */
    public Game Poll(String authToken){

        String username = userTable.getUserFromAuth(authToken);

        return  gameTable.getGameFromUserName(username);


    }

    /**
     * This places a chat into the chat list of a specific game associated with the gameID passed in
     *
     * @param gameID is the gameID of the game that you are placing your chat into
     * @param c is the chat that the user submitted to be added to a specific game
     *
     * {pre gameID != null and is associated with a game that has been started and already exists}
     * {post the chat is added to the list of chats associated with the gameID passed in }
     */
    public void logChat(String gameID, Chat c){
        chatTable.addChat(gameID, c);
    }

    /**
     * This places a history into the history list of a specific game associated with the gameID passed in
     *
     * @param gameID is the gameID of the game that you are placing your chat into
     * @param h is the history that the user submitted to be added to a specific game
     *
     * {pre gameID != null and is associated with a game that has been started and already exists}
     * {post the history is added to the list of histories associated with the gameID passed in }
     */
    public void logHistory(String gameID, History h){
        historyTable.addHistory(gameID, h);
    }

    /**
     * This function returns a players destination cards back to the destination deck
     *
     * @param gameID is the gameID of the game that you are placing your chat into
     * @param userName is the username of the player who is returning cards to the deck
     * @param dc a list of destination cards being returned to the deck
     *
     * {pre gameID != null && its associated game must already exist && be started}
     * {pre userName != null && a user with the associated username == exists}
     * {pre dc != null dc >= 1 }
     *
     * {post the DestinationCards in dc are added back to the DestinationDeck List}
     */
    public void returnDCards(String gameID, String userName, List<DestinationCard> dc){
        dCardTable.returnCards(gameID, dc);
        dCardTable.shuffle(gameID);
        gameTable.returnDCards(gameID, userName, dc);
        userTable.returnDCards(userName, dc);
    }


    /**
     * Returns the username of a user with the associated authToken
     *
     * @param auth is an authToken associated with a User
     * @return username of user with the associated authToken
     *
     * {pre auth != null and is associated with a user}
     * {post userName == the userName of a player with the associated authToken}
     */
    public String getUserName(String auth){
        String userName = userTable.getUserFromAuth(auth);
        return userName;
    }


    /**
     * Returns the User with the associated userName
     *
     * @param userName is the username of a specific User
     * @return User object of user with the associated username
     *
     * {pre username != null and is associated with a spesific user}
     * {post User == a specific User with the associated userName}
     */
    public User getUser(String userName){
        return registeredUsers.get(userName);
    }

    /**
     * Returns the User with the associated userName
     *
     * @param username is the username of a specific User
     * @return gameID of a game that the username is associated with
     *
     * {pre username != null and is associated with a specific user}
     * {post gameID == a specific Game that contains the associated username}
     */
    public String getGameID(String username){
        String gameID = gameTable.getGameIDFromUserName(username);
        return gameID;
    }

    public Game getGame(String gameID){
        Game game = gameListStarted.get(gameID);
        return game;
    }

    /**
     * Get how many cards each player has as a map of key value pairs
     *
     * @param gameID is the gameID of the game that you retrieving the
     * number of cards each player has in their hand.
     * @return toRet is a map containing
     * {pre gameID != null && its associated game must already exist && be started}
     * {post toRet == Map of usernames to the number of Destination cards each user has}
     *
     */
    public Map<String, Integer> getPlayerDCardCounts(String gameID){
        Map<String, Integer> toRet = new HashMap<>();
        Game game = gameListStarted.get(gameID);
        Map<String, Player> players = game.getCurrentGameState().getCurrentPlayers();
        for (Player p: players.values()) {
            Integer i = p.getNumDestCards();
            toRet.put(p.getUsername(), i);
        }
        return toRet;
    }


    /**
     * Get the number of cards that are in the deck
     *
     * {pre gameID != null && its associated game must already exist && be started}
     * @param gameID is the gameID of the game that you retrieving the
     * @return the number of DestinationCards in the deck List.
     */
    public int getDDeckCount(String gameID){
        List<DestinationCard> deck = getGamesToDDecks().get(gameID);
        return deck.size();
    }

    /**
     * @return number of destination cards for a spesific user
     * @param gameID is the gameID of the game that you retrieving the
     * @param userName is the username of a specific User
     * {pre username != null and is associated with a specific user}
     * {pre gameID != null && its associated game must already exist && be started}
     * {post }
     */
    public int getPlayerDCardCount(String gameID, String userName){
        Game game = gameListStarted.get(gameID);
        Map<String, Player> players = game.getCurrentGameState().getCurrentPlayers();
        Player myPlayer = players.get(userName);
        return myPlayer.getNumDestCards();
    }

    /**
     * @return color of the specified user passed in based off of the username
     * @param gameID is the gameID of the game that you retrieving the
     * @param userName is the username of a specific User
     * {pre username != null and is associated with a specific user}
     * {pre gameID != null && its associated game must already exist && be started}
     */
    public String getColor(String gameID, String userName){
        Game game = gameListStarted.get(gameID);
        Map<String, Player> players = game.getCurrentGameState().getCurrentPlayers();
        return players.get(userName).getColor();
    }

    public boolean drawUpTCard(TrainCard tc, String gameID, String userName){
        boolean shuffled = tCardTable.drawUpCard(gameID, tc);
        gameTable.drawUpTCard(gameID, userName);
        userTable.drawTCard(userName, tc);
        return shuffled;
    }

    public TrainCard drawDownTCard(String gameID, String userName){
        TrainCard card = tCardTable.drawDownTCard(gameID);
        gameTable.drawDownTCard(gameID, userName);
        userTable.drawTCard(userName, card);
        return card;
    }

    public List<DestinationCard> drawDCard(String gameID, String userName){
        List<DestinationCard> drawn = dCardTable.draw3Cards(gameID);
        gameTable.drawDCards(gameID, userName, drawn);
        userTable.drawDCards(userName, drawn);
        return drawn;
    }

    public String endTurn(String gameID, String userName){
        if(isLastRound(gameID, userName)){
            startLastRound(gameID, userName);
        }
        String toRet = gameTable.endTurn(gameID, userName);
        if(toRet == null){
            prepareEndGameData(gameID);
            endGame(gameID);
        }
        return toRet;
    }

    public void claimRoute(Route route, String gameID, String userName, String cColor){
        gameTable.claimRoute(route, gameID, userName);
        List<TrainCard> cardToAdd = new ArrayList<>();
        if(cColor == null){
            cardToAdd = userTable.claimRoute(userName,route, route.getColor());
        }
        else{
            cardToAdd = userTable.claimRoute(userName, route, cColor);
        }
        tCardTable.returnCards(gameID, cardToAdd);
    }

    private boolean isLastRound(String gameID, String userName){
        Game game = gameListStarted.get(gameID);
        Player p = game.getCurrentGameState().getCurrentPlayers().get(userName);
        int count = p.getTrains();
        if(count < 3){
            return true;
        }
        return false;
    }

    public boolean isLastRound(String gameID){
        return gameTable.isLastRound(gameID);
    }

    private void startLastRound(String gameID, String userName){
        gameTable.startLastRound(gameID, userName);
    }

    private void prepareEndGameData(String gameID){
        Game game = gameListStarted.get(gameID);
        List<Player> players = game.getCurrentGameState().getOrderedPlayers();
        endGameData.put(gameID, players);

    }

    public List<Player> getEndGameData(String gameID){
        return endGameData.get(gameID);
    }

    private void endGame(String gameID){
        userTable.endGamePoints(gameID);
        gameListStarted.remove(gameID);
        conversations.remove(gameID);
        gameLogs.remove(gameID);
        GamesToDDecks.remove(gameID);
        GameToUsers.remove(gameID);
        downDeck.remove(gameID);
        upDeck.remove(gameID);
        userToGameList.values().remove(gameID);
    }

}
