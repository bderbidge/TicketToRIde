package RouteCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbstr on 11/10/2017.
 */

public class Util {

    public static List<String> getAllCities() {
        if (allCities == null) {
            allCities = new ArrayList<>();
            allCities.add("Vancouvar");
            allCities.add("Calgary");
            allCities.add("Winnipeg");
            allCities.add("Sault Ste. Marie");
            allCities.add("Toronto");
            allCities.add("Montreal");
            allCities.add("Boston");
            allCities.add("New York");
            allCities.add("Washington DC");
            allCities.add("Raleigh");
            allCities.add("Charleston");
            allCities.add("Atlanta");
            allCities.add("Nashville");
            allCities.add("Miami");
            allCities.add("Pittsburgh");
            allCities.add("Chicago");
            allCities.add("New Orleans");
            allCities.add("Little Rock");
            allCities.add("Saint Louis");
            allCities.add("Houston");
            allCities.add("Dallas");
            allCities.add("Oklahoma City");
            allCities.add("Kansas City");
            allCities.add("Omaha");
            allCities.add("Duluth");
            allCities.add("Santa Fe");
            allCities.add("El Paso");
            allCities.add("Denver");
            allCities.add("Helena");
            allCities.add("Salt Lake City");
            allCities.add("Phoenix");
            allCities.add("Las Vegas");
            allCities.add("Los Angeles");
            allCities.add("San Francisco");
            allCities.add("Portland");
            allCities.add("Seattle");
        }
        return allCities;
    }
    private static List<String> allCities;
}
