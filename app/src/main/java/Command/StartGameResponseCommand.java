package Command;

import java.util.List;

import Communication.Game;
import Communication.GameState;
import Communication.History;
import Communication.User;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class StartGameResponseCommand extends StartGameResponseCommandData implements ICommand {

    /**
     * required public, empty constructor
     */
    public StartGameResponseCommand(){ super();}

    /**
     * public constructor for when info is available to fill the super, the command data
     * @param user User with updated information
     * @param game Game that has been started
     */
    public StartGameResponseCommand(User user, Game game, List<History> histories){
        super(user, game, histories);
    }

    /**
     * Starts the specified game by calling startGame in the ServerFacade and returns the updated GameState
     * @return UpdateGameListResponseCommand
     */
    @Override
    public Object execute() {
        CommandFacade.instance().startGame(user, game);
        if(mHistorys != null) {
            for (History h : mHistorys) {
                CommandFacade.instance().addHistory(h);
            }
        }
        return null;
    }
}
