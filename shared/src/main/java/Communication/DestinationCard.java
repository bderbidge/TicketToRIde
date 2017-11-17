package Communication;

/**
 * Created by autumnchapman on 10/17/17.
 */

public class DestinationCard {

    private String city1;
    private String city2;
    private int points;

    public DestinationCard(){}

    public DestinationCard(String city1, String city2, int points){
        this.city1 = city1;
        this.city2 = city2;
        this.points = points;
    }

    public String getCity1() {
        return city1;
    }

    public String getCity2() {
        return city2;
    }

    public int getPoints() {
        return points;
    }

}
