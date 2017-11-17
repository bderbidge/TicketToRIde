package Command;

import java.util.ArrayList;
import java.util.List;

import Communication.History;
import Communication.TrainCard;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class CallerDrawFaceUpTResponseCommand extends CallerDrawFaceUpTResponseCommandData implements ICommand{

    public CallerDrawFaceUpTResponseCommand(TrainCard card, int updatedPlayerTrainCardCount, int updatedTrainDeckCount, TrainCard[] updatedFaceUpDeck, List<History> history) {
        super(card, updatedPlayerTrainCardCount, updatedTrainDeckCount, updatedFaceUpDeck, history);
    }

    public CallerDrawFaceUpTResponseCommand(String message) {super(message);}

    public CallerDrawFaceUpTResponseCommand(){super();}

    @Override
    public Object execute() {
        CommandFacade.instance().addTrainCardFromYard(getCard(), getUpdatedPlayerTrainCardCount(), getUpdatedTrainDeckCount(), getUpdatedFaceUpDeck());
        for (History history: getHistory()) {
            CommandFacade.instance().addHistory(history);
        }
        return null;
    }
}
