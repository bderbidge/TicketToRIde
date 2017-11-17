package RouteCalculator;

import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import RouteCalculator.VisitableWeightedEdge;


/**
 * Extends the SimpleWeightedGraph class to add a longest continuous path
 * method and a connected nodes method
 */
public class TicketToRideGraph extends SimpleWeightedGraph<String, VisitableWeightedEdge>
{
    private ConnectivityInspector<String, VisitableWeightedEdge> inspector;

    public TicketToRideGraph(EdgeFactory<String, VisitableWeightedEdge> ef)
    {
        super(ef);
        initInspector();
    }

    public TicketToRideGraph(Class<? extends VisitableWeightedEdge> edgeClass)
    {
        this(new ClassBasedEdgeFactory<String, VisitableWeightedEdge>(edgeClass));
        initInspector();
    }

    /**
     * Initialize the inspector for the graph
     */
    private void initInspector()
    {
        inspector = new ConnectivityInspector<>(this);
    }

    /**
     * Gets the longest continuous path of edges
     * @return the longest continuous path
     */
    public int getLongestPath()
    {
        int longestPath = 0;
        for(String city : vertexSet())
        {
            int iterLongPath = longestPathRec(city);
            if(iterLongPath > longestPath)
            {
                longestPath = iterLongPath;
            }
        }
        return longestPath;
    }

    /**
     * Recursively branches out from the city to find the longest continuous path
     * out from the city
     * @param city the start vertex
     * @return the longest continuous path that branches out from the city
     */
    public int longestPathRec(String city)
    {
        int additionalPathLength = 0;

        Set<VisitableWeightedEdge> edges = edgesOf(city);
        for(VisitableWeightedEdge edge: edges) // iterate through all edges off city
        {
            int iterLength = 0;
            if(!edge.isVisited())
            {
                edge.setVisited(true);
                String targetCity = getEdgeTarget(edge);
                if(targetCity.equals(city))
                {
                    targetCity = getEdgeSource(edge);
                }
                int callLength = (int) (getEdgeWeight(edge));
                iterLength = callLength + longestPathRec(targetCity);
                if(iterLength > additionalPathLength)
                {
                    additionalPathLength = iterLength;
                }
                edge.setVisited(false);
            }
        }
        return additionalPathLength;
    }

    /**
     * Returns true if there is a path between city1 and city2
     * @param city1 first connection point
     * @param city2 second connection point
     * @return whether there is a path between city1 and city2
     */
    public boolean pathExists(String city1, String city2)
    {
        if(containsVertex(city1) && containsVertex(city2))
        {
            return inspector.pathExists(city1, city2);
        }
        else
        {
            return false;
        }
    }

    /**
     * Clear all vertices and edges
     */
    public void clear()
    {
        clearEdges();
        clearVertices();
    }

    /**
     * Clear all edges
     */
    public void clearEdges()
    {
        List<VisitableWeightedEdge> copy = new LinkedList<>();
        {
            for(VisitableWeightedEdge edge : edgeSet())
            {
                copy.add(edge);
            }
            removeAllEdges(copy);
        }
    }

    /**
     * Clear all vertices
     */
    public void clearVertices()
    {
        List<String> copy = new LinkedList<>();
        for(String vertex : vertexSet())
        {
            copy.add(vertex);
        }
        removeAllVertices(copy);
    }
}
