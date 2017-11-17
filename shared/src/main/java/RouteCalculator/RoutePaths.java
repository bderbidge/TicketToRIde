package RouteCalculator;

import java.util.Collections;

import Communication.Route;
import RouteCalculator.Exception.RouteCalcException;
import RouteCalculator.TicketToRideGraph;
import RouteCalculator.VisitableWeightedEdge;

/**
 * Holds information about the connections formed by player owned
 * routes
 */
public class RoutePaths
{

    private TicketToRideGraph playerGraph;

    public RoutePaths()
    {
        playerGraph = new TicketToRideGraph(VisitableWeightedEdge.class);
    }

    /**
     * Adds the route to the connections
     * @param route the route to be added
     */
    public void add(Route route) throws RouteCalcException
    {
        VisitableWeightedEdge edge = new VisitableWeightedEdge();
        if(!playerGraph.vertexSet().contains(route.getCity1())) // graph does not contain cityA
        {
            //playerGraph.addVertex(route.getCity1());
            throw new RouteCalcException("The route has a city " + route.getCity1() + " that does not exist as a vertex in the calculator, graph has " + playerGraph.vertexSet().toString() +  " cannot add route to the graph");
        }
        if(!playerGraph.vertexSet().contains(route.getCity2())) // graph does not contain cityB
        {
            //playerGraph.addVertex(route.getCity2());
            throw new RouteCalcException("The route has a city " + route.getCity2() + " that does not exist as a vertex in the calculator, cannot add route to the graph");
        }
        playerGraph.addEdge(route.getCity1(), route.getCity2(), edge);
        playerGraph.setEdgeWeight(edge, route.getLength());
    }

    public void addCity(String city) {
        if(!playerGraph.vertexSet().contains(city)) {
            playerGraph.addVertex(city);
        }
    }

    /**
     * Returns true if the destination has been completed
     * @param cityA the name of the starting city
     * @param cityB the name of the ending city
     * @return completion of the destination
     */
    public boolean destinationComplete(String cityA, String cityB)
    {
        return playerGraph.pathExists(cityA, cityB);
    }

    /**
     * Gets the length of longest path of connected routes
     * @return the length of longest path of connected routes
     */
    public int longestPath()
    {
        return playerGraph.getLongestPath();
    }

    /**
     * Clears the set of routes
     */
    public void clear()
    {
        playerGraph.clear();
    }

    /**
     * Clears the edges from the graph
     */
    public void clearEdges() {
        playerGraph.clearEdges();
    }

    /**
     * Tells if the graph is empty or not
     * @return
     */
    public boolean isEmpty() {
        if (playerGraph.vertexSet() == null || playerGraph.vertexSet().size() == 0) {
            return true;
        }
        return false;
    }

    public boolean cityExists(String city) {
        return playerGraph.vertexSet().contains(city);
    }
}
