package Communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emmag on 9/27/2017.
 */

public class User {
    private String mUsername;
    private String mPassword;
    private List<DestinationCard> destCards;
    private List<TrainCard> trainCards;
    //talking to emma

    public User(){};
    public User(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public static boolean checkPasswordsMatch(String passwordA, String passwordB) {
        if(passwordA.equals(passwordB)){
            return true;
        }
        return false;
    }

    public List<DestinationCard> getDestCards() {
        return destCards;
    }

    public void setDestinationCards(List<DestinationCard> destCards) {
        this.destCards = destCards;
    }

    public List<TrainCard> getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(List<TrainCard> trainCards) {
        this.trainCards = trainCards;
    }

    public void addTrainCard(TrainCard trainCard) {
        trainCards.add(trainCard);
    }

    public boolean removeTrainCard(String color) {
        for (int i = 0; i < trainCards.size(); i++) {
            if(trainCards.get(i).getColor().equals(color)) {
                trainCards.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getBlueTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("blue")){
                toRet++;
            }
        }
        return toRet;
    }

    public int getGreenTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("green")){
                toRet++;
            }
        }
        return toRet;
    }

    public int getYellowTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("yellow")){
                toRet++;
            }
        }
        return toRet;
    }

    public int getOrangeTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("orange")){
                toRet++;
            }
        }
        return toRet;
    }

    public int getRedTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("red")){
                toRet++;
            }
        }
        return toRet;
    }

    public int getPurpleTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("purple")){
                toRet++;
            }
        }
        return toRet;
    }

    public int getBlackTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("black")){
                toRet++;
            }
        }
        return toRet;
    }

    public int getWhiteTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("white")){
                toRet++;
            }
        }
        return toRet;
    }

    public int getWildTrains(){
        int toRet = 0;
        for(TrainCard card: trainCards){
            if(card.getColor().equals("wild")){
                toRet++;
            }
        }
        return toRet;
    }

    public Map<String, Integer> getMyTrainCardCounts(){
        Map<String,Integer> toRet = new HashMap<>();
        toRet.put("wild", getWildTrains());
        toRet.put("white", getWhiteTrains());
        toRet.put("black", getBlackTrains());
        toRet.put("purple", getPurpleTrains());
        toRet.put("red", getRedTrains());
        toRet.put("orange", getOrangeTrains());
        toRet.put("yellow", getYellowTrains());
        toRet.put("green", getGreenTrains());
        toRet.put("blue", getBlueTrains());
        return toRet;
    }

    public boolean removeDcard(DestinationCard card){
        for(int i =0; i<destCards.size(); i++){
            String c1 = destCards.get(i).getCity1();
            String c2 = destCards.get(i).getCity2();
            int pt = destCards.get(i).getPoints();
            if(c1.equals(card.getCity1()) && c2.equals(card.getCity2()) && pt == card.getPoints()){
                destCards.remove(i);
                return true;
            }
        }
        return false;
    }

}
