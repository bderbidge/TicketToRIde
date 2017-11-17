package Communication;

import java.util.Map;

/**
 * Created by emmag on 9/30/2017.
 */

public class GamesList {
    private Map<String, Game> mOpenGames;

    public GamesList(){}

    public GamesList(Map<String, Game> openGames) {
        mOpenGames = openGames;
    }

    public Map<String, Game> getOpenGames() {
        return mOpenGames;
    }

    public Game queryOpenGames(String gameID) {
        if(mOpenGames.containsKey(gameID)){
            return mOpenGames.get(gameID);
        }
        else {
            return null;
        }
    }

    public boolean updateSpecifiedGameState(String gameID, GameState gameState){
        Game gameToUpdate = queryOpenGames(gameID);
        if(gameToUpdate != null) {
            gameToUpdate.setCurrentGameState(gameState);
            return true;
        }
        else {
            return false;
        }
    }

    public void removeGame(String gameID) {
        if(mOpenGames.containsKey(gameID)){
            mOpenGames.remove(gameID);
        }
    }
}
