package Command;

import java.util.Map;

import Communication.History;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class OtherDestinationRemovalResponseCommandData extends Command {
    //private Map<String , Integer> playerDestinationCardCounts;

    //username of player to throw away card
    private String username;
    //updated count of destination cards for the player
    private int playerDestinationCardCount;
    private int updatedDestinationDeckCount;
    private History history;

    public OtherDestinationRemovalResponseCommandData(){ this.setType("DestinationRemovalRequestCommand");}

    public OtherDestinationRemovalResponseCommandData(String username, int playerDestinationCardCount, int updatedDestinationDeckCount, History history) {
        //this.playerDestinationCardCounts = playerDestinationCardCounts;
        this.username = username;
        this.playerDestinationCardCount = playerDestinationCardCount;
        this.updatedDestinationDeckCount = updatedDestinationDeckCount;
        this.history = history;
        setSuccess(true);
        this.setType("OtherDestinationRemovalResponseCommand");
    }

    public OtherDestinationRemovalResponseCommandData(String message) {
        setSuccess(false);
        setMessage(message);
        this.setType("OtherDestinationRemovalResponseCommand");
    }

    public String getUsername() {
        return username;
    }

    public int getPlayerDestinationCardCount() {
        return playerDestinationCardCount;
    }

    public int getUpdatedDestinationDeckCount() {
        return updatedDestinationDeckCount;
    }

    public History getHistory() {
        return history;
    }
}
