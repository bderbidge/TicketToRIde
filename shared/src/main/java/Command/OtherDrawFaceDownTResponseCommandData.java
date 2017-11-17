package Command;

import Communication.History;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class OtherDrawFaceDownTResponseCommandData extends Command {

     int playerTrainCardCount;
     String playerUpdatedUsername;
     int updatedTrainDeckCount;
     History history;

    public OtherDrawFaceDownTResponseCommandData() {
        this.setType("OtherDrawFaceDownTResponseCommand");
    }

    public OtherDrawFaceDownTResponseCommandData(int playerTrainCardCount, String playerUpdatedUsername, int updatedTrainDeckCount, History history) {
        this.playerTrainCardCount = playerTrainCardCount;
        this.playerUpdatedUsername = playerUpdatedUsername;
        this.updatedTrainDeckCount = updatedTrainDeckCount;
        this.history = history;
        this.setSuccess(true);
        this.setType("OtherDrawFaceDownTResponseCommand");
    }

    public OtherDrawFaceDownTResponseCommandData(String message) {
        setType("OtherDrawFaceDownTResponseCommand");
        setMessage(message);
        setSuccess(false);
    }

    public int getPlayerTrainCardCount() {
        return playerTrainCardCount;
    }

    public String getPlayerUpdatedUsername() {
        return playerUpdatedUsername;
    }

    public int getUpdatedTrainDeckCount() {
        return updatedTrainDeckCount;
    }

    public History getHistory() {
        return history;
    }
}
