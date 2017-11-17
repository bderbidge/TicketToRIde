package Command;

import Communication.GameState;

/**
 * Created by autumnchapman on 10/2/17.
 */

public class UpdateGameStateResponseCommandData extends Command {

    String gameID;
    GameState gameState;

    /**
     * required public constructor, sets Command type
     */
    public UpdateGameStateResponseCommandData(){
        this.setType("UpdateGameStateResponseCommand");
    }

    /**
     * public constructor
     * @param id String id of game we're returning the current state of
     * @param gs GameState of current game from Server
     */
    public UpdateGameStateResponseCommandData(String id, GameState gs){
        this.setType("UpdateGameStateResponseCommand");
        this.gameID = id;
        this.gameState = gs;
        this.setSuccess(true);
    }

    public UpdateGameStateResponseCommandData(String message) {
        setSuccess(false);
        setMessage(message);
        this.setType("UpdateGameStateResponseCommand");
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
