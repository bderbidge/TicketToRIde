package DataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Communication.DestinationCard;
import Communication.Route;

/**
 * Created by autumnchapman on 10/23/17.
 */

public class DCardDAO {

    private ServerModelRoot db;


    public DCardDAO(ServerModelRoot d){
        this.db = d;
    }

    public void initializeDeck(String gameID){
        Map<String, List<DestinationCard>> GamesToDDecks = db.getGamesToDDecks();
        List<DestinationCard> newDeck = new ArrayList<>();

        String city = "Helena";
        String secondCity = "Los Angeles";
        DestinationCard dc = new DestinationCard(city, secondCity, 8);
        newDeck.add(dc);
        city = "Denver";
        secondCity = "El Paso";
        dc = new DestinationCard(city, secondCity, 4);
        newDeck.add(dc);
        city = "Chicago";
        secondCity = "Santa Fe";
        dc = new DestinationCard(city, secondCity, 9);
        newDeck.add(dc);
        city = "Denver";
        secondCity = "Pittsburgh";
        dc = new DestinationCard(city, secondCity, 11);
        newDeck.add(dc);
        city = "San Francisco";
        secondCity = "Atlanta";
        dc = new DestinationCard(city, secondCity, 17);
        newDeck.add(dc);
        city = "Dallas";
        secondCity = "New York";
        dc = new DestinationCard(city, secondCity, 11);
        newDeck.add(dc);
        city = "Seattle";
        secondCity = "New York";
        dc = new DestinationCard(city, secondCity, 22);
        newDeck.add(dc);
        city = "Boston";
        secondCity = "Miami";
        dc = new DestinationCard(city, secondCity, 12);
        newDeck.add(dc);
        city = "Portland";
        secondCity = "Phoenix";
        dc = new DestinationCard(city, secondCity, 11);
        newDeck.add(dc);
        city = "Los Angeles";
        secondCity = "Miami";
        dc = new DestinationCard(city, secondCity, 20);
        newDeck.add(dc);
        city = "Toronto";
        secondCity = "Miami";
        dc = new DestinationCard(city, secondCity, 10);
        newDeck.add(dc);
        city = "Calgary";
        secondCity = "Salt Lake City";
        dc = new DestinationCard(city, secondCity, 7);
        newDeck.add(dc);
        city = "Montreal";
        secondCity = "New Orleans";
        dc = new DestinationCard(city, secondCity, 13);
        newDeck.add(dc);
        city = "Chicago";
        secondCity = "New Orleans";
        dc = new DestinationCard(city, secondCity, 7);
        newDeck.add(dc);
        city = "New York";
        secondCity = "Atlanta";
        dc = new DestinationCard(city, secondCity, 6);
        newDeck.add(dc);
        city = "Montreal";
        secondCity = "Atlanta";
        dc = new DestinationCard(city, secondCity, 9);
        newDeck.add(dc);
        city = "Los Angeles";
        secondCity = "Chicago";
        dc = new DestinationCard(city, secondCity, 16);
        newDeck.add(dc);
        city = "Los Angeles";
        secondCity = "New York";
        dc = new DestinationCard(city, secondCity, 21);
        newDeck.add(dc);
        city = "Winnipeg";
        secondCity = "Houston";
        dc = new DestinationCard(city, secondCity, 12);
        newDeck.add(dc);
        city = "Sault Ste. Marie";
        secondCity = "Nashville";
        dc = new DestinationCard(city, secondCity, 8);
        newDeck.add(dc);
        city = "Calgary";
        secondCity = "Phoenix";
        dc = new DestinationCard(city, secondCity, 13);
        newDeck.add(dc);
        city = "Duluth";
        secondCity = "El Paso";
        dc = new DestinationCard(city, secondCity, 10);
        newDeck.add(dc);
        city = "Sault Ste. Marie";
        secondCity = "Oklahoma City";
        dc = new DestinationCard(city, secondCity, 9);
        newDeck.add(dc);
        city = "Vancouver";
        secondCity = "Santa Fe";
        dc = new DestinationCard(city, secondCity, 13);
        newDeck.add(dc);
        city = "Kansas City";
        secondCity = "Houston";
        dc = new DestinationCard(city, secondCity, 5);
        newDeck.add(dc);
        city = "Winnipeg";
        secondCity = "Little Rock";
        dc = new DestinationCard(city, secondCity, 11);
        newDeck.add(dc);
        city = "Seattle";
        secondCity = "Los Angeles";
        dc = new DestinationCard(city, secondCity, 9);
        newDeck.add(dc);
        city = "Vancouver";
        secondCity = "Montreal";
        dc = new DestinationCard(city, secondCity, 20);
        newDeck.add(dc);
        city = "Portland";
        secondCity = "Nashville";
        dc = new DestinationCard(city, secondCity, 17);
        newDeck.add(dc);
        city = "Duluth";
        secondCity = "Houston";
        dc = new DestinationCard(city, secondCity, 8);
        newDeck.add(dc);

        GamesToDDecks.put(gameID, newDeck);
        shuffle(gameID);
        //db.setGamesToDDecks(GamesToDDecks);
    }

    public void returnCards(String gameID, List<DestinationCard> dc){
        Map<String, List<DestinationCard>> gamesToDDecks = db.getGamesToDDecks();
        List<DestinationCard> deck = gamesToDDecks.get(gameID);
        if(dc != null){
            for (DestinationCard card: dc) {
                deck.add(0, card);
            }
        }
        gamesToDDecks.put(gameID, deck);
        //db.setGamesToDDecks(gamesToDDecks);
    }

    public List<DestinationCard> draw3Cards(String gameID){

        Map<String, List<DestinationCard>> gamesToDDecks = db.getGamesToDDecks();
        List<DestinationCard> deck = gamesToDDecks.get(gameID);
        List<DestinationCard> toRet = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            if(deck.size() > 0){
                DestinationCard card = deck.get(deck.size()-1);
                deck.remove(card);
                toRet.add(card);
            }
        }

        gamesToDDecks.put(gameID, deck);
        //db.setGamesToDDecks(gamesToDDecks);
        return toRet;
    }

    public void shuffle(String gameID){
        Map<String, List<DestinationCard>> gamesToDDecks = db.getGamesToDDecks();
        List<DestinationCard> deck = gamesToDDecks.get(gameID);
        List<DestinationCard> toRet = ServerUtil.shuffle(deck);
        gamesToDDecks.put(gameID, toRet);
        //db.setGamesToDDecks(gamesToDDecks);
    }
}
