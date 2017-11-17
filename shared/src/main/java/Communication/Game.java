package Communication;

import java.util.List;
import java.util.Vector;


/**
 * Created by emmag on 9/30/2017.
 */

public class Game {
    private String mGameID;
    private String mGameName;
    private int mMaxNumberOfPlayers;
    private GameState mCurrentGameState;

    private TrainCard[] mTUpDeck;
    private int mTDownDeckCount;
    private int mDDownDeckCount;

    public Game() {
    }

    public Game(String gameID, String gameName, int maxNumberOfPlayers, GameState currentGameState) {
        mGameID = gameID;
        mGameName = gameName;
        mMaxNumberOfPlayers = maxNumberOfPlayers;
        mCurrentGameState = currentGameState;
        mTUpDeck = new TrainCard[5];
    }

    public String getGameID() {
        return mGameID;
    }

    public String getGameName() {
        return mGameName;
    }

    public int getMaxNumberOfPlayers() {
        return mMaxNumberOfPlayers;
    }

    public GameState getCurrentGameState() {
        return mCurrentGameState;
    }

    public void setCurrentGameState(GameState currentGameState) {
        mCurrentGameState = currentGameState;
    }

    public int getGameVersion() {
        return mCurrentGameState.getGameStateVersion();
    }

    public int getTrainDeckCount() {
        return mTDownDeckCount;
    }

    public void setTrainDeckCount(int c) { mTDownDeckCount = c; }

    public int getDestinationDeckCount() {
        return mDDownDeckCount;
    }

    public void setDestinationDeckCount(int c) { mDDownDeckCount = c; }

    public TrainCard[] getFaceUpTrainDeck() {
        return mTUpDeck;
    }

    public void setUpDeck(TrainCard[] trains){
        mTUpDeck = trains;
    }

    public int getTUpDeckSize(){
        int toRet = 0;
        for(int i = 0; i < 5; i++){
            if(mTUpDeck[i]!=null){
                toRet = toRet+1;
            }
        }
        return toRet;
    }

    public void setUpDeck(List<TrainCard> trains) {
        int i = 0;
        for(int j = 0; j < 5; j++){
            mTUpDeck[i] = null;
        }
        for (TrainCard card: trains) {
            mTUpDeck[i] = card;
            i++;
        }
    }

    public void startGame(int dDeckSize, int tDeckSize, List<TrainCard> upTDeck) throws Exception{
        //set up decks DONE in server Model Root
        //pass out card to users DONE in gameDAO
        //set up player cards and numbers DONE in gameState startGame
        mTDownDeckCount = tDeckSize;
        mDDownDeckCount = dDeckSize;
        int i = 0;
        for(TrainCard tc: upTDeck){
            mTUpDeck[i] = tc;
            i++;
        }

        mCurrentGameState.startGame();
    }

    public void startLastRound(String userName){
        mCurrentGameState.startLastRound(userName);
    }


}
