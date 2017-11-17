package DataAccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Communication.TrainCard;

/**
 * Created by autumnchapman on 10/23/17.
 */

public class TCardDAO {

    private ServerModelRoot db;



    public TCardDAO(ServerModelRoot d){
        this.db = d;
    }

    public void initializeDecks(String gameID) {
        Map<String, List<TrainCard>> downDeck = db.getDownDeck();
        Map<String, List<TrainCard>> upDeck = db.getUpDeck();
        List<TrainCard> downCards = new ArrayList<>();
        List<TrainCard> upCards = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("blue");
        colors.add("green");
        colors.add("purple");
        colors.add("yellow");
        colors.add("white");
        colors.add("black");
        colors.add("orange");
        colors.add("wild");

        //initializes 12 of each "color" of card to the down deck
        for(int j = 0; j < 9; j++) {
            for (int i = 0; i < 12; i++) {
                TrainCard toAdd = new TrainCard(colors.get(j));
                downCards.add(toAdd);
            }
        }
        //adds the two extra wild cards
        TrainCard first = new TrainCard("wild");
        TrainCard second = new TrainCard("wild");
        downCards.add(first);
        downCards.add(second);

        downCards = shuffleDeck(downCards);
        downCards = shuffleDeck(downCards);
        downCards = shuffleDeck(downCards);

        for(int i = 0; i < 5; i++){
            TrainCard tc = downCards.get(0);
            upCards.add(tc);
            downCards.remove(tc);
        }
        downDeck.put(gameID, downCards);
        upDeck.put(gameID, upCards);
        //db.setDownDeck(downDeck);
        //db.setUpDeck(upDeck);
    }

    public void shuffle(String gameID){
        Map<String, List<TrainCard>> downDeck = db.getDownDeck();
        List<TrainCard> deck = downDeck.get(gameID);
        List<TrainCard> toRet = ServerUtil.shuffle(deck);
        downDeck.put(gameID, toRet);
    }

    private List<TrainCard> shuffleDeck(List<TrainCard> deck){
        List<TrainCard> toRet = ServerUtil.shuffle(deck);
        return toRet;
    }

    public boolean drawUpCard(String gameID, TrainCard card){
        boolean shuffled = false;
        Map<String, List<TrainCard>> upDeck = db.getUpDeck();
        List<TrainCard> udeck = upDeck.get(gameID);
        Map<String, List<TrainCard>> downDeck = db.getDownDeck();
        List<TrainCard> ddeck = downDeck.get(gameID);
        String toRemCol = card.getColor();
        for(int i = 0; i< udeck.size(); i++){
            if(udeck.get(i).getColor().equals(toRemCol)){
                udeck.remove(i);
                break;
            }
        }
        if(ddeck.size() > 0){
            udeck.add(ddeck.get(ddeck.size() - 1));
            ddeck.remove(ddeck.size() - 1);
        }

        upDeck.put(gameID, udeck);
        downDeck.put(gameID, ddeck);

        if(needsShuffled(udeck)){
            shuffled = true;
            ddeck = reShuffle(gameID);
            for(int i = 0; i < 5; i++){
                TrainCard tc = ddeck.get(0);
                udeck.add(tc);
                ddeck.remove(tc);
            }
        }

        upDeck.put(gameID, udeck);
        downDeck.put(gameID, ddeck);
        return shuffled;
    }

    public TrainCard drawDownTCard(String gameID){
        Map<String, List<TrainCard>> downDeck = db.getDownDeck();
        List<TrainCard> deck = downDeck.get(gameID);
        TrainCard card = deck.get(deck.size()-1);
        deck.remove(deck.size()-1);
        downDeck.put(gameID, deck);
        //db.setDownDeck(downDeck);
        return card;
    }

    private boolean needsShuffled(List<TrainCard> deck){
        int count = 0;
        for (TrainCard card:deck) {
            if(card.getColor().equals("wild")){
                count++;
            }
        }
        if(count > 2){
            return true;
        }
        return false;
    }

    private List<TrainCard> reShuffle(String gameID){
        Map<String, List<TrainCard>> upDeck = db.getUpDeck();
        List<TrainCard> udeck = upDeck.get(gameID);
        Map<String, List<TrainCard>> downDeck = db.getDownDeck();
        List<TrainCard> ddeck = downDeck.get(gameID);
        int size = udeck.size();
        for(int i = 0; i < size; i++){
            TrainCard tc = udeck.get(0);
            ddeck.add(tc);
            udeck.remove(tc);
        }
        return shuffleDeck(ddeck);
    }

    public void returnCards(String gameID, List<TrainCard> cards){
        Map<String, List<TrainCard>> downDeck = db.getDownDeck();
        List<TrainCard> deck = downDeck.get(gameID);
        for (TrainCard card: cards) {
            deck.add(0, card);
        }
        downDeck.put(gameID, deck);
    }
}
