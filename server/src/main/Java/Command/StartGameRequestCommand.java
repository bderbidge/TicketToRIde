package Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Communication.CommandList;
import Communication.Game;
import Communication.GameState;
import Communication.History;
import Communication.Player;
import Communication.ServerFacade;
import Command.StartGameRequestCommandData;
import Communication.User;
import Manager.GameCommandManager;

/**
 * Created by autumnchapman on 10/5/17.
 */

public class StartGameRequestCommand extends StartGameRequestCommandData implements ICommand {

    /**
     * required public, empty constructor
     */
    public StartGameRequestCommand(){ super();}

    /**
     * public constructor for when info is available to fill the super, the command data
     * @param id String id of the game to be started
     * @param auth String auth token of the calling client
     */
    public StartGameRequestCommand(String id, String auth){
        super(id, auth);
    }

    /**
     * Starts the specified game by calling startGame in the ServerFacade and returns the updated GameState
     * @return UpdateGameListResponseCommand
     */
    @Override
    public List<Command> execute() {
        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String username = SF.getUserName(this.authToken);
        Game game = SF.startGame(this.getGameID());

        List<History> history = new ArrayList<>();
        Player player = game.getCurrentGameState().getPlayer(username);

        if(game != null){
            String message = username + " started the game";
            History h = new History(message, username, player.getColor());
            history.add(h);

            Set<String> usernames = game.getCurrentGameState().getCurrentPlayers().keySet();
            List<StartGameResponseCommandData> list = new ArrayList<>();

            for (String u: usernames) {
                User use = SF.getUser(u);
                StartGameResponseCommandData cmmd = new StartGameResponseCommandData(use, game, history);
                list.add(cmmd);
            }


            List<Command> toRet = manager.addStartGame(list, username, gameID);
            return toRet;
        }
        else{
            String message = "incorrect amount of players to start game";
            StartGameResponseCommandData cmmd = new StartGameResponseCommandData(message);
            List<Command> toRet = manager.getCommands(gameID, username);
            toRet.add(cmmd);
            return toRet;
        }
    }
}
