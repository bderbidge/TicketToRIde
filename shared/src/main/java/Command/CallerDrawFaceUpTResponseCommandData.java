package Command;

import java.util.List;

import Communication.History;
import Communication.TrainCard;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class CallerDrawFaceUpTResponseCommandData extends Command {

     TrainCard card;
     int updatedPlayerTrainCardCount;
     int updatedTrainDeckCount;
     TrainCard[] updatedFaceUpDeck;
     List<History> history;

    public CallerDrawFaceUpTResponseCommandData(TrainCard card, int updatedPlayerTrainCardCount, int updatedTrainDeckCount, TrainCard[] updatedFaceUpDeck, List<History> history) {
        this.card = card;
        this.updatedPlayerTrainCardCount = updatedPlayerTrainCardCount;
        this.updatedTrainDeckCount = updatedTrainDeckCount;
        this.updatedFaceUpDeck = updatedFaceUpDeck;
        this.history = history;
        setSuccess(true);
        setType("CallerDrawFaceUpTResponseCommand");
    }

    public CallerDrawFaceUpTResponseCommandData(){
        setType("CallerDrawFaceUpTResponseCommand");
    }

    public CallerDrawFaceUpTResponseCommandData(String message){
        setMessage(message);
        setSuccess(false);
        setType("CallerDrawFaceUpTResponseCommand");
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

    public TrainCard[] getUpdatedFaceUpDeck() {
        return updatedFaceUpDeck;
    }

    public List<History> getHistory() {
        return history;
    }
}
