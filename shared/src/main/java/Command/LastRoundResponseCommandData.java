package Command;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class LastRoundResponseCommandData extends Command {

    String authToken;
    String gameID;

    public LastRoundResponseCommandData(String authToken, String gameID) {
        setType("LastRoundResponseCommand");
        setSuccess(true);
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public LastRoundResponseCommandData() {
        this.setType("LastRoundResponseCommand");
    }

    public LastRoundResponseCommandData(String message){
        this.setType("LastRoundResponseCommand");
        setMessage(message);
        setSuccess(false);
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameID() {
        return gameID;
    }
}
