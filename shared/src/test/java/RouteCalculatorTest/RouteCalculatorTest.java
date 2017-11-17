package RouteCalculatorTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Communication.DestinationCard;
import Communication.Player;
import Communication.Route;
import RouteCalculator.Exception.RouteCalcException;
import RouteCalculator.RouteCalculator;

import static org.junit.Assert.*;
/**
 * Created by pbstr on 11/7/2017.
 */

public class RouteCalculatorTest {

    //--------------------------------------Data to be used for tests -----------------------------

    List<String> testGraph1 = Arrays.asList("Denver", "Salt Lake", "Seattle", "Los Angeles", "New York", "Orlando", "Chicago", "Dallas");

    RouteCalculator testRouteCalculator1 = new RouteCalculator(testGraph1);

    DestinationCard card1 = new DestinationCard("Denver", "Salt Lake", 10);
    DestinationCard card2 = new DestinationCard("Denver", "Orlando", 20);
    DestinationCard cardSeattleNewYork = new DestinationCard("Seattle", "New York", 20);
    DestinationCard cardSeattleSaltLake = new DestinationCard("Seattle", "Salt Lake", 10);

    Player testPlayer = new Player("test", "red");

    Route routeDenvSaltLake = new Route(true, 3, "Denver", "Salt Lake", "blue", false, 1, 4);
    Route routeSeatLos = new Route(true, 3, "Seattle", "Los Angeles", "blue", false, 2, 4);
    Route routeNewYorkOrlando = new Route(true, 6, "New York", "Orlando", "blue", false, 3, 15);
    Route routeDenvChicago = new Route(true, 4, "Denver", "Chicago", "blue", false, 4, 7);
    Route routeSaltLakeLosAngles = new Route(true, 4, "Salt Lake", "Los Angeles", "blue", false, 5, 7);
    Route routeSaltLakeSeattle = new Route(true, 5, "Salt Lake", "Seattle", "blue", false, 6, 10);
    Route routeChicagoNewYork = new Route(true, 5, "Chicago", "New York", "blue", false, 8, 10);
    Route routeChicagoOrlando = new Route(true, 6, "Chicago", "Orlando", "blue", false, 9, 15);
    Route routeLADallas = new Route(true, 6, "Dallas", "Los Angeles", "blue", false, 12, 15);
    Route routeDenverDallas = new Route(true, 4, "Denver", "Dallas", "blue", false, 13, 7);
    Route routeChicagoDallas = new Route(true, 4, "Chicago", "Dallas", "blue", false, 13, 7);

    Route routeUnclaimedDoubleSaltLakeDenver1 = new Route(false, 3, "Denver", "Salt Lake", "blue", true, 10, 4);
    Route routeUnclaimedDoubleSaltLakeDenver2 = new Route(false, 3, "Denver", "Salt Lake", "blue", true, 11, 4);
    Route routeClaimedDoubleSaltLakeDenver1 = new Route(true, 3, "Denver", "Salt Lake", "blue", true, 10, 4);
    Route routeClaimedDoubleSaltLakeDenver2 = new Route(true, 3, "Denver", "Salt Lake", "blue", true, 11, 4);

