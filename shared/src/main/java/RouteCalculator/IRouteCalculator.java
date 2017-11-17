package RouteCalculator;

import java.util.List;

import Communication.DestinationCard;
import Communication.Player;
import Communication.Route;
import RouteCalculator.Exception.RouteCalcException;

/**
 * Created by pbstr on 11/7/2017.
 */

public interface IRouteCalculator {

    int calculateLongestRoute(Player player, List<Route> claimedRoutes) throws RouteCalcException;
    int calculatePointsForDest(DestinationCard card, List<Route> claimedRoutes) throws RouteCalcException;
}
