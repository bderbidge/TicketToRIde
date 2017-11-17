package com.example.brandonderbidge.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Communication.Player;
import Communication.Route;

/**
 * Created by emilyprigmore on 10/28/17.
 *
 * This class is in charge of all drawing in the map activity.
 * It is a helper class to the map activity.
 */

public class BoardDrawer {

    private static BoardDrawer mInstance = new BoardDrawer();

    private Map<String, Pair<Point, Point>> mCities;
    private List<Route> mRoutes = new ArrayList<>();
    private Map<Integer, Route> mRouteIDs = new HashMap<>();
    private Map<Integer, Pair<Point, Point>> mRouteIDLocations = new HashMap<>();
    private DrawingUtil mDrawingUtil;

    /**
     * @pre this constructor is only called once (? don't know if this is a pre condition)
     * @post mCities, mRoutes, mRouteIDs != null
     */
    public BoardDrawer() {
        loadCities();
        loadRoutes();
    }

    public static BoardDrawer getInstance() { return mInstance; }

    public Map<String, Pair<Point, Point>> getCities() {
        return mCities;
    }

    public List<Route> getRoutes() {
        return mRoutes;
    }

    public Map<Integer, Route> getRouteIDs() {
        return mRouteIDs;
    }

    public Map<Integer, Pair<Point, Point>> getRouteIDLocations() {
        return mRouteIDLocations;
    }

    public DrawingUtil getDrawingUtil() {
        return mDrawingUtil;
    }

    /**
     * @pre context != null
     * @pre mDrawingUtil == null
     * @post mDrawingUtil != null
     *
     * @param context the application context using this board drawer and drawing util class
     */
    public void setDrawingUtil(Context context) {
        mDrawingUtil = new DrawingUtil(context);
    }

    /**
     * This method will initialize the bitmap used to draw the routes and cities
     * @pre mCities != null
     * @pre mRoutes != null
     * @post the method will return a map with all cities in mCities and all routes in mRoutes drawn
     * @return the bitmap with drawn routes and cities
     */
    public Bitmap drawBoard() {
            Bitmap bitmap = Bitmap.createBitmap(850, 575, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);

            for (String s : mCities.keySet()) {
                int x1 = mCities.get(s).first.x;
                int y1 = mCities.get(s).first.y;
                canvas.drawCircle(x1, y1, 3, paint);
                int x2 = mCities.get(s).second.x;
                int y2 = mCities.get(s).second.y;
                canvas.drawText(s, x2, y2, paint);
            }
            for (int i = 0; i < mRoutes.size(); i++) {
                if (mRoutes.get(i).isDoubleRoute()) {
                    drawTwoPaths(mRoutes.get(i), mRoutes.get(i + 1), bitmap);
                    i++;
                } else {
                    drawPath(mRoutes.get(i), bitmap);
                }
            }
        return bitmap;
    }

    /**
     * This method draws the claims on the map.
     * @pre mRouteIDLocations != null
     * @pre mDrawingUtil != null
     * @pre all parameters != null
     * @post given bitmap will have all claims drawn to it
     *
     * @param bm    the current bitmap of the game
     * @param claims    a map of username to list of claims
     * @param players   a map of username to player
     * @return  the given bitmap with newly drawn claims
     */
    public Bitmap drawClaimedRoutes(Bitmap bm, Map<String, List<Route>> claims, Map<String, Player> players) {
            Canvas canvas = new Canvas(bm);
            for(String s : claims.keySet()) {
                for(Route r : claims.get(s)) {
                    Pair<Point, Point> points = mRouteIDLocations.get(r.getId());

                    Path path = mDrawingUtil.getPathToDraw(points.first, points.second);
                    Paint paint = mDrawingUtil.setPaint(r, players.get(s).getColor(),
                            points.first, points.second);
                    canvas.drawPath(path, paint);
                }
            }
            return bm;
    }

