package Command;

import java.util.Map;

import Communication.History;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class OtherDestinationRemovalResponseCommand extends OtherDestinationRemovalResponseCommandData implements ICommand {

    public OtherDestinationRemovalResponseCommand() {
    }

    public OtherDestinationRemovalResponseCommand(String username, int playerDestinationCardCount, int updatedDestinationDeckCount, History history) {
        super(username, playerDestinationCardCount, updatedDestinationDeckCount, history);
    }

    @Override
    public Object execute() {
        CommandFacade.instance().updatePlayersDestinationCardNumber(getUsername(), getPlayerDestinationCardCount(), getUpdatedDestinationDeckCount());
        CommandFacade.instance().addHistory(getHistory());
        return null;
    }
}
