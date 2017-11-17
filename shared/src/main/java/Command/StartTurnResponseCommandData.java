package Command;

import Communication.History;

/**
 * Created by brandonderbidge on 11/6/17.
 */

public class StartTurnResponseCommandData extends Command {

    private History history;

    private String username;

    public StartTurnResponseCommandData() {
        this.setType("StartTurnResponseCommand");
    }

    public StartTurnResponseCommandData(String username, History hist) {
        this.setType("StartTurnResponseCommand");
        this.username = username;
        this.history = hist;
        setSuccess(true);
    }

    public StartTurnResponseCommandData(String message) {
        setSuccess(false);
        setMessage(message);
        this.setType("StartTurnResponseCommand");
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
