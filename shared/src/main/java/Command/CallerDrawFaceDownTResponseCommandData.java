package Command;

import java.util.List;

import Communication.History;
import Communication.TrainCard;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class CallerDrawFaceDownTResponseCommandData extends Command {

      TrainCard card;
      int updatedPlayerTrainCardCount;
      int updatedTrainDeckCount;
      History history;

    public CallerDrawFaceDownTResponseCommandData() {
        this.setType("CallerDrawFaceDownTResponseCommand");
    }

    public CallerDrawFaceDownTResponseCommandData(TrainCard card, int updatedPlayerTrainCardCount, int updatedTrainDeckCount, History history) {
        setType("CallerDrawFaceDownTResponseCommand");
        this.card = card;
        this.updatedPlayerTrainCardCount = updatedPlayerTrainCardCount;
        this.updatedTrainDeckCount = updatedTrainDeckCount;
        this.history = history;
        setSuccess(true);
    }

    public CallerDrawFaceDownTResponseCommandData(String message){
        setType("CallerDrawFaceDownTResponseCommand");
        setMessage(message);
        setSuccess(false);
    }

    public TrainCard getCard() {
        return card;
    }

    public int getUpdatedPlayerTrainCardCount() {
        return updatedPlayerTrainCardCount;
    }

    public int getUpdatedTrainDeckCount() {
        return updatedTrainDeckCount;
    }

    public History getHistory() {
        return history;
    }
}
