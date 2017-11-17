package Communication;

import java.util.List;
import java.util.Map;

import RequestResult.LoginRegisterResult;
import Command.Command;
import DataAccess.ServerModelRoot;

/**
 * Created by autumnchapman on 9/28/17.
 */

public class ServerFacade implements IServer {


    /**
     * public empty constructor
     */
    public ServerFacade(){
    }

    /**
     * calls the login method on the ServerModelRoot
     * @param username String
     * @param password String
     * @return LoginRegisterResult
     */
    @Override
    public LoginRegisterResult login(String username, String password) {
        return ServerModelRoot.instance().login(username, password);
    }

    /**
     * calls the register method on the ServerModelRoot
     * @param username String
     * @param password String
     * @return LoginRegisterResult
     */
    @Override
    public LoginRegisterResult register(String username, String password) {
        return ServerModelRoot.instance().register(username, password);

    }

    /**
     * I have no idea what this is, it's only usages that come up are in the app
     * @param command
     * @return
     */
    @Override
    public Command command(Command command) { return null; }

    /**
     * Calls create game in the ServerModelRoot
     * @param gameName String
     * @param numPlayers String
     * @return current GamesList (now contains new created game)
     */
    public String createGame(String gameName, int numPlayers){ return ServerModelRoot.instance().createGame(gameName, numPlayers); }

    /**
     * Calls joinGame in the ServerModelRoot
     * @param player Player to join
     * @param gameID String id of game to join
     * @return Game Object that the player joined
     */
    public Game joinGame(Player player, String gameID){return ServerModelRoot.instance().joinGame(player, gameID);}

    /**
     * Calls getGamesList in the ServerModelRoot
     * @return the current GamesList object
     */
    public GamesList getGameList() { return ServerModelRoot.instance().getGamesList(); }

    /**
     * Calls getGameState on specified game in the ServerModelRoot
     * @param id String id of game to have its state gotten
     * @return the GameState object
     */
    public GameState getGameState(String id) {return ServerModelRoot.instance().getGameState(id); }

    /**
     * Calls startGame on the specified game in the ServerModelRoot
     * @param id String id of game to be started
     * @return the GameState object of the newly started Game
     */
    public Game startGame(String id) { return ServerModelRoot.instance().startGame(id);}

    /**
     * Calls Poll in the ServerModelRoot
     * @param authToken String authtoken of calling client
     * @return Game object that was polled
     */
    public Game Poll(String authToken){
        return ServerModelRoot.instance().Poll(authToken);
    }

    public void logChat(String gameID, Chat c){
        ServerModelRoot.instance().logChat(gameID, c);
    }

    public void logHistory(String gameID, History h){
        ServerModelRoot.instance().logHistory(gameID, h);
    }

    public void returnDCards(String gameID, String userName, List<DestinationCard> dc){
        ServerModelRoot.instance().returnDCards(gameID, userName, dc);
    }

    public String getUserName(String auth){
        return ServerModelRoot.instance().getUserName(auth);
    }

    public User getUser(String userName){
        return ServerModelRoot.instance().getUser(userName);
    }

    public String getGameID(String userName){
        return ServerModelRoot.instance().getGameID(userName);
    }

    public Map<String, Integer> getPlayerDCardCounts(String gameID){
        return ServerModelRoot.instance().getPlayerDCardCounts(gameID);
    }

    public int getDDeckCount(String gameID){
        return ServerModelRoot.instance().getDDeckCount(gameID);
    }

    public int getPlayerDCardCount(String gameID, String userName){
        return ServerModelRoot.instance().getPlayerDCardCount(gameID, userName);
    }

    public String getColor(String gameID, String userName){
        return ServerModelRoot.instance().getColor(gameID, userName);
    }

    public Game getGame(String gameID){
        return ServerModelRoot.instance().getGame(gameID);
    }

    public boolean drawUpTCard(TrainCard tc, String gameID, String userName){
        return ServerModelRoot.instance().drawUpTCard(tc, gameID, userName);
    }

    public TrainCard drawDownTCard(String gameID, String userName){
        return ServerModelRoot.instance().drawDownTCard(gameID, userName);
    }

    public List<DestinationCard> drawDCard(String gameID, String userName){
        return ServerModelRoot.instance().drawDCard(gameID, userName);
    }

    public String endTurn(String gameID, String userName){
        return ServerModelRoot.instance().endTurn(gameID, userName);
    }

    public boolean isLastRound(String gameID){
        return ServerModelRoot.instance().isLastRound(gameID);
    }

    public void claimRoute(Route route, String gameID, String userName, String cColor){
        ServerModelRoot.instance().claimRoute(route, gameID, userName, cColor);
    }

    public List<Player> getEndGameData(String gameID){
        return ServerModelRoot.instance().getEndGameData(gameID);
    }
}
