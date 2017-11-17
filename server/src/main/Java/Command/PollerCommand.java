package Command;

import java.util.ArrayList;
import java.util.List;

import Communication.CommandList;
import Communication.EncoderDecoder;
import Communication.Game;
import Communication.GamesList;
import Communication.ServerFacade;
import Manager.GameCommandManager;

/**
 * Created by brandonderbidge on 10/6/17.
 */

public class PollerCommand extends PollerCommandData implements ICommand {

    /**
     * required public, empty constructor
     */
    public PollerCommand(){ super(); }

    /**
     * public constructor for when info is available to fill the super, the command data
     * @param authToken auth token of the calling client
     * @param version
     */
    public PollerCommand(String authToken, int version){
        super(authToken, version);
    }

    /**
     * based on whether the client's game exists or is up to date this commands gets up to date data
     * and returns it to the calling client in the form of a command
     * @return UpdateGameListResponseCommand or UpdateGameStateResponseCommandData
     */
    @Override
    public List<Command> execute() {

        EncoderDecoder ED = new EncoderDecoder();
        ServerFacade SF = new ServerFacade();
        GameCommandManager manager = GameCommandManager.instance();
        String username = SF.getUserName(this.getAuthtoken());
        String gameID = SF.getGameID(username);
        List<Command> toRet = manager.Poll(username, gameID);

        return toRet;

    }
}




