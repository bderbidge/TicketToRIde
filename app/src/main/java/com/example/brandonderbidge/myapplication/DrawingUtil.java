package com.example.brandonderbidge.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;

import Communication.Route;

/**
 * Created by emilyprigmore on 11/1/17.
 */

public class DrawingUtil {

    Context mContext;

    /**
     * This method will construct the Drawing Util.
     * @pre context != null
     * @post mContext != null
     *
     * @param context the application context this drawing util wil be used for
     */
    public DrawingUtil(Context context) {
        mContext = context;
    }

    /**
     * This method creates the graphics Path object to be drawn.
     * @pre p1, p2 != null
     * @post returning path != null
     *
     * @param p1 the first point in the line/path to be created
     * @param p2 the second point in the line/path to be created
     * @return the graphics Path object created by forming a line between the given points
     */
    public Path getPathToDraw(Point p1, Point p2) {
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        return path;
    }

    /**
     * This method sets the paint object for the class calling it. In this case, the only callers of
     * this method are route draw-ers so the paints returned here look like dashed lines with
     * varying widths.
     *
     * @pre route, color, point1, point2 != null
     * @post the returning paint object != null
     * @param route the route to be painted, if the route is claimed, the paint will be wider,
     *              shorter and vice versa
     * @param color the color the paint object should be set to
     * @param point1 the first point in the line - needed for getting line length
     * @param point2 the second point in the line - needed for getting line length
     * @return the graphics Paint object with the desired attributes
     */
    public Paint setPaint(Route route, String color, Point point1, Point point2) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getColor(color));

        double distance = computeLineLength(point1, point2);
        int routeLength = route.getLength();
        double segmentLength = distance / routeLength;
        double halfSegmentLength = segmentLength / 2;
        if(!route.isClaimed()) {
            paint.setStrokeWidth(4);
            paint.setPathEffect(new DashPathEffect(new float[]{(float) halfSegmentLength,
                    (float) halfSegmentLength}, 0));
        }
        else {
            paint.setStrokeWidth(10);
            paint.setPathEffect(new DashPathEffect(new float[] {(float) halfSegmentLength / 2,
                    (float) (halfSegmentLength * 3 / 2)}, (float) ( -1 * halfSegmentLength / 2)));
        }
        return paint;
    }

    /**
     * This method will compute the length of the line between the two given points.
     * @pre p1, p2 != null
     * @post the double return != null
     *
     * @param p1 the first point in the line
     * @param p2 the second and ending point in the line
     * @return the distance between p1 and p2
     */
    public double computeLineLength(Point p1, Point p2) {
        double x = Math.pow((p1.x - p2.x), 2);
        double y = Math.pow((p1.y - p2.y), 2);
        double z = Math.sqrt(x + y);
        return z;
    }

    /**
     * This method will return the Canvas-renderable color int. This method is why the context is
     * necessary.
     * @pre c != ""
     * @post the return will be a valid color, if no known color is given, gray is returned
     *
     * @param c the string of the color to be returned
     * @return the int value of the color searched for
     */
    public int getColor(String c) {
        String color = c.toLowerCase();
        switch (color) {
            case "red":
                return Color.RED;
            case "orange":
                return ContextCompat.getColor(mContext, R.color.orange);
            case "yellow":
                return Color.YELLOW;
            case "green":
                return Color.GREEN;
            case "blue":
                return Color.BLUE;
            case "purple":
                return ContextCompat.getColor(mContext, R.color.purple);
            case "black":
                return Color.BLACK;
            case "white":
                return Color.WHITE;
            default:
                return Color.GRAY;
        }
    }
}
