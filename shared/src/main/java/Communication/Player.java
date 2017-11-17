package Communication;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by emmag on 9/30/2017.
 */

public class Player {
    private String mColor;
    private String mPlayerID; // aka: their username
    private int trains;
    private int numDestCards;
    private int numTrainCards;
    private int totalPoints;
    private int routePoints;
    private int destPoints;
    private int negDestPoints;
    private boolean longestRoute;

    private Set <Route> routes;

    public Player() {}

    public Player(String playerID, String color) {
        mColor = color;
        mPlayerID = playerID;
        totalPoints = 0;
        routePoints = 0;
        destPoints = 0;
        negDestPoints = 0;
        routes = new HashSet<Route>();
    }

    public String getColor() {
        return mColor;
    }

    public String getUsername() {
        return mPlayerID;
    }

    public void setColor(String mColor) {
        this.mColor = mColor;
    }

    public String getPlayerID() {
        return mPlayerID;
    }

    public void setPlayerID(String mPlayerID) {
        this.mPlayerID = mPlayerID;
    }

    public int getTrains() {
        return trains;
    }

    public void setTrains(int trains) {
        this.trains = trains;
    }

    public int getNumDestCards() {
        return numDestCards;
    }

    public void setNumDestCards(int numDestCards) {
        this.numDestCards = numDestCards;
    }

    public int getNumTrainCards() {
        return numTrainCards;
    }

    public void setNumTrainCards(int numTrainCards) {
        this.numTrainCards = numTrainCards;
    }

    public int getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(int routePoints) {
        this.routePoints = routePoints;
    }

    public int getDestPoints() {
        return destPoints;
    }

    public void setDestPoints(int destPoints) {
        this.destPoints = destPoints;
    }

    public void addDestPoints(int points){
        totalPoints += points;
        destPoints += points;
    }

    public int getNegDestPoints() {
        return negDestPoints;
    }

    public void setNegDestPoints(int negDestPoints) {
        this.negDestPoints = negDestPoints;
    }

    public void addNegDestPoints(int points){
        totalPoints += points;
        negDestPoints += points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int points) {
        totalPoints = points;
    }

    public void addTotalPoints(int points) { totalPoints = totalPoints + points; }

    private void addRoutePoints(int points) {
        totalPoints += points;
        routePoints += points;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<Route> routes) {
        this.routes = routes;
    }

    public void claimRoute(Route r) {
        routes.add(r);
        trains = trains - r.getLength();
        addRoutePoints(r.getPoints());
        setNumTrainCards(numTrainCards - r.getLength());
     }

    public boolean isLongestRoute() {
        return longestRoute;
    }

    public void setLongestRoutePoints(boolean longestRoute) {
        // 10 is how many points longest road is worth
        if(!longestRoute) {
            totalPoints = totalPoints - 10;
        }
        else if(longestRoute) {
            totalPoints = totalPoints + 10;
        }
        this.longestRoute = longestRoute;
    }

    public void setLongestRoute(boolean longestRoute){
        this.longestRoute = longestRoute;
    }
}
