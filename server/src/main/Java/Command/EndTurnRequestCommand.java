package Command;

import java.util.ArrayList;
import java.util.List;

import Communication.DestinationCard;
import Communication.Game;
import Communication.GameState;
import Communication.History;
import Communication.Player;
import Communication.ServerFacade;
import Manager.GameCommandManager;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class EndTurnRequestCommand extends EndTurnRequestCommandData implements ICommand{

    public EndTurnRequestCommand(){
            super();
    }

    public EndTurnRequestCommand(String auth, String id){
        super(auth, id);
    }

    @Override
    public List<Command> execute() {


        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String username = SF.getUserName(getAuthToken());
        String usernameOfPlayerWhoseTurnItIs = SF.endTurn(getGameID(), username);

        List<Command> list = new ArrayList<>();

        if(usernameOfPlayerWhoseTurnItIs == null){
            //it's time to end the game
            List<Player> endGameData = SF.getEndGameData(gameID);
            EndGameResponseCommandData cmmd = new EndGameResponseCommandData(gameID, endGameData);
            list.add(cmmd);
            list.add(cmmd);

            List<Command> toRet = manager.addCommands(gameID, username, list);
            return toRet;
        }
        if(SF.isLastRound(gameID)){
            LastRoundResponseCommandData cmmd = new LastRoundResponseCommandData(authToken, gameID);
            manager.addLastRoundCommands(gameID, cmmd);

        }

        Game game = SF.getGame(getGameID());
        GameState state = game.getCurrentGameState();
        Player player = state.getPlayer(username);
        String message = username + " just finished  their turn";
        History h = new History(message, username, player.getColor());

        StartTurnResponseCommandData cmmd = new StartTurnResponseCommandData(usernameOfPlayerWhoseTurnItIs, h);

        list.add(cmmd);
        list.add(cmmd);

        List<Command> toRet = manager.addCommands(getGameID(), username,list);
        return toRet;
    }

}
