package Model;

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
import Communication.User;

import static Model.ModelRoot.GAMEJOIN.JOINED;
import static Model.ModelRoot.GAMEJOIN.NO_GAME;

/**
 * Created by emmag on 9/27/2017.
 */

public class ModelRoot extends ModelSubject {
    private static ModelRoot mInstance = new ModelRoot();
    private String mAuthToken;
    private String mUsername;
    private User mUser;
    private GamesList mGamesList;
    private Game mCurrentGame;
    private String mCurrentGameID;
    private Player mPlayer;

    public enum GAMEJOIN {NO_GAME, PENDING, JOINED;}
    private GAMEJOIN inGame = NO_GAME;

    private boolean gameStarted = false;
    private boolean myTurn = false;
    private boolean finalRound = false;
    private boolean gameOver = false;


    private List<Chat> mChats;
    private List<History> mHistories;
    private List<Player> playerOrder;
    private Player personTakingTurn;
    private String playerWithLongestRoute;

    private Map<String, List<Route>> mAllClaimedRoutesByUser;
    private Map<String, Route> mNewClaimedRoutesByUser;
    private Map<Integer, Route> mClaimedRoutesByID;
    //private List<Integer> mNewClaimedRouteIDs;
    private List<DestinationCard> destinationOptions;
    private Map<String, Integer> mTrainCardCount;


    public static ModelRoot instance() {
        return mInstance;
    }

    /**
     * private constructor for singleton pattern
     */
    private ModelRoot() {
        super();
        mGamesList = new GamesList(new HashMap<String, Game>());
        mChats = new ArrayList<Chat>();
        mHistories = new ArrayList<History>();
        mAllClaimedRoutesByUser = new HashMap<>();
        mNewClaimedRoutesByUser = new HashMap<>();
        mClaimedRoutesByID = new HashMap<>();   // don't know if this is used/needed but NullPtExcep EPM
    }

    /**
     * gets the user's authtoken. there will only be an authtoken if the user has successfully logged in
     * @return string authToken
     */
    public String getAuthToken() {
        return mAuthToken;
    }

