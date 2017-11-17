package Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Communication.DestinationCard;
import Communication.History;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class DestinationRemovalRequestCommandData extends Command {

    String authToken;
    List<DestinationCard> destinationCardToDitch;

    public DestinationRemovalRequestCommandData() {this.setType("DestinationRemovalRequestCommand");}

    public DestinationRemovalRequestCommandData(String authToken, List<DestinationCard> destinationCardToDitch){
        super();
        this.setType("DestinationRemovalRequestCommand");
        this.authToken = authToken;
        this.destinationCardToDitch = destinationCardToDitch;
    }

    public String getAuthToken() {
        return authToken;
    }

    public List<DestinationCard> getDestinationCardToDitch() {
        return destinationCardToDitch;
    }
}
