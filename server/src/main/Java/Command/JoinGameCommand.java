package Command;

import java.util.List;

import Communication.CommandList;
import Communication.Game;
import Communication.Player;
import Communication.ServerFacade;
import Manager.GameCommandManager;

/**
 * Created by autumnchapman on 10/2/17.
 */

public class JoinGameCommand extends JoinGameCommandData implements ICommand{

    /**
     * required public, empty constructor
     */
    public JoinGameCommand(){ super(); }

    /**
     * public constructor for when info is available to fill the super, the command data
     * @param p The joining player
     * @param g id of the game to be joined
     * @param a auth token of the calling client
     */
    public JoinGameCommand(Player p, String g, String a){
        super(p, g, a);
    }

    /**
     * Joins a player to a specified game by calling the server facade and returns a command to update the changed game
     * @return an UpdateGameStateResponseCommand
     */
    @Override
    public List<Command> execute() {
        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String username = SF.getUserName(this.authToken);
        Game game = SF.joinGame(this.getPlayerJoining(), this.getGameToJoinID());
        if(game != null){
            UpdateGameStateResponseCommandData cmmd = new UpdateGameStateResponseCommandData(game.getGameID(), game.getCurrentGameState());
            List<Command> toRet = manager.addJoinGame(cmmd, username, this.getGameToJoinID());
            return toRet;
        }
        else{
            String message = "Game is already full";
            UpdateGameStateResponseCommandData cmmd = new UpdateGameStateResponseCommandData(message);
            List<Command> toRet = manager.Poll(username, null);
            toRet.add(cmmd);
            return toRet;
        }
    }
}