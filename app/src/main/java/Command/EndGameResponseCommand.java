package Command;

import Model.CommandFacade;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class EndGameResponseCommand extends EndGameResponseCommandData implements ICommand {

    public Object execute() {
        CommandFacade.instance().endGame(getPlayerList());
        return null;
    }
}
