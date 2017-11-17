package Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Communication.DestinationCard;
import Communication.Game;
import Communication.History;
import Communication.ServerFacade;
import Communication.User;
import Manager.GameCommandManager;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class DesintationRemovalRequestCommand extends DestinationRemovalRequestCommandData implements ICommand {

    public DesintationRemovalRequestCommand() {
        super();
    }

    public DesintationRemovalRequestCommand(String authToken, List<DestinationCard> destinationCardToDitch) {
        super(authToken, destinationCardToDitch);

    }

    @Override
    public Object execute() {

        GameCommandManager manager = GameCommandManager.instance();
        ServerFacade SF = new ServerFacade();
        String username = SF.getUserName(this.authToken);
        String gameID = SF.getGameID(username);
        SF.returnDCards(gameID, username, destinationCardToDitch);

        int updateDDeckCount = SF.getDDeckCount(gameID);
        int updatedPlayerDCount = SF.getPlayerDCardCount(gameID, username);
        History history = new History();

        if(destinationCardToDitch!= null){
            history.setHistMessage(username + " returned " + getDestinationCardToDitch().size() + " destination card");      // what if person returns 2?
            history.setColor(SF.getColor(gameID, username));
            history.setUserName(username);
        }

        OtherDestinationRemovalResponseCommandData Ocmmd = new OtherDestinationRemovalResponseCommandData(username, updatedPlayerDCount, updateDDeckCount, history);
        CallerDestinationRemovalResponseCommandData Ccmmd = new CallerDestinationRemovalResponseCommandData(updatedPlayerDCount, updateDDeckCount, history);

        List<Command> list = new ArrayList<>();
        list.add(Ccmmd);
        list.add(Ocmmd);
        List<Command> toRet = manager.addCommands(gameID, username, list);
        return toRet;
    }
}
