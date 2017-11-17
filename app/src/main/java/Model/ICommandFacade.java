package Model;

import java.util.List;
import java.util.Set;

import Communication.Chat;
import Communication.DestinationCard;
import Communication.Game;
import Communication.History;
import Communication.Player;
import Communication.Route;
import Communication.TrainCard;
import Communication.User;
import RequestResult.LoginRegisterResult;

/**
 * Created by emmag on 10/9/2017.
 */

public interface ICommandFacade {

    /**
     * updates the model based on the login or register result from the Server
     * @param loginRegisterResult
     */
    public void resultReturn(LoginRegisterResult loginRegisterResult);

    /**
     * notify the observers of the specified error
     * @param errorMessage
     */
    public void notifyOfCommandError(String errorMessage);

    public void startGame(User user, Game game);

    public void updateMyPlayer(Player player);

    //public boolean updatePlayerInfo(String username, int points, int numCards, Set<Route> claimedRoutes);



    public void updateFaceUpTrains(TrainCard[] faceUpTrains);

    public void addTrainCardFromDeck(TrainCard trainCard, int userCount, int deckCount);

    public void addTrainCardFromYard(TrainCard trainCard, int userCount, int downDeckCount, TrainCard[] updatedYard);

    public void updatePlayersTrainCardFromDeck(String username, int numberOfTrainCards, int deckCount);

    public void updatePlayersTrainCardFromTrainYard(String username, int numberOfTrainCards, int trainDeckCount, TrainCard[] yard);


    public void setDestinationOptions(List<DestinationCard> drawnDestinations, int destDeckCount);

    public void finishRemovingDestinations(int myDestCount, int destDeckCount);

    public void updatePlayersDestinationCardNumber(String username, int numberOfDestinationCards, int deckCount);


    public void addMyRoute(Route route, List<TrainCard> updatedHand, int deckCardCount, List<String> longestPlayer);

    //public void updatePlayersTrainNumber(String username, int numberOfTrains);

    public void addPlayerClaimedRoute(String username, Route route, int deckCardCount, List<String> longestPlayer);

    //public void updatePlayerWithLongestRoute(String username);


    public void addChat(Chat chat);

    public void addHistory(History history);


    public void startNextPlayersTurn(String username);

    public void startLastRound();

    public void endGame(List<Player> finalPlayers);

}
