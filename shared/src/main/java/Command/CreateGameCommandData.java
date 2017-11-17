package Command;

/**
 * Created by autumnchapman on 9/30/17.
 */

public class CreateGameCommandData extends Command {

    String gameName;
    int numberOfPLayers;
    String authToken;

    /**
     * required public constructor, sets Command type
     */
    public CreateGameCommandData(){
        this.setType("CreateGameCommand");
    }

    /**
     * Constructor
     * @param name String name of game
     * @param num int number of players
     * @param auth String auth token of calling client
     */
    public CreateGameCommandData(String name, int num, String auth){
        super();
        this.setType("CreateGameCommand");
        this.gameName = name;
        this.numberOfPLayers = num;
        this.authToken = auth;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getNumberOfPLayers() {
        return numberOfPLayers;
    }

    public void setNumberOfPLayers(int numberOfPLayers) {
        this.numberOfPLayers = numberOfPLayers;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
