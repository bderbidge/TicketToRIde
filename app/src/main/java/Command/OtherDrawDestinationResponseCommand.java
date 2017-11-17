package Command;

import Communication.History;
import Model.CommandFacade;

/**
 * Created by emmag on 11/8/2017.
 */

public class OtherDrawDestinationResponseCommand extends OtherDrawDestinationResponseCommandData implements ICommand {
    public OtherDrawDestinationResponseCommand() {
    }

    public OtherDrawDestinationResponseCommand(String username, int playerDestinationCardCount, int updatedDestinationDeckCount, History history) {
        super(username, playerDestinationCardCount, updatedDestinationDeckCount, history);
    }

    @Override
    public Object execute() {
        CommandFacade.instance().updatePlayersDestinationCardNumber(getUsername(), getPlayerDestinationCardCount(), getUpdatedDestinationDeckCount());
        CommandFacade.instance().addHistory(getHistory());
        return null;
    }
}
