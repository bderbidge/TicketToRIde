package Command;

import java.util.List;

import Communication.History;
import Communication.TrainCard;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class OtherDrawFaceUpTResponseCommand extends OtherDrawFaceUpTResponseCommandData implements ICommand {

    public OtherDrawFaceUpTResponseCommand(int playerTrainCardCount, String playerUpdatedUsername, int updatedTrainDeckCount, TrainCard[] updatedFaceUpDeck, List<History> history) {
        super(playerTrainCardCount, playerUpdatedUsername, updatedTrainDeckCount, updatedFaceUpDeck, history);
    }

    public OtherDrawFaceUpTResponseCommand() {
        super();
    }

    public OtherDrawFaceUpTResponseCommand(String message) {
        super(message);
    }

    @Override
    public Object execute() {
        CommandFacade.instance().updatePlayersTrainCardFromTrainYard(getPlayerUpdatedUsername(), getPlayerTrainCardCount(), getUpdatedTrainDeckCount(), getUpdatedFaceUpDeck());
        for (History history: getHistory()) {
            CommandFacade.instance().addHistory(history);
        }
        return null;
    }
}