    /**
     * This class is in charge of drawing single routes to the given bitmap
     * @pre route, bm != null
     * @pre mDrawingUtil, mCities, mRouteIDLocations != null
     * @post given bitmap will have drawn the single route given as a parameter
     * @post mRouteIDLocations will have another route to location pair
     * @post mRouteIDLocations != null
     *
     * @param route the route to be drawn to the bitmap
     * @param bm    the bitmap to add drawings to
     * @return  the bitmap with newly drawn route
     */
    public Bitmap drawPath(Route route, Bitmap bm) {
        Canvas canvas = new Canvas(bm);

        Point point1 = mCities.get(route.getCity1()).first;
        Point point2 = mCities.get(route.getCity2()).first;
        Path path = mDrawingUtil.getPathToDraw(point1, point2);
        //mAvailableRoutes.put(route, new Pair<>(point1, point2));
        mRouteIDLocations.put(route.getId(), new Pair<>(point1, point2));

        Paint paint = mDrawingUtil.setPaint(route, route.getColor(), point1, point2);
        canvas.drawPath(path, paint);
        return bm;
    }

    /**
     * This class is for drawing double routes. It will calculate the slope of the given routes and
     * determine whether to adjust the x or y values on the routes in an attempt to make separated,
     * somewhat parallel lines.
     * @pre r1, r2, bm != null
     * @pre mCities, mRouteIDLocations, mDrawingUtil != null
     * @post given bitmap will have newly drawn, almost parallel lines
     * @post mRouteIDLocations will have two more routes added with the adjusted locations
     * @post mRouteIDLocations != null
     *
     * @param r1 the first double route
     * @param r2 the second double route
     * @param bm the bitmap to add drawings to
     * @return the bitmap with newly drawn routes
     */
    public Bitmap drawTwoPaths(Route r1, Route r2, Bitmap bm) {
        Canvas canvas = new Canvas(bm);
        Point point1 = mCities.get(r1.getCity1()).first;
        Point point2 = mCities.get(r1.getCity2()).first;

        int x1 = point1.x;
        int y1 = point1.y;
        int x2 = point2.x;
        int y2 = point2.y;

        Path path1;
        Path path2;

        double slope = Math.abs((y2 - y1) / (x2 - x1));

        if (slope >= 1) {    // more vertical line, alter x
            // Path 1
            int path1_x1 = x1 + 3;
            int path1_x2 = x2 + 3;
            Point path1_p1 = new Point(path1_x1, y1);
            Point path1_p2 = new Point(path1_x2, y2);
            path1 = mDrawingUtil.getPathToDraw(path1_p1, path1_p2);
            Paint paint1 = mDrawingUtil.setPaint(r1, r1.getColor(), path1_p1, path1_p2);
            canvas.drawPath(path1, paint1);
            //mAvailableRoutes.put(r1, new Pair<>(path1_p1, path1_p2));
            mRouteIDLocations.put(r1.getId(), new Pair<>(path1_p1, path1_p2));

            // Path 2
            int path2_x1 = x1 - 3;
            int path2_x2 = x2 - 3;
            Point path2_p1 = new Point(path2_x1, y1);
            Point path2_p2 = new Point(path2_x2, y2);
            path2 = mDrawingUtil.getPathToDraw(path2_p1, path2_p2);
            Paint paint2 = mDrawingUtil.setPaint(r2, r2.getColor(), path2_p1, path2_p2);
            canvas.drawPath(path2, paint2);
            //mAvailableRoutes.put(r2, new Pair<>(path2_p1, path2_p2));
            mRouteIDLocations.put(r2.getId(), new Pair<>(path2_p1, path2_p2));
        }
        else {  // more horizontal line, alter y
            // Path 1
            int path1_y1 = y1 + 3;
            int path1_y2 = y2 + 3;
            Point path1_p1 = new Point(x1, path1_y1);
            Point path1_p2 = new Point(x2, path1_y2);
            path1 = mDrawingUtil.getPathToDraw(path1_p1, path1_p2);
            Paint paint1 = mDrawingUtil.setPaint(r1, r1.getColor(), path1_p1, path1_p2);
            canvas.drawPath(path1, paint1);
            //mAvailableRoutes.put(r1, new Pair<>(path1_p1, path1_p2));
            mRouteIDLocations.put(r1.getId(), new Pair<>(path1_p1, path1_p2));

            // Path 2
            int path2_y1 = y1 - 3;
            int path2_y2 = y2 - 3;
            Point path2_p1 = new Point(x1, path2_y1);
            Point path2_p2 = new Point(x2, path2_y2);
            path2 = mDrawingUtil.getPathToDraw(path2_p1, path2_p2);
            Paint paint2 = mDrawingUtil.setPaint(r2, r2.getColor(), path2_p1, path2_p2);
            canvas.drawPath(path2, paint2);
            //mAvailableRoutes.put(r2, new Pair<>(path2_p1, path2_p2));
            mRouteIDLocations.put(r2.getId(), new Pair<>(path2_p1, path2_p2));
        }
        return bm;
    }

