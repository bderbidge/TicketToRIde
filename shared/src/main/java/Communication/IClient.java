package Communication;

/**
 * Created by emmag on 9/30/2017.
 */

public interface IClient {

    //updates model GameList to reflect passed in GamesList
    public void updateGamesList(GamesList gamesList);

    //updates the GameState on the Game with the given gameID to reflected the passed in gameState
    public void updateGameState(String gameID, GameState gameState);

}
