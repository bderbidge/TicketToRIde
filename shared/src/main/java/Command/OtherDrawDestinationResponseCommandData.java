package Command;

import Command.Command;
import Communication.History;

/**
 * Created by emmag on 11/8/2017.
 */

public class OtherDrawDestinationResponseCommandData extends Command {
    //username of player to throw away card
    private String username;
    //updated count of destination cards for the player
    private int playerDestinationCardCount;
    private int updatedDestinationDeckCount;
    private History history;

    public OtherDrawDestinationResponseCommandData(){ this.setType("DestinationRemovalRequestCommand");}

    public OtherDrawDestinationResponseCommandData(String username, int playerDestinationCardCount, int updatedDestinationDeckCount, History history) {
        //this.playerDestinationCardCounts = playerDestinationCardCounts;
        this.username = username;
        this.playerDestinationCardCount = playerDestinationCardCount;
        this.updatedDestinationDeckCount = updatedDestinationDeckCount;
        this.history = history;
        setSuccess(true);
        this.setType("OtherDrawDestinationResponseCommand");
    }

    public OtherDrawDestinationResponseCommandData(String message) {
        setSuccess(false);
        setMessage(message);
        this.setType("OtherDrawDestinationResponseCommand");
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
