package Command;

import Communication.GameState;
import Model.CommandFacade;

/**
 * Created by autumnchapman on 10/4/17.
 */

public class UpdateGameStateResponseCommand extends UpdateGameStateResponseCommandData implements ICommand{

    /**
     * required public, empty constructor
     */
    public UpdateGameStateResponseCommand(){ super(); }

    /**
     * public constructor for when info is available to fill the super, the command data
     * @param s id of the game the gameState belongs to
     * @param gs current game state of the specified game
     */
    public UpdateGameStateResponseCommand(String s, GameState gs){
        super(s, gs);
    }

    /**
     * When successful it updates the client's GameState, when it isn't it notifies the caller of the error and gives the
     * associated message
     * @return nothing
     */
    @Override
    public Object execute() {

        if(isSuccess()) {
            CommandFacade.instance().updateGameState(getGameID(), getGameState());
            return null;
        }
        else {
            CommandFacade.instance().notifyOfCommandError(getMessage());
            return null;
        }
    }
}
