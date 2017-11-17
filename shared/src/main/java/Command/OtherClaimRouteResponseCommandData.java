package Command;

import java.util.List;

import Communication.History;
import Communication.Route;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class OtherClaimRouteResponseCommandData extends Command{

    History history;
    Route route;
    List<String> longestRoutePlayer;
    String username;
    int deckCardCount;



    public OtherClaimRouteResponseCommandData(String username, Route r, int deckCardCount, List<String> longUser, History h){
        this.setType("OtherClaimRouteResponseCommand");
        this.username = username;
        this.deckCardCount = deckCardCount;
        this.history = h;
        this.route = r;
        this.longestRoutePlayer = longUser;
        setSuccess(true);
    }

    public OtherClaimRouteResponseCommandData(){
        this.setType("OtherClaimRouteResponseCommand");
    }

    public OtherClaimRouteResponseCommandData(String message) {
        this.setType("OtherClaimRouteResponseCommand");
        setSuccess(false);
        setMessage(message);
    }

    public String getUsername() {
        return username;
    }

    public History getHistory() {
        return history;
    }

    public Route getRoute() {
        return route;
    }

    public List<String> getLongestRoutePlayer() {
        return longestRoutePlayer;
    }

    public int getDeckCardCount() {
        return deckCardCount;
    }
}
