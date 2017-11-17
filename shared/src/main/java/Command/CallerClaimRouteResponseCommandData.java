package Command;

import java.util.List;

import Command.Command;
import Communication.History;
import Communication.Route;
import Communication.TrainCard;

/**
 * Created by emmag on 11/13/2017.
 */

public class CallerClaimRouteResponseCommandData extends Command {

    History history;
    Route route;
    List<String> longestRoutePlayer;
    List<TrainCard> trainHand;
    int deckCardCount;



    public CallerClaimRouteResponseCommandData(Route r, List<TrainCard> trainCards, int deckCardCount, List<String> longUser, History h){
        this.setType("CallerClaimRouteResponseCommand");
        setSuccess(true);
        this.trainHand = trainCards;
        this.deckCardCount = deckCardCount;
        this.history = h;
        this.route = r;
        this.longestRoutePlayer = longUser;
    }

    public CallerClaimRouteResponseCommandData(){
        this.setType("CallerClaimRouteResponseCommand");
    }

    public CallerClaimRouteResponseCommandData(String message) {
        this.setType("CallerClaimRouteResponseCommand");
        setSuccess(false);
        setMessage(message);
    }

    public History getHistory() {
        return history;
    }

    public Route getRoute() {
        return route;
    }

    public List<TrainCard> getTrainHand() {
        return trainHand;
    }

    public List<String> getLongestRoutePlayer() {
        return longestRoutePlayer;
    }

    public int getDeckCardCount() {
        return deckCardCount;
    }
}
