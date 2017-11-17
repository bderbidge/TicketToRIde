package Command;

import java.util.List;

import Communication.CommandList;
import Communication.GamesList;
import Communication.ServerFacade;
import Manager.GameCommandManager;


/**
 * Created by autumnchapman on 9/30/17.
 */

public class CreateGameCommand extends CreateGameCommandData implements ICommand{

    /**
     * required public, empty constructor
     */
    public CreateGameCommand(){ super(); }

    /**
     * public constructor for when info is available to fill the super, the command data
     * @param name the name of the game
     * @param num the number of players that can be in the game, 1 < num < 6
     * @param auth the auth token of the calling user
     */
    public CreateGameCommand(String name, int num, String auth){
        super(name, num, auth);
    }

    /**
     * Creates a new game through the ServerFacade and gets back the newest GamesList object to send to the Client
     * @return an UpdateGameListResponseCommand
     */
    @Override
    public List<Command> execute() {
        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String gameID = SF.createGame(this.getGameName(), this.getNumberOfPLayers());
        String username = SF.getUserName(this.authToken);
        GamesList toPass = SF.getGameList();
        UpdateGameListResponseCommandData cmd = new UpdateGameListResponseCommandData(toPass);
        List<Command> toRet = manager.addGameCreated(cmd, username, gameID);
        return toRet;
    }
}
