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

public class DrawFaceUpTCardRequestCommand extends DrawFaceUpTCardRequestCommandData implements ICommand {

    @Override
    public List<Command> execute() {
        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String username = SF.getUserName(getAuthToken());
        boolean shuffled = SF.drawUpTCard(getDrawFaceUp(), getGameID(), username);

        Game game = SF.getGame(getGameID());
        GameState state = game.getCurrentGameState();
        Player player = state.getPlayer(username);
        int playerCardCount = player.getNumTrainCards();
        int updatedTDeckCount = game.getTrainDeckCount();
        TrainCard[] updateFaceUpDeck = game.getFaceUpTrainDeck();
        String message = username + " drew train cards from the train yard";
        History h = new History(message, username, player.getColor());
        List<History> histories = new ArrayList<>();
        histories.add(h);
        if(shuffled){
            History h2 = new History("the train deck was shuffled", username, player.getColor());
            histories.add(h2);
        }

        CallerDrawFaceUpTResponseCommandData Ccmmd = new CallerDrawFaceUpTResponseCommandData(getDrawFaceUp(), playerCardCount, updatedTDeckCount, updateFaceUpDeck, histories);
        OtherDrawFaceUpTResponseCommandData Ocmmd = new OtherDrawFaceUpTResponseCommandData(playerCardCount, username, updatedTDeckCount, updateFaceUpDeck, histories);

        List<Command> list = new ArrayList<>();
        list.add(Ccmmd);
        list.add(Ocmmd);

        List<Command> toRet = manager.addCommands(getGameID(),username,list );
        return toRet;
    }
}
