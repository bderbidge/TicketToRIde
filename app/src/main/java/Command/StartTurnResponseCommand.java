package Command;

import Communication.History;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class StartTurnResponseCommand extends StartTurnResponseCommandData implements ICommand{

    public StartTurnResponseCommand() {
        super();
    }

    public StartTurnResponseCommand(String username, History hist) {
        super(username, hist);
    }

    public StartTurnResponseCommand(String message) {
        super(message);
    }

    @Override
    public Object execute() {
        CommandFacade.instance().startNextPlayersTurn(getUsername());
        return null;
    }
}
