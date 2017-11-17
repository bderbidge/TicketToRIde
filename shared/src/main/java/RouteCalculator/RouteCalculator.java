package RouteCalculator;

import java.util.List;
import java.util.logging.Logger;

import Communication.DestinationCard;
import Communication.Player;
import Communication.Route;
import RouteCalculator.Exception.RouteCalcException;
/**
 * Created by pbstr on 11/7/2017.
 */

/**
 * This calculates the longest path and if a destination card was reached or not by using the jgrapht library
 */
public class RouteCalculator implements IRouteCalculator{

    private RoutePaths myPaths; //the actual graph class

    public RouteCalculator(List<String> allCities) {

        myPaths = new RoutePaths();
        //creates an empty graph if null cities are given
        if (allCities != null) {
            for (String city : allCities) {
                myPaths.addCity(city.toLowerCase());
            }
        }
    }

    /**
     * Gets the longest path of the claimed routes for the player
     * @param player the player that you want to get the points for
     * @param claimedRoutes the routes the player has claimed
     *
     * @pre player != null
     * @pre claimedRoutes != null, the cities in the routes are the same cities as in the graph already
     * @return
     * @throws RouteCalcException thrown if any error occurs in the function
     */
    public int calculateLongestRoute(Player player, List<Route> claimedRoutes) throws RouteCalcException{

        //case if the graph is empty, it will return 0 instead of trying to calcualte the routes
        if(myPaths.isEmpty()) {
           return 0;
        }

        for(Route route : claimedRoutes) {
            //makes all the values lowercased so that everything is consistent to the graph
            Route loweredRoute = new Route(route.isClaimed(), route.getLength(), route.getCity1().toLowerCase(), route.getCity2().toLowerCase(), route.getColor(), route.isDoubleRoute(), route.getId(), route.getPoints());
            myPaths.add(loweredRoute); //add the edges to the graph
        }
        int longestPath = myPaths.longestPath(); //use longestPath functionality to find the longest path
        myPaths.clearEdges(); //clear all edges for a new call made to this function

       return longestPath;
    }

    /**
     * Determines if the player has reached the destinations on the card
     * @param card the card that has the two locations
     * @param claimedRoutes the routes the player has claimed
     *
     * @pre card != null, has cities that are the same cities as in the graph already,
     * @pre claimedRoutes != null, the cities in the routes are the same cities as in the graph already
     * @return positive points of the card if the player reached the destination, negative points if not
     * @throws RouteCalcException
     */
    public int calculatePointsForDest(DestinationCard card, List<Route> claimedRoutes) throws RouteCalcException {

        //give negative points, nothing to check if the graph is empty
        if(myPaths.isEmpty()) {
            return -1 * card.getPoints();
        }

        //lower all cities to make sure everything is consistent
        DestinationCard loweredDCard = new DestinationCard(card.getCity1().toLowerCase(), card.getCity2().toLowerCase(), card.getPoints());
        for(Route route: claimedRoutes) {
            Route loweredRoute = new Route(route.isClaimed(), route.getLength(), route.getCity1().toLowerCase(), route.getCity2().toLowerCase(), route.getColor(), route.isDoubleRoute(), route.getId(), route.getPoints());
            //add the route as an edge to the graph
            myPaths.add(loweredRoute);
        }

        int points = loweredDCard.getPoints();
        //checks if the destination was completed from the graph functions, gives negative points if not
        if (!myPaths.destinationComplete(loweredDCard.getCity1(), loweredDCard.getCity2())) {
            points = points * -1;
        }
        return points;
    }


    /**
     * Validates the input for the calculateLongestPath function, throws exceptions for invalid input with a message describing the problem
      * @param player
     * @param claimedRoutes
     * @throws RouteCalcException
     */
    private void validateInput(Player player, List<Route> claimedRoutes) throws RouteCalcException{
        if (player == null) {
            //Log.e(TAG, "Player was null in calculateLongestRoute");
            throw new RouteCalcException("The player given is null, cannot calculate longest route");
        }
        if (claimedRoutes == null) {
            throw new RouteCalcException("The claimed routes is null, cannot calculate longest route");
        }
        for(Route route : claimedRoutes) {
            if (!route.isClaimed()) {
                throw new RouteCalcException("The route from " + route.getCity1() + " to " + route.getCity2() + " is not claimed, cannot calculate longest route");
            }
        }
    }

    /**
     * Validates the input for the calcPointsForDCard function, throws exceptions for invalid input with a message describing the problem
     * @param card card given by caller
     * @param claimedRoutes claimed routes given by caller
     * @throws RouteCalcException
     */
    private void validateInput(DestinationCard card, List<Route> claimedRoutes) throws RouteCalcException {
        if (card == null) {
            throw new RouteCalcException("The destination card is null, cannot calc points for the dcard");
        }
        if (claimedRoutes == null) {
            throw new RouteCalcException("The claimed routes is null, cannot calc points for the dcard");
        }
        if (card.getCity1() == null || card.getCity2() == null) {
            throw new RouteCalcException("The dcard has bad data in it, cannot calc points for the dcard");
        }
        for(Route route: claimedRoutes) {
            if (route == null) {
                throw new RouteCalcException("A route was null in the claimed routes list, cannot calc points for the dcard");
            }
            if (route.getCity1() == null || route.getCity2() == null) {
                throw new RouteCalcException("The route has null cities, cannot calculate longest route");
            }
            if(!route.isClaimed()) {
                throw new RouteCalcException("The route from " + route.getCity1() + " to " + route.getCity2() + " is not claimed, cannot calculate longest route");
            }
        }

        if(!myPaths.cityExists(card.getCity1().toLowerCase())) {
            throw new RouteCalcException("The dcard has the city " + card.getCity1() + " but the graph does not, cannot calculate points for dcard");
        }
        if(!myPaths.cityExists(card.getCity2().toLowerCase())) {
            throw new RouteCalcException("The dcard has the city " + card.getCity2() + " but the graph does not, cannot calculate points for dcard");
        }
    }
}
