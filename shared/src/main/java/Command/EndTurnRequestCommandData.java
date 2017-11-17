package Command;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class EndTurnRequestCommandData extends Command{

    String authToken;
    String gameID;

    public EndTurnRequestCommandData(String auth, String id){
        this.setType("EndTurnRequestCommand");
        this.authToken = auth;
        this.gameID = id;
    }

    public EndTurnRequestCommandData(){
        this.setType("EndTurnRequestCommand");
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameID() {
        return gameID;
    }
}