    /**
     * This method will initialize the private data member mCities.
     * @pre mCities not initialized
     * @post mCities will hold all cities in the game
     * @post mCities != null
     *
     */
    public void loadCities() {
        Map<String, Pair<Point, Point>> cities = new HashMap<>();

        cities.put("Vancouvar", new Pair<>(new Point(100, 20), new Point(60, 15)));
        cities.put("Calgary", new Pair<>(new Point(220, 15), new Point(180, 10)));
        cities.put("Winnipeg", new Pair<>(new Point(400, 50), new Point(400, 45)));
        cities.put("Sault Ste. Marie", new Pair<>(new Point(590, 115), new Point(570, 100)));
        cities.put("Toronto", new Pair<>(new Point(650, 150), new Point(645, 150)));
        cities.put("Montreal", new Pair<>(new Point(715, 90), new Point(710, 80)));
        cities.put("Boston", new Pair<>(new Point(755, 145), new Point(760, 140)));
        cities.put("New York", new Pair<>(new Point(725, 190), new Point(730, 190)));
        cities.put("Washington DC", new Pair<>(new Point(715, 260), new Point(720, 260)));
        cities.put("Raleigh", new Pair<>(new Point(660, 310), new Point(670, 310)));
        cities.put("Charleston", new Pair<>(new Point(670, 380), new Point(675, 380)));
        cities.put("Atlanta", new Pair<>(new Point(595, 360), new Point(605, 360)));
        cities.put("Nashville", new Pair<>(new Point(585, 320), new Point(570, 315)));
        cities.put("Miami", new Pair<>(new Point(680, 530), new Point(680, 540)));
        cities.put("Pittsburgh", new Pair<>(new Point(645, 220), new Point(615, 210)));
        cities.put("Chicago", new Pair<>(new Point(530, 220), new Point(540, 235)));
        cities.put("New Orleans", new Pair<>(new Point(520, 455), new Point(530, 460)));
        cities.put("Little Rock", new Pair<>(new Point(480, 345), new Point(485, 350)));
        cities.put("Saint Louis", new Pair<>(new Point(505, 280), new Point(515, 280)));
        cities.put("Houston", new Pair<>(new Point(440, 450), new Point(440, 465)));
        cities.put("Dallas", new Pair<>(new Point(420, 420), new Point(430, 420)));
        cities.put("Oklahoma City", new Pair<>(new Point(415, 330), new Point(410, 325)));
        cities.put("Kansas City", new Pair<>(new Point(440, 270), new Point(445, 270)));
        cities.put("Omaha", new Pair<>(new Point(425, 220), new Point(435, 220)));
        cities.put("Duluth", new Pair<>(new Point(475, 160), new Point(465, 155)));
        cities.put("Santa Fe", new Pair<>(new Point(270, 330), new Point(275, 325)));
        cities.put("El Paso", new Pair<>(new Point(280, 420), new Point(260, 435)));
        cities.put("Denver", new Pair<>(new Point(300, 250), new Point(305, 235)));
        cities.put("Helena", new Pair<>(new Point(270, 130), new Point(230, 140)));
        cities.put("Salt Lake City", new Pair<>(new Point(205, 230), new Point(195, 225)));
        cities.put("Phoenix", new Pair<>(new Point(185, 350), new Point(175, 365)));
        cities.put("Las Vegas", new Pair<>(new Point(150, 300), new Point(140, 315)));
        cities.put("Los Angeles", new Pair<>(new Point(100, 350), new Point(25, 350)));
        cities.put("San Francisco", new Pair<>(new Point(60, 245), new Point(65, 245)));
        cities.put("Portland", new Pair<>(new Point(80, 100), new Point(25, 100)));
        cities.put("Seattle", new Pair<>(new Point(85, 60), new Point(40, 60)));

        mCities = cities;
    }

