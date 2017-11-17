package Command;

import java.util.ArrayList;
import java.util.List;

import Communication.Game;
import Communication.History;
import Communication.User;

/**
 * Created by brandonderbidge on 10/18/17.
 */

public class StartGameResponseCommandData extends Command {

    User user;
    Game game;
    List<History> mHistorys;

    /**
     * required public constructor, sets Command type
     */
    public StartGameResponseCommandData(){ this.setType("StartGameResponseCommand");}

    /**
     * public constructor
     * @param user User
     * @param game Game of game begin started
     */
    public StartGameResponseCommandData(User user, Game game, List<History> histories) {
        this.setType("StartGameResponseCommand");
        this.user = user;
        this.game = game;
        mHistorys = histories;
        setSuccess(true);
    }

    public StartGameResponseCommandData(String message) {
        setSuccess(false);
        setMessage(message);
        this.setType("StartGameResponseCommand");
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void addHistory(History h) {
        mHistorys.add(h);
    }

}