    /**
     * set the user's AuthToken
     * @param authToken string
     */
    public void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    /**
     * check to see if the user has an authToken. Only has authToken if they have successfully logged in
     * @return boolean
     */
    public boolean hasAuthToken() {
        if(mAuthToken != null) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * return's the user object
     * only has a user if they have successfully logged in
     * @return user
     */
    public User getUser() {
        return mUser;
    }

    /**
     * set the user
     * @param user
     */
    public void setUser(User user) {
        if(user != null) {
            mUser = user;
        }
    }

    /**
     * get the current games list
     * @return GameList
     */
    public GamesList getGamesList() {
        return mGamesList;
    }

    /**
     * set the current game list
     * @param gamesList
     */
    public void setGamesList(GamesList gamesList) {
        mGamesList = gamesList;
    }

    /**
     * get the current game. will return null if user is not in a game
     * @return Game
     */
    public Game getCurrentGame() {
        return mCurrentGame;
    }

    /**
     * set the user's current game
     * @param currentGame
     */
    public void setCurrentGame(Game currentGame) {
        mCurrentGame = currentGame;
    }

    /**
     * get the id of the user's current game. will return null if the user has not successfully joined a game
     * @return string gameID
     */
    public String getCurrentGameID() {
        return mCurrentGameID;
    }

    /**
     * set the current game's id. will return correctly even if the user has not been confirmed for game
     * @param currentGameID
     */
    public void setCurrentGameID(String currentGameID) {
        mCurrentGameID = currentGameID;
    }

    /**
     * get the state of the specified game
     * @param gameID id of game to get state of
     * @return GameState
     */
    public GameState getGameState(String gameID) {
        //do i have a current game
        if(mCurrentGameID != null && mCurrentGame != null) {
            //if i have a game and game id
            //check to see if this gameID matches
            if(mCurrentGameID.equals(gameID)){
                return mCurrentGame.getCurrentGameState();
            }
        }
        //otherwise, if i do not have a current game or my game id dosn't match
        return mGamesList.queryOpenGames(gameID).getCurrentGameState();
    }

    /**
     * return the user's player. Will be null if the user is not successfully in a game
     * @return Player
     */
    public Player getPlayer() {
        return mPlayer;
    }

    /**
     * set the user's player
     * @param player
     */
    public void setPlayer(Player player) {
        mPlayer = player;
    }

    /**
     * get's information about whether a user is in no game NO_GAME, trying to join a game PENDING, or successfully in a game JOINED
     * @return GAMEJOIN enum
     */
    public GAMEJOIN getInGame() {
        return inGame;
    }

    /**
     * sets the user's state regarding being in a game
     * @param inGame
     */
    public void setInGame(GAMEJOIN inGame) {
        this.inGame = inGame;
    }

    /**
     * gets the current version of the game the use is in
     * returns -1 if the user is not in a game
     * @return int game version
     */
    public int getCurrentGameVersion() {
        if(inGame == JOINED) {
            return mCurrentGame.getGameVersion();
        }
        return -1;
    }

    public Player getPlayerInGame(String username) {
        return mCurrentGame.getCurrentGameState().getPlayer(username);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean turn) {
        myTurn = turn;
    }

    public boolean isFinalRound() {
        return finalRound;
    }

    public void setFinalRound(boolean finalRound) {
        this.finalRound = finalRound;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void addChat(Chat c) {
        mChats.add(c);
    }

    /*public void updateChat(List<Chat> newChats) {
        for (Chat c : newChats) {
            mChats.add(c);
        }
    }*/

    public List<Chat> getChats() {
        return mChats;
    }

    public void addHistory(History h) {
        mHistories.add(h);
    }

    /*public void updateHistory(List<History> newHistory) {
        for(History h : newHistory){
            mHistories.add(h);
        }
    }*/

    public List<History> getHistory() {
        return mHistories;
        //maybe should modify this to be unchangable
    }

    public List<Player> getPlayerOrder() {
        return playerOrder;
    }

    public void setPlayerOrder(List<Player> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public Player getPersonTakingTurn() {
        return personTakingTurn;
    }

    public void setPersonTakingTurn(Player personTakingTurn) {
        this.personTakingTurn = personTakingTurn;
    }

    public void calculateMyTrainCardCounts() {
        mTrainCardCount = new HashMap<String, Integer>();
        mTrainCardCount.put("red", mUser.getRedTrains());
        mTrainCardCount.put("yellow", mUser.getYellowTrains());
        mTrainCardCount.put("blue", mUser.getBlueTrains());
        mTrainCardCount.put("black", mUser.getBlackTrains());
        mTrainCardCount.put("purple", mUser.getPurpleTrains());
        mTrainCardCount.put("orange", mUser.getOrangeTrains());
        mTrainCardCount.put("green", mUser.getGreenTrains());
        mTrainCardCount.put("white", mUser.getWhiteTrains());
        mTrainCardCount.put("wild", mUser.getWildTrains());
    }

    public Map<String, Integer> getMyTrainCardCounts() {
        if(mTrainCardCount == null) {
            calculateMyTrainCardCounts();
        }
        return mTrainCardCount;
    }

    public List<DestinationCard> getMyDestinationCards() {
        return mUser.getDestCards();
    }

    public Map<String, List<Route>> getAllClaimedRoutes() {
        return mAllClaimedRoutesByUser;
    }

    public Map<String, Route> getNewClaimedRoutes() { return  mNewClaimedRoutesByUser; }

    public void addClaimedRoute(String username, Route route) {
        if(mAllClaimedRoutesByUser.get(username) != null)   // getting nullPtEx so added check -EPM
            mAllClaimedRoutesByUser.get(username).add(route);
        else {
            mAllClaimedRoutesByUser.put(username, new ArrayList<Route>());
            mAllClaimedRoutesByUser.get(username).add(route);
        }
        mNewClaimedRoutesByUser.put(username, route);
        mClaimedRoutesByID.put(route.getId(), route);
    }

    public void clearNewClaimedRoutes() {
        mNewClaimedRoutesByUser = new HashMap<>();
    }


    public boolean checkIfRouteClaimed(int routeID) {
        return mClaimedRoutesByID.containsKey(routeID);
    }

    public List<String> getPlayerWithLongestRoute() {
        return mCurrentGame.getCurrentGameState().getLongestUser();
    }

    public void setPlayerWithLongestRoute(String playerWithLongestRoute) {
        this.playerWithLongestRoute = playerWithLongestRoute;
    }



    public List<DestinationCard> getDestinationOptions() {
        return destinationOptions;
    }

    public void setDestinationOptions(List<DestinationCard> drawnDestinations) {
        this.destinationOptions = drawnDestinations;
    }

    public void addDestinationOptionsToMyDestinations() {
        for (DestinationCard d : destinationOptions) {
            mUser.getDestCards().add(d);
            getPlayer().setNumDestCards(getPlayer().getNumDestCards() + 1);
        }
        destinationOptions = null;
    }

    public void resetTrainCardCount() {     // added for demo testing -EPM 10-31-17
        mTrainCardCount = null;
    }


}
