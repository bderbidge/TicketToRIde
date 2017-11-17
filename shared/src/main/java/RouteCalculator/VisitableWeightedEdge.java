package RouteCalculator;/*
 * Created by Tom on 6/5/2017.
 */

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * An edge class that keeps track of whether it has been visited
 */
public class VisitableWeightedEdge extends DefaultWeightedEdge
{
    private boolean visited = false;

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

}
