package com.example.brandonderbidge.myapplication;

import android.graphics.Point;
import android.support.v4.util.Pair;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Communication.Route;

/**
 * Created by emilyprigmore on 11/1/17.
 *
 * This class is in charge of processing touch events when a user is claiming a route.
 * It is a helper class for the Map Activity
 */

public class TouchProcessor {

    private Map<Integer, Route> mRouteIDs;
    private Map<Integer, Pair<Point, Point>> mRouteIDLocations;
    private Map<Integer, Pair<Point, Point>> mAvailableRoutes;

    public TouchProcessor() {
        mRouteIDs = BoardDrawer.getInstance().getRouteIDs();
        mRouteIDLocations = BoardDrawer.getInstance().getRouteIDLocations();
    }

    /**
     * This method will process the touch event and return the route closest to the touch event.
     * @pre event, map != null
     * @pre mRouteIDLocations, mRouteIDs != null
     * @post Route != if event was close enough to a route, Route = null if event not close enough
     *          to route
     *
     * @param event the MotionEvent of where the user pressed down
     * @param map the ImageView showing the map and routes, needed for scaling the press location to
     *            the location on the canvas/bitmap
     * @return Route - the route closest to where the MotionEvent occurred; if not within a buffer
     *                  of 5, the method returns null
     */
    public Route processTouch(MotionEvent event, ImageView map) {
        if(mAvailableRoutes == null) {
            mAvailableRoutes = new HashMap<>(mRouteIDLocations);    // shallow copy
        }
        double x = event.getX();
        double y = event.getY();
        double xScaleFactor = (double) map.getWidth() / (double) 850;    // 850 is bitmap width
        double yScaleFactor = (double) map.getHeight() / (double) 575;   // 575 is bitmap height
        double realX = x / xScaleFactor;
        double realY = y / yScaleFactor;

        Point touchPoint = new Point((int) realX, (int) realY);
        double shortestDistance = 1000; // a large number that is larger than any difference we will see
        Route toClaim = null;
        //for (Integer id : mRouteIDLocations.keySet()) {
          //  Pair<Point, Point> pair = mRouteIDLocations.get(id);

        for (Integer id : mAvailableRoutes.keySet()) {
            Pair<Point, Point> pair = mAvailableRoutes.get(id);
            double distance = computeDistanceToLine(pair.first, pair.second, touchPoint);
            if (distance < shortestDistance && distance < 5) {   // buffer on the route width
                if (checkX(pair.first.x, pair.second.x, touchPoint.x) &&    // check if click is in route bounds
                        checkY(pair.first.y, pair.second.y, touchPoint.y)) {
                    toClaim = mRouteIDs.get(id);
                    shortestDistance = distance;
                }
            }
        }
        return toClaim;
    }

    /**
     * This check is to make sure the motion event is within the x bounds of the route.
     * It is a helper method for processTouch above.
     * @pre x1, x2, x are real numbers
     * @pre x1 != x2
     * @post the method will return true or false
     *
     * @param x1 one bound (lower or upper) on the x range of the route
     * @param x2 second bound (lower or upper) on the x range of the route
     * @param x the x location of the motion event
     * @return T/F depending on whether or not the motion event is in the x range
     */
    private boolean checkX(double x1, double x2, double x) {
        if ((x1 < x && x < x2) || (x2 < x && x < x1))    // the route could read R to L or L to R
            return true;
        return false;
    }
    /**
     * This check is to make sure the motion event is within the y bounds of the route.
     * It is a helper method for processTouch above.
     * @pre y1, y2, y are real numbers
     * @pre y1 != y2
     * @post the method will return true or false
     *
     * @param y1 one bound (lower or upper) on the y range of the route
     * @param y2 second bound (lower or upper) on the y range of the route
     * @param y the y location of the motion event
     * @return T/F depending on whether or not the motion event is in the y range
     */
    private boolean checkY(double y1, double y2, double y) {
        // the route could read up-down or down-up
        if ((y1 - 5 < y && y < y2 + 5) || (y2 - 5 < y && y < y1 + 5))
            return true;
        return false;
    }

    /**
     * This method calculates the distance between a given point and a line between two other points.
     * @pre p1, p2, p3 != null
     * @post distance returned >= 0
     *
     * @param p1 one point in the line
     * @param p2 second point in the line
     * @param p3 the point in which the distance to the line is being calculated, in this case, the
     *           point of the touch event
     * @return the distance between p3 and the line made up by p1 and p2
     */
    public double computeDistanceToLine(Point p1, Point p2, Point p3) {
        // line is made up of p1 and p2
        double numerator = ((p2.y - p1.y) * p3.x) - ((p2.x - p1.x) * p3.y) + (p2.x * p1.y) - (p2.y * p1.x);
        double denominator = Math.pow((p2.y - p1.y), 2) + Math.pow((p2.x - p1.x), 2);
        double distance = Math.abs(numerator) / Math.sqrt(denominator);
        return distance;
    }

    public List<Route> claimsToList(Map<String, List<Route>> claims) {  // helper for removing claims
        List<Route> allClaimedRoutes = new ArrayList<>();
        for(List<Route> list : claims.values()) {
            for(Route route : list) {
                allClaimedRoutes.add(route);
            }
        }
        return allClaimedRoutes;
    }


    public void remove(List<Route> routes, int numPlayers) {
        if(mAvailableRoutes == null) {
            mAvailableRoutes = new HashMap<>(mRouteIDLocations);    // shallow copy
        }
        for(Route route : routes) {
            mAvailableRoutes.remove(route.getId());
            if(route.isDoubleRoute() && numPlayers < 4) {   // no double routes in 2-3 player game
                removeDouble(route);
            }
        }
    }

    // removes the double route from the list of available routes.
    private void removeDouble(Route route) {
        int idToRemove = 0;
        for(Integer id : mAvailableRoutes.keySet()) {
            if(mRouteIDs.get(id).getCity1().equals(route.getCity1()) &&
                    mRouteIDs.get(id).getCity2().equals(route.getCity2())) {
                idToRemove = id;
            }
        }
        mAvailableRoutes.remove(idToRemove);
    }
}
