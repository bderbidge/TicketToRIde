package Command;

import Communication.History;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class OtherDrawFaceDownTResponseCommand extends OtherDrawFaceDownTResponseCommandData implements ICommand {

    public OtherDrawFaceDownTResponseCommand() {
    }

    public OtherDrawFaceDownTResponseCommand(int playerTrainCardCount, String playerUpdatedUsername, int updatedTrainDeckCount, History history) {
        super(playerTrainCardCount, playerUpdatedUsername, updatedTrainDeckCount, history);
    }

    public OtherDrawFaceDownTResponseCommand(String message) {
        super(message);
    }

    @Override
    public Object execute() {
        CommandFacade.instance().updatePlayersTrainCardFromDeck(getPlayerUpdatedUsername(), getPlayerTrainCardCount(), getUpdatedTrainDeckCount());
        CommandFacade.instance().addHistory(getHistory());
        return null;
    }
}
