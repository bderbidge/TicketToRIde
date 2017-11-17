package Command;

/**
 * Created by autumnchapman on 10/5/17.
 */

public class StartGameRequestCommandData extends Command {

    String gameID;
    String authToken;

    /**
     * required public constructor, sets Command type
     */
    public StartGameRequestCommandData(){
        this.setType("StartGameCommand");
    }

    /**
     * public constructor
     * @param game String id of game to be started
     * @param auth String authtoken of calling client
     */
    public StartGameRequestCommandData(String game, String auth){
        this.setType("StartGameCommand");
        this.gameID = game;
        this.authToken = auth;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
}
