package Command;

import Communication.Player;

/**
 * Created by autumnchapman on 10/2/17.
 */

public class JoinGameCommandData extends Command {

    Player playerJoining;
    String gameToJoinID;
    String authToken;

    /**
     * required public constructor, sets Command type
     */
    public JoinGameCommandData(){
        this.setType("JoinGameCommand");
    }

    /**
     * public constructor
     * @param pj Player joining a game
     * @param id String game id of the game being joined
     * @param auth String auth token of the calling client
     */
    public JoinGameCommandData(Player pj, String id, String auth){
        this.setType("JoinGameCommand");
        this.playerJoining = pj;
        this.gameToJoinID = id;
        this.authToken = auth;
    }

    public Player getPlayerJoining() {
        return playerJoining;
    }

    public void setPlayerJoining(Player playerJoining) {
        this.playerJoining = playerJoining;
    }

    public String getGameToJoinID() {
        return gameToJoinID;
    }

    public void setGameToJoin(String gameToJoin) {
        this.gameToJoinID = gameToJoin;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
