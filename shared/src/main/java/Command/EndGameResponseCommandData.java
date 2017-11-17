package Command;

import java.util.List;

import Communication.Player;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class EndGameResponseCommandData extends Command {
    private String mGameID;
    private List<Player> playerList;

    public EndGameResponseCommandData(String gameID, List<Player> playerList) {
        setType("EndGameResponseCommand");
        this.mGameID = gameID;
        this.playerList = playerList;
        setSuccess(true);
    }

    public EndGameResponseCommandData() {setType("EndGameResponseCommand");}

    public String getGameID() {
        return mGameID;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
