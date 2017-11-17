package Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Communication.DestinationCard;
import Communication.Game;
import Communication.GameState;
import Communication.History;
import Communication.Player;
import Communication.ServerFacade;
import Communication.User;
import Manager.GameCommandManager;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class DrawDestinationRequestCommand extends DrawDestinationRequestCommandData implements ICommand {

    public List<Command> execute() {
        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String username = SF.getUserName(getAuthToken());
        List<DestinationCard> cards = SF.drawDCard( getGameID(), username );

        Game game = SF.getGame(getGameID());
        GameState state = game.getCurrentGameState();
        Player player = state.getPlayer(username);
        int playerCardcount = player.getNumDestCards();
        int updatedDDeckCount = game.getDestinationDeckCount();
        String message = username + " drew destination cards";
        History h = new History(message, username, player.getColor());

        CallerDrawDestinationResponseCommandData Ccmmd = new CallerDrawDestinationResponseCommandData(h,cards, updatedDDeckCount);
        OtherDrawDestinationResponseCommandData Ocmmd = new OtherDrawDestinationResponseCommandData(username, playerCardcount, updatedDDeckCount, h);

        List<Command> list = new ArrayList<>();
        list.add(Ccmmd);
        list.add(Ocmmd);

        List<Command> toRet = manager.addCommands(getGameID(),username,list );
        return toRet;
    }
}
