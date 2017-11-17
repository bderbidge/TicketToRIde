package Command;

import Communication.History;
import Communication.Route;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class ClaimRouteRequestCommandData extends Command{

    String authToken;
    String gameID;
    Route route;
    String claimingColor;

    public ClaimRouteRequestCommandData(String auth, String id, Route r, String cColor){
        this.setType("ClaimRouteRequestCommand");
        this.authToken = auth;
        this.gameID = id;
        this.route = r;
        this.claimingColor = cColor;
    }

    public ClaimRouteRequestCommandData(){
        this.setType("ClaimRouteRequestCommand");
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameID() {
        return gameID;
    }

    public Route getRoute() {
        return route;
    }

    public String getClaimingColor() {
        return claimingColor;
    }
}
