package Command;

import Communication.DestinationCard;
import Communication.History;
import Model.CommandFacade;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class CallerDestinationRemovalResponseCommand extends CallerDestinationRemovalResponseCommandData implements ICommand {

    public CallerDestinationRemovalResponseCommand() {super();}

    public CallerDestinationRemovalResponseCommand(int updatedPlayerDestinationCount, int updatedDestinationDeckCount, History history) {
        super(updatedPlayerDestinationCount, updatedDestinationDeckCount, history);
    }

    @Override
    public Object execute() {
        CommandFacade.instance().finishRemovingDestinations(updatedPlayerDestinationCount, updatedDestinationDeckCount);
        CommandFacade.instance().addHistory(history);
        return null;
    }
}
