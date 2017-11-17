package Command;

import java.util.List;

import Communication.DestinationCard;
import Communication.History;

/**
 * Created by emilyprigmore on 11/6/17.
 */

public class CallerDrawDestinationResponseCommandData extends Command {
    private History mHistory;
    private List<DestinationCard> mCardsToDraw;
    private int updateDestinationDeckCount;

    public CallerDrawDestinationResponseCommandData(History mHistory, List<DestinationCard> cardsToDraw, int updateDestinationDeckCount) {

        setType("CallerDrawDestinationResponseCommand");
        this.mHistory = mHistory;
        this.mCardsToDraw = cardsToDraw;
        this.updateDestinationDeckCount = updateDestinationDeckCount;
        setSuccess(true);
    }

    public CallerDrawDestinationResponseCommandData() {

        setType("CallerDrawDestinationResponseCommand");
    }

    public CallerDrawDestinationResponseCommandData(String message) {
        setType("CallerDrawDestinationResponseCommand");
        this.setMessage(message);
        setSuccess(false);
    }



    public History getHistory() {
        return mHistory;
    }

    public void setHistory(History history) {
        this.mHistory = history;
    }

    public List<DestinationCard> getCardsToDraw() {
        return mCardsToDraw;
    }

    public void setCardsToDraw(List<DestinationCard> cardsToDraw) {
        this.mCardsToDraw = cardsToDraw;
    }

    public int getUpdateDestinationDeckCount() {
        return updateDestinationDeckCount;
    }

    public void setUpdateDestinationDeckCount(int updateDestinationDeckCount) {
        this.updateDestinationDeckCount = updateDestinationDeckCount;
    }
}
