package Command;

import Communication.TrainCard;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class DrawFaceDownTCardRequestCommandData extends Command {

    String authToken;
    String gameID;

    public DrawFaceDownTCardRequestCommandData(String authToken, String gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.setType("DrawFaceDownTCardRequestCommand");
        this.setSuccess(true);
    }

    public DrawFaceDownTCardRequestCommandData() {
        this.setType("DrawFaceDownTCardRequestCommand");
    }

    public DrawFaceDownTCardRequestCommandData(String message){
        setType("DrawFaceDownTCardRequestCommand");
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
