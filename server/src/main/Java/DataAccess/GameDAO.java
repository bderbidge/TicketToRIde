package DataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import Communication.DestinationCard;
import Communication.Game;
import Communication.GameState;
import Communication.GamesList;
import Communication.Player;
import Communication.Route;
import Communication.TrainCard;
import Communication.User;
import sun.security.krb5.internal.crypto.Des;

/**
 * Created by brandonderbidge on 9/30/17.
 */

public class GameDAO {

    private ServerModelRoot db;

    public GameDAO(ServerModelRoot db) {
        this.db = db;
    }


    public String addGame(String name, int numPlayers){

        Map<String, Game> gameList = db.getGameList();
        Map<String, List<User>> gamesToUser = db.getGameToUsers();
        String ID= UUID.randomUUID().toString();
        Map<String, Player> playerMap = new HashMap<>();


        //create a game and add a gamestate to it then put it in a list of games.
        GameState state = new GameState(false,playerMap ,0);
        Game game = new Game(ID, name, numPlayers, state);

        List<User> users = new ArrayList<>();
        gamesToUser.put(ID, users);
        //db.setGameToUsers(gamesToUser);

        gameList.put(ID, game);
        //db.setGameList(gameList);

        return ID;
    }


    //Join a game based on the game id
    public Game joinGame(Player player, String gameID){

        Map<String, Game> gameList = db.getGameList();
        Map<String, GameState> playerToState = db.getPlayerToState();
        Map<String, Game> userToGameList = db.getUserToGameList();
        Map<String, List<User>> GameToUser = db.getGameToUsers();
        Map<String, User> registeredUsers = db.getRegisteredUsers();

        Game game = gameList.get(gameID);

        //If the max number of players have been joined then don't allow a join.
        if(game.getMaxNumberOfPlayers() > game.getCurrentGameState().getCurrentNumberOfPlayers()) {

            game.getCurrentGameState().addPlayer(player);

            game.getCurrentGameState().incrementVersion();

            String userName = player.getUsername();
            playerToState.put(userName, game.getCurrentGameState());
            userToGameList.put(userName, game);

            List<User> users = GameToUser.get(gameID);
            users.add(registeredUsers.get(userName));
            GameToUser.put(gameID, users);

            return game;
        }
        else {
            return null;
        }
    }


    //State the game based on the id passed in.
    public Game startGame(String ID) throws Exception{
        Map<String, Game> gameList = db.getGameList();
        Map<String, List<TrainCard>> upDeck = db.getUpDeck();
        Map<String, List<TrainCard>> downDeck = db.getDownDeck();
        Map<String, Game> gameListStarted = db.getGameListStarted();

        //Pass out cards to players and get the size of the remaining decks
        initialDCards(ID);
        initialTCards(ID);

        //Get the remaining size of the decks
        List<TrainCard> UpTDeck = upDeck.get(ID);

        //Remove the game from the list of unstarted games
        Game game = gameList.get(ID);
        gameList.remove(ID);
        gameListStarted.put(game.getGameID(), game);
        int dDeckSize = getDCardSize(ID);
        int tDeckSize = getTCardSize(ID);

        //Start the game
        //this starts the gameState
        game.startGame(dDeckSize, tDeckSize, UpTDeck);

        //GameState gs = game.getCurrentGameState();

        //return altered tables to ServerModelRoot
        //db.setUpDeck(upDeck);
        //db.setDownDeck(downDeck);
        //db.setGameList(gameList);
        //db.setGameListStarted(gameListStarted);

        return game;
    }


    //Get a list of all the current games
    public GamesList getGameList()
    {
        Map<String, Game> gameList = db.getGameList();
        GamesList list = new GamesList(gameList);

        //db.setGameList(gameList);
        return list;
    }


    //get the state of the game id passed in
    public GameState getGameState(String ID){

        Game game = null;

        Map<String, Game> gameList = db.getGameList();
        Map<String, Game> gameListStarted = db.getGameListStarted();
        if(gameList.containsKey(ID)){
            game = gameList.get(ID);
        }
        if(gameListStarted.containsKey(ID)){
            game = gameListStarted.get(ID);
        }
        GameState gameState = game.getCurrentGameState();

        //db.setGameList(gameList);
        return gameState;

    }

    public Game getGameFromUserName(String username){

        Map<String, Game> userToGameList = db.getUserToGameList();
        Game game = userToGameList.get(username);

        //db.setUserToGameList(userToGameList);
        return game;

    }

    public String getGameIDFromUserName(String useName){
        Map<String, Game> userToGameList = db.getUserToGameList();
        if(!userToGameList.containsKey(useName)){
            return null;
        }
        else{
            Game game = userToGameList.get(useName);
            return game.getGameID();
        }
    }

