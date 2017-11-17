package Command;

import java.util.ArrayList;
import java.util.List;

import Communication.Game;
import Communication.GameState;
import Communication.History;
import Communication.Player;
import Communication.ServerFacade;
import Communication.TrainCard;
import Manager.GameCommandManager;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class DrawFaceDownTCardRequestCommand extends DrawFaceDownTCardRequestCommandData implements ICommand {

    @Override
    public List<Command> execute() {
        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String username = SF.getUserName(getAuthToken());
        TrainCard card = SF.drawDownTCard( getGameID(), username );

        Game game = SF.getGame(getGameID());
        GameState state = game.getCurrentGameState();
        Player player = state.getPlayer(username);
        int playerCardCount = player.getNumTrainCards();
        int updatedDDeckCount = game.getTrainDeckCount();
        String message = username + " drew train cards from the deck";
        History h = new History(message, username, player.getColor());

        CallerDrawFaceDownTResponseCommandData Ccmmd = new CallerDrawFaceDownTResponseCommandData(card, playerCardCount, updatedDDeckCount, h);
        OtherDrawFaceDownTResponseCommandData Ocmmd = new OtherDrawFaceDownTResponseCommandData(playerCardCount, username, updatedDDeckCount, h);

        List<Command> list = new ArrayList<>();
        list.add(Ccmmd);
        list.add(Ocmmd);

        List<Command> toRet = manager.addCommands(getGameID(),username,list );
        return toRet;
    }
}
