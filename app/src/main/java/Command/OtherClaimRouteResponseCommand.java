package Command;

import java.util.List;

import Communication.History;
import Communication.Route;
import Command.OtherClaimRouteResponseCommandData;
import Model.CommandFacade;


/**
 * Created by brandonderbidge on 11/6/17.
 */

public class OtherClaimRouteResponseCommand extends OtherClaimRouteResponseCommandData implements ICommand{

    public OtherClaimRouteResponseCommand(){
        super();
    }

    public OtherClaimRouteResponseCommand(String id, Route r, int deckCount, List<String> longestUser, History h){
        super(id, r, deckCount, longestUser, h);
    }

    @Override
    public List<Command> execute(){
        CommandFacade.instance().addPlayerClaimedRoute(getUsername(), getRoute(), getDeckCardCount(), getLongestRoutePlayer());
        CommandFacade.instance().addHistory(getHistory());
        return null;
    }
}