    /**
     * This method will initialize mRoutes and mRouteIDs
     * @pre mRoutes and mRouteIDs not initialized
     * @post mRoutes, mRouteIDs != null
     * @post mRoutes, mRouteIDs will be initialized with all possible routes for the game.
     */
    public void loadRoutes() {

        Route route1 = new Route(false, 3, "Vancouvar", "Calgary", "blank", false, 1);
        Route route2 = new Route(false, 1, "Vancouvar", "Seattle", "blank", true, 2, 3);
        Route route3 = new Route(false, 1, "Vancouvar", "Seattle", "blank", true, 3, 2);
        Route route4 = new Route(false, 6, "Calgary", "Winnipeg", "white", false, 4);
        Route route5 = new Route(false, 4, "Calgary", "Seattle", "blank", true, 5, 6);
        Route route6 = new Route(false, 4, "Calgary", "Seattle", "blank", true, 6, 5);
        Route route7 = new Route(false, 6, "Winnipeg", "Sault Ste. Marie", "blank", false, 7);
        Route route8 = new Route(false, 5, "Sault Ste. Marie", "Montreal", "black", false, 8);
        Route route9 = new Route(false, 2, "Montreal", "Boston", "blank", true, 9, 10);
        Route route10 = new Route(false, 2, "Montreal", "Boston", "blank", true, 10, 9);
        Route route11 = new Route(false, 3, "Montreal", "New York", "blue", false, 11);
        Route route12 = new Route(false, 3, "Montreal", "Toronto", "blank", false, 12);
        Route route13 = new Route(false, 2, "Boston", "New York", "yellow", true, 13, 14);
        Route route14 = new Route(false, 2, "Boston", "New York", "red", true, 14, 13);
        Route route15 = new Route(false, 2, "Toronto", "Pittsburgh", "blank", false, 15);
        Route route16 = new Route(false, 4, "Toronto", "Chicago", "white", false, 16);
        Route route17 = new Route(false, 6, "Duluth", "Toronto", "purple", false, 17);
        Route route18 = new Route(false, 2, "Sault Ste. Marie", "Toronto", "blank", false, 18);
        Route route19 = new Route(false, 2, "Washington DC", "New York", "orange", true, 19, 20);
        Route route20 = new Route(false, 2, "Washington DC", "New York", "black", true, 20, 19);
        Route route21 = new Route(false, 2, "Pittsburgh", "Washington DC", "blank", false, 21);
        Route route22 = new Route(false, 2, "New York", "Pittsburgh", "white", true, 22, 23);
        Route route23 = new Route(false, 2, "New York", "Pittsburgh", "green", true, 23, 22);
        Route route24 = new Route(false, 3, "Pittsburgh", "Chicago", "orange", true, 24, 25);
        Route route25 = new Route(false, 3, "Pittsburgh", "Chicago", "black", true, 25, 24);
        Route route26 = new Route(false, 2, "Pittsburgh", "Raleigh", "blank", false, 26);
        Route route27 = new Route(false, 5, "Saint Louis", "Pittsburgh", "green", false, 27);
        Route route28 = new Route(false, 4, "Pittsburgh", "Nashville", "yellow", false, 28);
        Route route29 = new Route(false, 2, "Raleigh", "Charleston", "blank", false, 29);
        Route route30 = new Route(false, 2, "Raleigh", "Atlanta", "blank", true, 30, 31);
        Route route31 = new Route(false, 2, "Raleigh", "Atlanta", "blank", true, 31, 30);
        Route route32 = new Route(false, 3, "Raleigh", "Nashville", "black", false, 32);
        Route route33 = new Route(false, 2, "Raleigh", "Washington DC", "blank", true, 33, 34);
        Route route34 = new Route(false, 2, "Raleigh", "Washington DC", "blank", true, 34, 33);
        Route route35 = new Route(false, 1, "Atlanta", "Nashville", "blank", false, 35);
        Route route36 = new Route(false, 2, "Atlanta", "Charleston", "blank", false, 36);
        Route route37 = new Route(false, 5, "Atlanta", "Miami", "blue", false, 37);
        Route route38 = new Route(false, 4, "Atlanta", "New Orleans", "yellow", true, 38, 39);
        Route route39 = new Route(false, 4, "Atlanta", "New Orleans", "orange", true, 39, 38);
        Route route40 = new Route(false, 4, "Miami", "Charleston", "purple", false, 40);
        Route route41 = new Route(false, 6, "Miami", "New Orleans", "red", false, 41);
        Route route42 = new Route(false, 3, "New Orleans", "Little Rock", "green", false, 42);
        Route route43 = new Route(false, 2, "New Orleans", "Houston", "blank", false, 43);
        Route route44 = new Route(false, 6, "Houston", "El Paso", "green", false, 44);
        Route route45 = new Route(false, 1, "Houston", "Dallas", "blank", true, 45, 46);
        Route route46 = new Route(false, 1, "Houston", "Dallas", "blank", true, 46, 45);
        Route route47 = new Route(false, 2, "Dallas", "Little Rock", "blank", false, 47);
        Route route48 = new Route(false, 3, "Little Rock", "Nashville", "white", false, 48);
        Route route49 = new Route(false, 2, "Little Rock", "Saint Louis", "blank", false, 49);
        Route route50 = new Route(false, 2, "Little Rock", "Oklahoma City", "blank", false, 50);
        Route route51 = new Route(false, 2, "Saint Louis", "Nashville", "blank", false, 51);
        Route route52 = new Route(false, 2, "Saint Louis", "Chicago", "green", true, 52, 53);
        Route route53 = new Route(false, 2, "Saint Louis", "Chicago", "white", true, 53, 52);
        Route route54 = new Route(false, 2, "Saint Louis", "Kansas City", "purple", true, 54, 55);
        Route route55 = new Route(false, 2, "Saint Louis", "Kansas City", "blue", true, 55, 54);
        Route route56 = new Route(false, 4, "Chicago", "Omaha", "blue", false, 56);
        Route route57 = new Route(false, 3, "Chicago", "Duluth", "red", false, 57);
        Route route58 = new Route(false, 2, "Toronto", "Sault Ste. Marie", "blank", false, 58);
        Route route59 = new Route(false, 6, "Toronto", "Duluth", "purple", false, 59);
        Route route60 = new Route(false, 3, "Sault Ste. Marie", "Duluth", "blank", false, 60);
        Route route61 = new Route(false, 4, "Duluth", "Winnipeg", "black", false, 61);
        Route route62 = new Route(false, 6, "Duluth", "Helena", "orange", false, 62);
        Route route63 = new Route(false, 2, "Duluth", "Omaha", "blank", true, 63, 64);
        Route route64 = new Route(false, 2, "Duluth", "Omaha", "blank", true, 64, 63);
        Route route65 = new Route(false, 5, "Omaha", "Helena", "red", false, 65);
        Route route66 = new Route(false, 4, "Omaha", "Denver", "purple", false, 66);
        Route route67 = new Route(false, 1, "Omaha", "Kansas City", "blank", true, 67, 68);
        Route route68 = new Route(false, 1, "Omaha", "Kansas City", "blank", true, 68, 67);
        Route route69 = new Route(false, 4, "Kansas City", "Denver", "black", true, 69, 70);
        Route route70 = new Route(false, 4, "Kansas City", "Denver", "orange", true, 70, 69);
        Route route71 = new Route(false, 2, "Kansas City", "Oklahoma City", "blank", true, 71, 72);
        Route route72 = new Route(false, 2, "Kansas City", "Oklahoma City", "blank", true, 72, 71);
        Route route73 = new Route(false, 4, "Oklahoma City", "Denver", "red", false, 73);
        Route route74 = new Route(false, 3, "Oklahoma City", "Santa Fe", "blue", false, 74);
        Route route75 = new Route(false, 5, "Oklahoma City", "El Paso", "yellow", false, 75);
        Route route76 = new Route(false, 2, "Oklahoma City", "Dallas", "blank", true, 76, 77);
        Route route77 = new Route(false, 2, "Oklahoma City", "Dallas", "blank", true, 77, 76);
        Route route78 = new Route(false, 4, "Dallas", "El Paso", "red", false, 78);
        Route route79 = new Route(false, 2, "El Paso", "Santa Fe", "blank", false, 79);
        Route route80 = new Route(false, 3, "El Paso", "Phoenix", "blank", false, 80);
        Route route81 = new Route(false, 6, "El Paso", "Los Angeles", "black", false, 81);
        Route route82 = new Route(false, 3, "Santa Fe", "Phoenix", "blank", false, 82);
        Route route83 = new Route(false, 2, "Santa Fe", "Denver", "blank", false, 83);
        Route route84 = new Route(false, 5, "Denver", "Phoenix", "white", false, 84);
        Route route85 = new Route(false, 3, "Denver", "Salt Lake City", "yellow", true, 85, 86);
        Route route86 = new Route(false, 3, "Denver", "Salt Lake City", "red", true, 86, 85);
        Route route87 = new Route(false, 4, "Denver", "Helena", "green", false, 87);
        Route route88 = new Route(false, 3, "Helena", "Salt Lake City", "purple", false, 88);
        Route route89 = new Route(false, 4, "Helena", "Winnipeg", "blue", false, 89);
        Route route90 = new Route(false, 4, "Helena", "Calgary", "blank", false, 90);
        Route route91 = new Route(false, 6, "Helena", "Seattle", "yellow", false, 91);
        Route route92 = new Route(false, 1, "Seattle", "Portland", "blank", true, 92, 93);
        Route route93 = new Route(false, 1, "Seattle", "Portland", "blank", true, 93, 92);
        Route route94 = new Route(false, 5, "Portland", "San Francisco", "green", true, 94, 95);
        Route route95 = new Route(false, 5, "Portland", "San Francisco", "purple", true, 95, 94);
        Route route96 = new Route(false, 6, "Portland", "Salt Lake City", "blue", false, 96);
        Route route97 = new Route(false, 3, "Salt Lake City", "Las Vegas", "orange", false, 97);
        Route route98 = new Route(false, 5, "Salt Lake City", "San Francisco", "orange", true, 98, 99);
        Route route99 = new Route(false, 5, "Salt Lake City", "San Francisco", "white", true, 99, 98);
        Route route100 = new Route(false, 3, "San Francisco", "Los Angeles", "yellow", true, 100, 101);
        Route route101 = new Route(false, 3, "San Francisco", "Los Angeles", "purple", true, 101, 100);
        Route route102 = new Route(false, 2, "Los Angeles", "Las Vegas", "blank", false, 102);
        Route route103 = new Route(false, 3, "Los Angeles", "Phoenix", "blank", false, 103);

        mRoutes.add(route1);    mRouteIDs.put(1, route1);
        mRoutes.add(route2);    mRouteIDs.put(2, route2);
        mRoutes.add(route3);    mRouteIDs.put(3, route3);
        mRoutes.add(route4);    mRouteIDs.put(4, route4);
        mRoutes.add(route5);    mRouteIDs.put(5, route5);
        mRoutes.add(route6);    mRouteIDs.put(6, route6);
        mRoutes.add(route7);    mRouteIDs.put(7, route7);
        mRoutes.add(route8);    mRouteIDs.put(8, route8);
        mRoutes.add(route9);    mRouteIDs.put(9, route9);
        mRoutes.add(route10);   mRouteIDs.put(10, route10);
        mRoutes.add(route11);   mRouteIDs.put(11, route11);
        mRoutes.add(route12);   mRouteIDs.put(12, route12);
        mRoutes.add(route13);   mRouteIDs.put(13, route13);
        mRoutes.add(route14);   mRouteIDs.put(14, route14);
        mRoutes.add(route15);   mRouteIDs.put(15, route15);
        mRoutes.add(route16);   mRouteIDs.put(16, route16);
        mRoutes.add(route17);   mRouteIDs.put(17, route17);
        mRoutes.add(route18);   mRouteIDs.put(18, route18);
        mRoutes.add(route19);   mRouteIDs.put(19, route19);
        mRoutes.add(route20);   mRouteIDs.put(20, route20);
        mRoutes.add(route21);   mRouteIDs.put(21, route21);
        mRoutes.add(route22);   mRouteIDs.put(22, route22);
        mRoutes.add(route23);   mRouteIDs.put(23, route23);
        mRoutes.add(route24);   mRouteIDs.put(24, route24);
        mRoutes.add(route25);   mRouteIDs.put(25, route25);
        mRoutes.add(route26);   mRouteIDs.put(26, route26);
        mRoutes.add(route27);   mRouteIDs.put(27, route27);
        mRoutes.add(route28);   mRouteIDs.put(28, route28);
        mRoutes.add(route29);   mRouteIDs.put(29, route29);
        mRoutes.add(route30);   mRouteIDs.put(30, route30);
        mRoutes.add(route31);   mRouteIDs.put(31, route31);
        mRoutes.add(route32);   mRouteIDs.put(32, route32);
        mRoutes.add(route33);   mRouteIDs.put(33, route33);
        mRoutes.add(route34);   mRouteIDs.put(34, route34);
        mRoutes.add(route35);   mRouteIDs.put(35, route35);
        mRoutes.add(route36);   mRouteIDs.put(36, route36);
        mRoutes.add(route37);   mRouteIDs.put(37, route37);
        mRoutes.add(route38);   mRouteIDs.put(38, route38);
        mRoutes.add(route39);   mRouteIDs.put(39, route39);
        mRoutes.add(route40);   mRouteIDs.put(40, route40);
        mRoutes.add(route41);   mRouteIDs.put(41, route41);
        mRoutes.add(route42);   mRouteIDs.put(42, route42);
        mRoutes.add(route43);   mRouteIDs.put(43, route43);
        mRoutes.add(route44);   mRouteIDs.put(44, route44);
        mRoutes.add(route45);   mRouteIDs.put(45, route45);
        mRoutes.add(route46);   mRouteIDs.put(46, route46);
        mRoutes.add(route47);   mRouteIDs.put(47, route47);
        mRoutes.add(route48);   mRouteIDs.put(48, route48);
        mRoutes.add(route49);   mRouteIDs.put(49, route49);
        mRoutes.add(route50);   mRouteIDs.put(50, route50);
        mRoutes.add(route51);   mRouteIDs.put(51, route51);
        mRoutes.add(route52);   mRouteIDs.put(52, route52);
        mRoutes.add(route53);   mRouteIDs.put(53, route53);
        mRoutes.add(route54);   mRouteIDs.put(54, route54);
        mRoutes.add(route55);   mRouteIDs.put(55, route55);
        mRoutes.add(route56);   mRouteIDs.put(56, route56);
        mRoutes.add(route57);   mRouteIDs.put(57, route57);
        mRoutes.add(route58);   mRouteIDs.put(58, route58);
        mRoutes.add(route59);   mRouteIDs.put(59, route59);
        mRoutes.add(route60);   mRouteIDs.put(60, route60);
        mRoutes.add(route61);   mRouteIDs.put(61, route61);
        mRoutes.add(route62);   mRouteIDs.put(62, route62);
        mRoutes.add(route63);   mRouteIDs.put(63, route63);
        mRoutes.add(route64);   mRouteIDs.put(64, route64);
        mRoutes.add(route65);   mRouteIDs.put(65, route65);
        mRoutes.add(route66);   mRouteIDs.put(66, route66);
        mRoutes.add(route67);   mRouteIDs.put(67, route67);
        mRoutes.add(route68);   mRouteIDs.put(68, route68);
        mRoutes.add(route69);   mRouteIDs.put(69, route69);
        mRoutes.add(route70);   mRouteIDs.put(70, route70);
        mRoutes.add(route71);   mRouteIDs.put(71, route71);
        mRoutes.add(route72);   mRouteIDs.put(72, route72);
        mRoutes.add(route73);   mRouteIDs.put(73, route73);
        mRoutes.add(route74);   mRouteIDs.put(74, route74);
        mRoutes.add(route75);   mRouteIDs.put(75, route75);
        mRoutes.add(route76);   mRouteIDs.put(76, route76);
        mRoutes.add(route77);   mRouteIDs.put(77, route77);
        mRoutes.add(route78);   mRouteIDs.put(78, route78);
        mRoutes.add(route79);   mRouteIDs.put(79, route79);
        mRoutes.add(route80);   mRouteIDs.put(80, route80);
        mRoutes.add(route81);   mRouteIDs.put(81, route81);
        mRoutes.add(route82);   mRouteIDs.put(82, route82);
        mRoutes.add(route83);   mRouteIDs.put(83, route83);
        mRoutes.add(route84);   mRouteIDs.put(84, route84);
        mRoutes.add(route85);   mRouteIDs.put(85, route85);
        mRoutes.add(route86);   mRouteIDs.put(86, route86);
        mRoutes.add(route87);   mRouteIDs.put(87, route87);
        mRoutes.add(route88);   mRouteIDs.put(88, route88);
        mRoutes.add(route89);   mRouteIDs.put(89, route89);
        mRoutes.add(route90);   mRouteIDs.put(90, route90);
        mRoutes.add(route91);   mRouteIDs.put(91, route91);
        mRoutes.add(route92);   mRouteIDs.put(92, route92);
        mRoutes.add(route93);   mRouteIDs.put(93, route93);
        mRoutes.add(route94);   mRouteIDs.put(94, route94);
        mRoutes.add(route95);   mRouteIDs.put(95, route95);
        mRoutes.add(route96);   mRouteIDs.put(96, route96);
        mRoutes.add(route97);   mRouteIDs.put(97, route97);
        mRoutes.add(route98);   mRouteIDs.put(98, route98);
        mRoutes.add(route99);   mRouteIDs.put(99, route99);
        mRoutes.add(route100);   mRouteIDs.put(100, route100);
        mRoutes.add(route101);   mRouteIDs.put(101, route101);
        mRoutes.add(route102);   mRouteIDs.put(102, route102);
        mRoutes.add(route103);   mRouteIDs.put(103, route103);
    }
}
