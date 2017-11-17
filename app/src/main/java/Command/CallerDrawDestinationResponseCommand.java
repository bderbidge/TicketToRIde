package Command;

import Model.CommandFacade;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class CallerDrawDestinationResponseCommand extends CallerDrawDestinationResponseCommandData implements ICommand {

    public Object execute() {
        CommandFacade.instance().setDestinationOptions(getCardsToDraw(), getUpdateDestinationDeckCount());
        CommandFacade.instance().addHistory(getHistory());
        return null;
    }
}
