package Command;

import Communication.History;
import Communication.TrainCard;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class CallerDrawFaceDownTResponseCommand extends CallerDrawFaceDownTResponseCommandData implements ICommand{

    public CallerDrawFaceDownTResponseCommand() {
    }

    public CallerDrawFaceDownTResponseCommand(TrainCard card, int updatedPlayerTrainCardCount, int updatedTrainDeckCount, History history) {
        super(card, updatedPlayerTrainCardCount, updatedTrainDeckCount, history);
    }

    public CallerDrawFaceDownTResponseCommand(String message) {
        super(message);
    }

    @Override
    public Object execute() {
        CommandFacade.instance().addTrainCardFromDeck(getCard(), getUpdatedPlayerTrainCardCount(), getUpdatedTrainDeckCount());
        CommandFacade.instance().addHistory(getHistory());
        return null;
    }
}
