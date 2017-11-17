package Command;

import Communication.GamesList;

/**
 * Created by autumnchapman on 9/30/17.
 */

public class UpdateGameListResponseCommandData extends Command {

    private GamesList gameList;

    /**
     * required public constructor, sets Command type
     */
    public UpdateGameListResponseCommandData (){
        this.setType("UpdateGameListResponseCommand");
    }

    /**
     * public constructor
     * @param gl GamesList currently in the Server to return to the Client
     */
    public UpdateGameListResponseCommandData(GamesList gl){
        this.setType("UpdateGameListResponseCommand");
        this.gameList = gl;
        this.setSuccess(true);
    }

    public GamesList getGameList() {return gameList;}

    public void setGameList(GamesList gameList) {this.gameList = gameList;}
}
