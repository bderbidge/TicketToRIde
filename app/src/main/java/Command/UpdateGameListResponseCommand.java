package Command;

import Communication.GamesList;
import Model.CommandFacade;

/**
 * Created by autumnchapman on 10/4/17.
 */

public class UpdateGameListResponseCommand extends UpdateGameListResponseCommandData implements ICommand {
    /**
     * required public empty constructor
     */
    public UpdateGameListResponseCommand(){ super(); }

    /**
     * public constructor to be used when there is information for the command data
     * @param gl the current version of the GamesList object contained in the ServerModelRoot
     */
    public UpdateGameListResponseCommand(GamesList gl){
        super(gl);
    }

    /**
     * updates the client's gameList when successful and notifies the caller of an error when not
     * @return
     */
    @Override
    public Object execute() {
        if(isSuccess()) {
            CommandFacade.instance().updateGamesList(getGameList());
            return null;
        }
        else {
            CommandFacade.instance().notifyOfCommandError(getMessage());
            return null;
        }
    }
}
