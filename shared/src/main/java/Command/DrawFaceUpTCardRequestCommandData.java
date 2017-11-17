package Command;

import Communication.TrainCard;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class DrawFaceUpTCardRequestCommandData extends Command {

    String authToken;
    String gameID;
    TrainCard drawFaceUp;

    public DrawFaceUpTCardRequestCommandData(String authToken, String gameID, TrainCard drawFaceUp) {
        setType("DrawFaceUpTCardRequestCommand");
        this.authToken = authToken;
        this.gameID = gameID;
        this.drawFaceUp = drawFaceUp;
        setSuccess(true);
    }

    public DrawFaceUpTCardRequestCommandData(){
        setType("DrawFaceUpTCardRequestCommand");
    }

    public DrawFaceUpTCardRequestCommandData(String message){
        setType("DrawFaceUpTCardRequestCommand");
        setMessage(message);
        setSuccess(false);
    }

    public String getAuthToken() {
        return authToken;
    }

    public TrainCard getDrawFaceUp() {
        return drawFaceUp;
    }

    public String getGameID() {
        return gameID;
    }
}
