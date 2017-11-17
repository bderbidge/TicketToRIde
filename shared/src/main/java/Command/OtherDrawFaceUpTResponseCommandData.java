package Command;

import java.util.List;

import Communication.History;
import Communication.TrainCard;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class OtherDrawFaceUpTResponseCommandData extends Command{

    int playerTrainCardCount;
    String playerUpdatedUsername;
    int updatedTrainDeckCount;
    TrainCard[] updatedFaceUpDeck;
    List<History> history;

    public OtherDrawFaceUpTResponseCommandData(int playerTrainCardCount, String playerUpdatedUsername, int updatedTrainDeckCount, TrainCard[] updatedFaceUpDeck, List<History> history) {
        this.playerTrainCardCount = playerTrainCardCount;
        this.playerUpdatedUsername = playerUpdatedUsername;
        this.updatedTrainDeckCount = updatedTrainDeckCount;
        this.updatedFaceUpDeck = updatedFaceUpDeck;
        this.history = history;
        this.setType("OtherDrawFaceUpTResponseCommand");
        this.setSuccess(true);
    }

    public OtherDrawFaceUpTResponseCommandData() {
        this.setType("OtherDrawFaceUpTResponseCommand");
    }

    public OtherDrawFaceUpTResponseCommandData(String message) {
        this.setType("OtherDrawFaceUpTResponseCommand");
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

    public TrainCard[] getUpdatedFaceUpDeck() {
        return updatedFaceUpDeck;
    }

    public List<History> getHistory() {
        return history;
    }
}
