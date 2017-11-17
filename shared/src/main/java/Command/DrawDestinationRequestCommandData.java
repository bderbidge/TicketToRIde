package Command;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class DrawDestinationRequestCommandData extends Command {
    private String mAuthToken;
    private String mGameID;

    public DrawDestinationRequestCommandData(String authToken, String gameID) {
        setType("DrawDestinationRequestCommand");
        mAuthToken = authToken;
        mGameID = gameID;
        setSuccess(true);
    }

    public DrawDestinationRequestCommandData() {
        setType("DrawDestinationRequestCommand");
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public void setAuthToken(String authToken) {
        this.mAuthToken = authToken;
    }

    public String getGameID() {
        return mGameID;
    }

    public void setGameID(String gameID) {
        this.mGameID = gameID;
    }
}
