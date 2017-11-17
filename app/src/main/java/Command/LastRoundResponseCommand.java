package Command;

import Model.CommandFacade;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class LastRoundResponseCommand extends LastRoundResponseCommandData implements ICommand{

    public LastRoundResponseCommand(){super();}

    public LastRoundResponseCommand(String authToken, String gameID){ super(authToken,gameID);}

    public LastRoundResponseCommand(String message){super(message);}

    @Override
    public Object execute() {
        CommandFacade.instance().startLastRound();
        return null;
    }
}
