package Command;

import java.util.List;

import Communication.History;
import Communication.Route;
import Communication.TrainCard;
import Model.CommandFacade;

/**
 * Created by emmag on 11/13/2017.
 */

public class CallerClaimRouteResponseCommand extends CallerClaimRouteResponseCommandData implements ICommand {

    public CallerClaimRouteResponseCommand(Route r, List<TrainCard> trainCards, int deckCardCount, List<String> longUser, History h){
        super(r, trainCards, deckCardCount, longUser, h);
    }

    public CallerClaimRouteResponseCommand() {
        super();
    }

    @Override
    public Object execute() {
        CommandFacade.instance().addMyRoute(getRoute(), getTrainHand(), getDeckCardCount(), getLongestRoutePlayer());
        CommandFacade.instance().addHistory(getHistory());
        return null;
    }
}