    List<Route> singleRouteList = Arrays.asList(routeDenvSaltLake);
    List<Route> threeUnconnectedRouts = Arrays.asList(routeSeatLos, routeNewYorkOrlando, routeDenvChicago);
    List<Route> notConnectedList = Arrays.asList(routeDenvSaltLake, routeSeatLos, routeNewYorkOrlando);
    List<Route> notConnectedByOneCity = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles, routeDenvSaltLake, routeDenvChicago, routeNewYorkOrlando);
    List<Route> twoWaysToConnectList = Arrays.asList(routeNewYorkOrlando, routeSaltLakeSeattle, routeSeatLos, routeSaltLakeLosAngles);
    List<Route> twoDisconnectedCycles = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles, routeSaltLakeSeattle, routeNewYorkOrlando, routeChicagoNewYork, routeChicagoOrlando);
    List<Route> twoDisconnectedCyclesBigger = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles, routeSaltLakeSeattle, routeNewYorkOrlando, routeChicagoNewYork, routeChicagoOrlando, routeLADallas);
    List<Route> twoConnectedCycles = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles, routeSaltLakeSeattle, routeDenvSaltLake, routeDenvChicago, routeNewYorkOrlando, routeChicagoNewYork, routeChicagoOrlando);
    List<Route> simpleCycle = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles, routeSaltLakeSeattle, routeDenvSaltLake);
    List<Route> multipleCircles = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles, routeSaltLakeSeattle, routeLADallas, routeDenvSaltLake, routeDenverDallas, routeDenvChicago, routeChicagoDallas, routeChicagoNewYork, routeChicagoOrlando, routeNewYorkOrlando);

    List<Route> doubleRoutesNotClaimed = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles);
    List<Route> doubleRoutesOneClaimed = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles, routeClaimedDoubleSaltLakeDenver1);
    List<Route> doubleRoutesBothClaimed = Arrays.asList(routeSeatLos, routeSaltLakeLosAngles, routeClaimedDoubleSaltLakeDenver1, routeClaimedDoubleSaltLakeDenver2);

    //--------------------------------------- Test the inner graph of the route calculator -----------------------

    @Test
    public void TestRouteCalculator_GraphIsNull_MakesAnEmptyList() throws RouteCalcException{
        RouteCalculator nullGraphCalculator = new RouteCalculator(null);

        assertEquals(0, nullGraphCalculator.calculateLongestRoute(testPlayer, singleRouteList));
        assertEquals(-1 * card1.getPoints(), nullGraphCalculator.calculatePointsForDest(card1, singleRouteList));
    }

    @Test
    public void TestRouteCalculator_GraphIsEmpty_FunctionsReturnProperly() throws RouteCalcException{
        RouteCalculator emtpyGraphCalculator = new RouteCalculator(new ArrayList<String>());

        assertEquals(0, emtpyGraphCalculator.calculateLongestRoute(testPlayer, singleRouteList));
        assertEquals(-1 * card1.getPoints(), emtpyGraphCalculator.calculatePointsForDest(card1, singleRouteList));
    }

    //the other cases of having multiple nodes will be tested in the the other unit tests


    //------------------------------------- TEst CalculatePointsForDest ----------------------------

    @Test(expected = RouteCalcException.class)
    public void TestCalculatePointsForDest_NullCard_ThrowError() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(null, singleRouteList);
    }

    @Test(expected = RouteCalcException.class)
    public void TestCalculatePointsForDest_NullRoutes_ThrowError() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(card1, null);
    }

    @Test(expected = RouteCalcException.class)
    public void TestCalculatePointsForDest_DestCardHasCitiesNotInGraph_ThrowError() throws RouteCalcException{
        DestinationCard badCard = new DestinationCard("blah", "blah", 10);
        int result = testRouteCalculator1.calculatePointsForDest(badCard, singleRouteList);
    }

    @Test(expected = RouteCalcException.class)
    public void TestCalculatePointsForDest_CardHasBadData_ThrowError() throws RouteCalcException{
        DestinationCard badCard2 = new DestinationCard(null, null, 10);
        int result = testRouteCalculator1.calculatePointsForDest(badCard2, singleRouteList);
    }

    @Test(expected = RouteCalcException.class)
    public void TestCalculatePointsForDest_ClaimedRoutesHasBadData_ThrowError() throws RouteCalcException{
        Route badRoute = new Route(true, 5, "Blah", null);
        List<Route> badRoutes = Arrays.asList(badRoute);
        int result = testRouteCalculator1.calculatePointsForDest(card1, badRoutes);
    }

    @Test(expected = RouteCalcException.class)
    public void TestCalculatePointsForDest_ClaimedRouteIsFalse_ThrowsError() throws RouteCalcException{
        Route badRoute = new Route(false, 5, "Blah", "bad");
        List<Route> badRoutes = Arrays.asList(badRoute);
        int result = testRouteCalculator1.calculatePointsForDest(card1, badRoutes);
    }

    @Test
    public void TestCalculatePointsForDest_EmptyListOfRoutes_ReturnsNegativePoints() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(card1, new ArrayList<Route>());
        assertEquals(-1 * card1.getPoints(), result);
    }

    @Test
    public void TestCalculatePointsForDest_DestCardHasUpperCaseCitiesButConnects_ReturnsPositivePoints() throws RouteCalcException{
        DestinationCard upperCard = new DestinationCard("SALT LAKE", "DENver", 20);
        int result = testRouteCalculator1.calculatePointsForDest(upperCard, singleRouteList);
        assertEquals(upperCard.getPoints(), result);

    }

    @Test
    public void TestCalculatePointsForDest_BasicTrueCase_ReturnsPositivePoints() throws RouteCalcException{

        int result = testRouteCalculator1.calculatePointsForDest(card1, singleRouteList);
        assertEquals(card1.getPoints(), result);
    }

    @Test
    public void TestCalculatePointsForDest_BasicFalseCase_ReturnsNegativePoints() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(card2, singleRouteList);
        assertEquals(-1 * card2.getPoints(), result);
    }

    @Test
    public void TestCalculatePointsForDest_EdgesFromBothCitiesButDoNotConnect_ReturnsNegativePoints() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(cardSeattleNewYork, notConnectedList);
        assertEquals(-1 * cardSeattleNewYork.getPoints(), result);
    }

    @Test
    public void TestCalculatePointsForDest_EdgesFromStartCityButDoesNotConnectByOneCity_ReturnsNegativePoints() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(cardSeattleNewYork, notConnectedByOneCity);
        assertEquals(-1 * cardSeattleNewYork.getPoints(), result);
    }

    @Test
    public void TestCalculatePointsForDest_TwoWaysToConnectDestination_ReturnsPositivePoints() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(cardSeattleSaltLake, twoWaysToConnectList);
        assertEquals(cardSeattleSaltLake.getPoints(), result);
    }

    @Test
    public void TestCalculatePointsForDest_RoutesHaveCyclesButNeverConnects_ReturnsNegativePoints() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(cardSeattleNewYork, twoDisconnectedCycles);
        assertEquals(-1 * cardSeattleNewYork.getPoints(), result);
    }

    @Test
    public void TestCalculatePointsForDest_RoutesHaveCyclesButConnects_ReturnsPositivePoints() throws RouteCalcException{
        int result = testRouteCalculator1.calculatePointsForDest(cardSeattleNewYork, twoConnectedCycles);
        assertEquals(cardSeattleNewYork.getPoints(), result);
    }


    //---------------------------- Testing Longest Route Calculations -----------------------------------------------

    @Test(expected = RouteCalcException.class)
    public void TestCalculateLongestRoute_NullPlayer_ThrowsError()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(null, singleRouteList);
    }

    @Test(expected = RouteCalcException.class)
    public void TestCalculateLongestRoute_NullClaimedRoutes_ThrowsError()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(null, singleRouteList);
    }

    @Test(expected = RouteCalcException.class)
    public void TestCalculateLongestRoute_SomeClamedRoutesAreFalse_ThrowsError()  throws RouteCalcException{
        Route badRoute = new Route(false, 5, "Blah", "bad");
        List<Route> badRoutes = Arrays.asList(badRoute);
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, badRoutes);
    }

    @Test
    public void TestCalculateLongestRoute_MultpleOftheSameRoute_ReturnsSizeOfJustOne()  throws RouteCalcException{
        List<Route> badRoutes = Arrays.asList(routeNewYorkOrlando, routeNewYorkOrlando);
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, badRoutes);
        assertEquals(routeNewYorkOrlando.getLength(), result);
    }

    @Test
    public void TestCalculateLongestRoute_EmptyListOfRoutes_Returns0()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, new ArrayList<Route>());
        assertEquals(0, result);
    }

    @Test
    public void TestCalculateLongestRoute_PlayerClaimed0DoubleRoutes_ReturnsCorrectLongestPath() throws RouteCalcException {
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, doubleRoutesNotClaimed);
        assertEquals(7, result);
    }

    @Test
    public void TestCalculateLongestRoute_PlayerClaimed1DoubleRoutes_ReturnsLongestPathWithRoute()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, doubleRoutesOneClaimed);
        assertEquals(10, result);
    }

    @Test
    public void TestCalculateLongestRoute_PlayerClaimedBothDoubleRoutes_ReturnsLongestPathWithOnlyOneRoute()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, doubleRoutesBothClaimed);
        assertEquals(10, result);
    }

    /* I think these tests are already done by the 3 above
    @Test
    public void TestCalculateLongestRoute_HasDoubleRoutes_RetunsCorrectLongestPath() {

    }

    @Test
    public void TestCalculateLongestRoute_HasNoDoubleRoutes_RetunsCorrectLongestPath() {

    }
    */

    @Test
    public void TestCalculateLongestRoute_RoutesHaveACycle_ReturnsCorrectLongestPath()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, simpleCycle);
        assertEquals(15, result);
    }

    @Test
    public void TestCalculateLongestRoute_HaveMultipleCycles_ReturnsCorrectLongestPath()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, simpleCycle);
        assertEquals(15, result);
    }

    @Test
    public void TestCalculateLongestRoute_HaveMultipleDisconnetedCycles_ReturnsCorrectLongestPath()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, twoDisconnectedCycles);
        assertEquals(17, result);
    }

    @Test
    public void TestCalculateLongestRoute_ChangeRouteByOne_GetsNewBiggerLongerDistance()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, twoDisconnectedCyclesBigger);
        assertEquals(18, result);
    }

    @Test
    public void TestCalculateLongestRoute_RoutesHaveNoCycle_ReturnsCorrectLongestPath()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, twoConnectedCycles);
        assertEquals(36, result);
    }

    @Test
    public void TestCalculateLongestRoute_ThreeRoutesClaimedButNotConnected_ReturnsLongestOfTheRoutes()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, threeUnconnectedRouts);
        assertEquals(6, result);
    }

    @Test
    public void TestCalculateLongestRoute_MultipleCircles_ReturnsLongestPathConnectingCirclesOnlyOnce()  throws RouteCalcException{
        int result = testRouteCalculator1.calculateLongestRoute(testPlayer, multipleCircles);
        assertEquals(47, result);
    }
}
