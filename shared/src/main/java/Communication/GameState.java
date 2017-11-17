package Communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import RouteCalculator.Exception.RouteCalcException;
import RouteCalculator.RouteCalculator;
import RouteCalculator.Util;


/**
 * Created by emmag on 9/30/2017.
 */

public class GameState {
    private boolean mGameStarted;
    private Map<String, Player> mCurrentPlayers;
    private int mCurrentNumberOfPlayers;
    private int gameStateVersion;
    private List<Player> orderedPlayers;
    private int playerWhoseTurnIndex;
    private Player playerWhoseTurn;
    private List<String> mlongestUser;
    private boolean isLastRound;
    private String lastRoundIndicator;
    private boolean finalTurn;


    public GameState() {}

    public GameState(boolean gameStarted, Map<String, Player> currentPlayers, int currentNumberOfPlayers) {
        mGameStarted = gameStarted;
        mCurrentPlayers = currentPlayers;
        mCurrentNumberOfPlayers = currentNumberOfPlayers;
        gameStateVersion = 0;
        isLastRound = false;
        finalTurn = false;
    }

    public List<String> getLongestUser() {
        return mlongestUser;
    }

    public void setLongestUser(List<String> longestUser) {
        if(mlongestUser != null) {
            for (String name: mlongestUser) {
                Player p = mCurrentPlayers.get(name);
                p.setLongestRoute(false);
            }
        }
        for (String name: longestUser) {
            Player p = mCurrentPlayers.get(name);
            p.setLongestRoute(true);
        }
        this.mlongestUser = longestUser;
    }

    public boolean isGameStarted() {
        return mGameStarted;
    }

    public Map<String, Player> getCurrentPlayers() {
        return mCurrentPlayers;
    }

    public int getCurrentNumberOfPlayers() {
        return mCurrentNumberOfPlayers;
    }

    public boolean isPlayerInGame(String username) {
        return getCurrentPlayers().containsKey(username);
    }

    public boolean isLastRound() {
        return isLastRound;
    }

    public void setLastRound(boolean lastRound) {
        isLastRound = lastRound;
    }

    public void rebootCurrentPlayers() {
        mCurrentPlayers = new HashMap<String, Player>();
        for(Player p : orderedPlayers) {
            mCurrentPlayers.put(p.getUsername(), p);
        }
    }

    public Player getPlayer(String username) {
        if(isPlayerInGame(username)) {
            return mCurrentPlayers.get(username);
        }
        return null;
    }

    public boolean addPlayer(Player player) {
        //check if player is already in the game
        if(getPlayer(player.getUsername()) == null) {
            mCurrentPlayers.put(player.getUsername(), player);
            mCurrentNumberOfPlayers++;
            return true;
        }
        else { //player is already in the game
            return true;
        }
    }

    public int getGameStateVersion() {
        return gameStateVersion;
    }

    public void setGameStateVersion(int gameStateVersion) {
        this.gameStateVersion = gameStateVersion;
    }

    public void incrementVersion() {
        gameStateVersion++;
    }

    public void startGame() throws Exception {

        //// TODO: 10/28/2017 this is 1 <= for testing purposes
        if( 1 <= mCurrentNumberOfPlayers || mCurrentNumberOfPlayers < 6){
            mGameStarted = true;
            //orderedPlayers = new Player[mCurrentNumberOfPlayers];
            orderedPlayers = new ArrayList<Player>();
            //int i = 0;
            for (Player p : mCurrentPlayers.values()) {
                //orderedPlayers[i] = p;
                p.setNumDestCards(3);
                p.setNumTrainCards(4);
                p.setTotalPoints(0);
                p.setTrains(45);
                orderedPlayers.add(p);
                //i++;
            }
            playerWhoseTurnIndex = 0;
           // playerWhoseTurnIterator = orderedPlayers.iterator();
            //playerWhoseTurn = playerWhoseTurnIterator.next();
            playerWhoseTurn = orderedPlayers.get(playerWhoseTurnIndex);
        }
        else{
            Exception e = new Exception();
            throw e;
        }
    }

    public void startNextTurn() {
        playerWhoseTurnIndex++;
        if(playerWhoseTurnIndex >= orderedPlayers.size()) {
            playerWhoseTurnIndex = 0;
        }
        playerWhoseTurn = orderedPlayers.get(playerWhoseTurnIndex);
    }

    public String incrementLastTurn(){
        int index = playerWhoseTurnIndex +1;
        if(index >= orderedPlayers.size()) {
            index = 0;
        }
        return orderedPlayers.get(index).getUsername();
    }

    public void drawDCard(String userName, int numCards){
        Player p = mCurrentPlayers.get(userName);
        int currentNum = p.getNumDestCards();
        p.setNumDestCards(currentNum + numCards);
    }

    public void drawTCard(String userName){
        Player p = getCurrentPlayers().get(userName);
        int currentCardnum = p.getNumTrainCards();
        p.setNumTrainCards(currentCardnum+1);
    }

    public void returnDCards(String userName, List<DestinationCard> dc){
        Player p = mCurrentPlayers.get(userName);
        int currentNum = p.getNumDestCards();
        if(dc != null){
            currentNum = currentNum - dc.size();
        }
        p.setNumDestCards(currentNum);
    }

    public Player getPlayerWhoseTurn() {
       // return orderedPlayers[playerWhoseTurnIndex];
        return playerWhoseTurn;
    }

    public List<Player> getOrderedPlayers() {
        return orderedPlayers;
    }

    public String endTurn(String userName){
        if(!isLastRound){
            startNextTurn();
            Player p = orderedPlayers.get(playerWhoseTurnIndex);
            return p.getUsername();
        }
        else{
            String nowTurn = incrementLastTurn();
            if(nowTurn.equals(lastRoundIndicator)){
                if(!finalTurn){
                    finalTurn = true;
                    startNextTurn();
                    Player p = orderedPlayers.get(playerWhoseTurnIndex);
                    return p.getUsername();
                }
                else{
                    endGamePoints();
                    return null;
                }
            }
            else {
                startNextTurn();
                Player p = orderedPlayers.get(playerWhoseTurnIndex);
                return p.getUsername();
            }
        }
    }

    public void claimRoute(Route route, String userName){
        Player p = mCurrentPlayers.get(userName);
        p.claimRoute(route);
        calculateLongestUser();
    }

    private void calculateLongestUser(){
        Util util = new Util();
        RouteCalculator rc = new RouteCalculator(util.getAllCities());
        Map<Player, Integer> longestRoutes = new HashMap<>();
        for (Player p: orderedPlayers) {
            List toPass = new ArrayList(p.getRoutes());
            Integer lr = null;
            try {
                lr = rc.calculateLongestRoute(p, toPass);
            } catch (RouteCalcException e) {
                e.printStackTrace();
            }
            longestRoutes.put(p, lr);
        }

        int min = 0;
        for (Integer value : longestRoutes.values()) {
            if(value > min){
                min = value;
            }
        }

        List<String> longestUsers = new ArrayList<>();
        for (Map.Entry<Player, Integer> entry : longestRoutes.entrySet()) {
            Player key = entry.getKey();
            Integer length = entry.getValue();
            if(length == min){
                longestUsers.add(key.getUsername());
            }
        }
        setLongestUser(longestUsers);
    }

    public void startLastRound(String userName){
        isLastRound = true;
        lastRoundIndicator = userName;
    }

    //calculates points for people getting longest route
    private void endGamePoints(){
        for (Player p: orderedPlayers) {
            if(getLongestUser().contains(p.getUsername())){
                p.setLongestRoutePoints(true);
            }
        }
    }
}