    public void returnDCards(String gameID, String userName, List<DestinationCard> d){
        //talk about how this should be done, through games or through users...
        //this could easily return the card to the deck but not take it from
        //the player/user
        Game game = db.getGameListStarted().get(gameID);
        int currentDeckCount = game.getDestinationDeckCount();
        game.setDestinationDeckCount(currentDeckCount+d.size());
        game.getCurrentGameState().returnDCards(userName, d);
    }

    public void initialDCards(String gameID){
        Map<String, List<User>> gamesToUsers = db.getGameToUsers();
        Map<String, List<DestinationCard>> gamesToDDecks = db.getGamesToDDecks();
        List<DestinationCard> deck = gamesToDDecks.get(gameID);

        List<User> users = gamesToUsers.get(gameID);
        for (User u : users) {
            List<DestinationCard> cardToGive = new ArrayList<>();
            DestinationCard first = deck.get(0);
            DestinationCard second = deck.get(1);
            DestinationCard third = deck.get(2);
            cardToGive.add(first);
            cardToGive.add(second);
            cardToGive.add(third);
            deck.remove(first);
            deck.remove(second);
            deck.remove(third);
            u.setDestinationCards(cardToGive);
        }
        //db.setGameToUsers(gamesToUsers);
        //db.setGamesToDDecks(gamesToDDecks);
    }

    public int getDCardSize(String gameID){
        Map<String, List<DestinationCard>> gamesToDDecks = db.getGamesToDDecks();
        List<DestinationCard> deck = gamesToDDecks.get(gameID);
        return deck.size();
    }

    public void initialTCards(String gameID){
        Map<String, List<User>> gamesToUsers = db.getGameToUsers();
        Map<String, List<TrainCard>> gameToTDecks = db.getDownDeck();
        List<TrainCard> deck = gameToTDecks.get(gameID);

        List<User> users = gamesToUsers.get(gameID);

        for (User u: users) {
            List<TrainCard> cardToGive = new ArrayList<>();
            TrainCard first = deck.get(0);
            TrainCard second = deck.get(1);
            TrainCard third = deck.get(2);
            TrainCard fourth = deck.get(3);
            cardToGive.add(first);
            cardToGive.add(second);
            cardToGive.add(third);
            cardToGive.add(fourth);
            deck.remove(first);
            deck.remove(second);
            deck.remove(third);
            deck.remove(fourth);
            u.setTrainCards(cardToGive);
        }
        //db.setGameToUsers(gamesToUsers);
        //db.setDownDeck(gameToTDecks);
    }

    public int getTCardSize(String gameID){
        Map<String, List<TrainCard>> downDeck= db.getDownDeck();
        List<TrainCard> deck = downDeck.get(gameID);
        return deck.size();

    }

    public void drawUpTCard(String gameID, String userName){
        Map<String, Game> gameListStarted = db.getGameListStarted();
        Map<String, List<TrainCard>> downDeck = db.getDownDeck();
        Map<String, List<TrainCard>> upDeck = db.getUpDeck();

        List<TrainCard> dDeck = downDeck.get(gameID);
        List<TrainCard> uDeck = upDeck.get(gameID);
        Game game = gameListStarted.get(gameID);
        GameState gs = game.getCurrentGameState();

        game.setTrainDeckCount(dDeck.size());
        game.setUpDeck(uDeck);
        gs.drawTCard(userName);
        return;
    }

    public void drawDownTCard(String gameID, String userName){
        Game game = getGameFromUserName(userName);
        int currentCount = game.getTrainDeckCount();
        game.setTrainDeckCount(currentCount -1);
        game.getCurrentGameState().drawTCard(userName);
    }

    public void drawDCards(String gameID, String userName, List<DestinationCard> dc){
        Game game = getGameFromUserName(userName);
        int currentCount = game.getDestinationDeckCount();
        game.setDestinationDeckCount(currentCount - dc.size());
        game.getCurrentGameState().drawDCard(userName, dc.size());

    }

    public String endTurn(String gameID, String userName){
        Map<String, Game> gameListStarted = db.getGameListStarted();
        Game game = gameListStarted.get(gameID);
        GameState gs = game.getCurrentGameState();
        return gs.endTurn(userName);
    }

    public void claimRoute(Route route, String gameID, String userName){
        Map<String, Game> gameListStarted = db.getGameListStarted();
        Game game = gameListStarted.get(gameID);
        game.setTrainDeckCount(game.getTrainDeckCount()+route.getLength());
        GameState state = game.getCurrentGameState();
        state.claimRoute(route, userName);
    }

    public void startLastRound(String gameID, String userName) {
        Map<String, Game> gameListStarted = db.getGameListStarted();
        Game game = gameListStarted.get(gameID);
        game.startLastRound(userName);
    }

    public boolean isLastRound(String gameID){
        Map<String, Game> gameListStarted = db.getGameListStarted();
        Game game = gameListStarted.get(gameID);
        return game.getCurrentGameState().isLastRound();
    }


}
