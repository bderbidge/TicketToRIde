package Communication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by autumnchapman on 10/17/17.
 */

public class Route {

    private Map<Integer, Integer> pointValues = new HashMap<>();

    private boolean claimed;
    private int length;
    private String city1;
    private String city2;
    private int points;
    private String color;
    private boolean doubleRoute;
    private int id;
    private int doubleID;

    public Route() {}

    public Route(boolean claim, int len, String c1, String c2){
        this.claimed = claim;
        this.length = len;
        this.city1 = c1;
        this.city2 = c2;
    }

    public Route(boolean claim, int len, String c1, String c2, String color, boolean doubleRoute,
                 int id) {
        this.claimed = claim;
        this.length = len;
        this.city1 = c1;
        this.city2 = c2;
        this.color = color;
        this.doubleRoute = doubleRoute;
        this.id = id;
        initializePoints(len);
    }

    public Route(boolean claim, int len, String c1, String c2, String color, boolean doubleRoute,
                 int id, int doubleID) {
        this.claimed = claim;
        this.length = len;
        this.city1 = c1;
        this.city2 = c2;
        this.color = color;
        this.doubleRoute = doubleRoute;
        this.id = id;
        this.doubleID = doubleID;

        initializePoints(len);
    }

    private void initializePoints(int length) {
        switch(length) {
            case 1:
                this.points = 1;
                break;
            case 2:
                this.points = 2;
                break;
            case 3:
                this.points = 4;
                break;
            case 4:
                this.points = 7;
                break;
            case 5:
                this.points = 10;
                break;
            case 6:
                this.points = 15;
                break;
        }
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getColor() {return color;}

    public boolean isDoubleRoute() { return doubleRoute; }

    public int getId() { return id; }

    public int getDoubleID() {
        return doubleID;
    }

    @Override
    public boolean equals(Object o) {
        Route other = (Route) o;
        if((other == null) || (getClass() != other.getClass()))
            return false;

        if(this.id != other.getId())
            return false;

        return true;
    }
}
