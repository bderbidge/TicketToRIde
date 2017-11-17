package Command;

import Communication.DestinationCard;
import Communication.History;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class CallerDestinationRemovalResponseCommandData extends Command{

    int updatedPlayerDestinationCount;
    int updatedDestinationDeckCount;
    History history;

    public CallerDestinationRemovalResponseCommandData(){
        this.setType("CallerDestinationRemovalResponseCommand");
    }

    public CallerDestinationRemovalResponseCommandData(int updatedPlayerDestinationCount, int updatedDestinationDeckCount, History history) {
        this.setType("CallerDestinationRemovalResponseCommand");
        this.updatedPlayerDestinationCount = updatedPlayerDestinationCount;
        this.updatedDestinationDeckCount = updatedDestinationDeckCount;
        this.history = history;
        setSuccess(true);
    }

    public CallerDestinationRemovalResponseCommandData(String message) {
        setType("DrawDestinationRequestCommand");
        setMessage(message);
        setSuccess(false);
    }


    public int getUpdatedPlayerDestinationCount() {
        return updatedPlayerDestinationCount;
    }

    public int getUpdatedDestinationDeckCount() {
        return updatedDestinationDeckCount;
    }

    public History getHistory() {
        return history;
    }
}
