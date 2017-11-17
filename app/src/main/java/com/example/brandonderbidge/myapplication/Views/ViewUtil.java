package com.example.brandonderbidge.myapplication.Views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.brandonderbidge.myapplication.R;

/**
 * Created by pbstr on 10/16/2017.
 */

/**
 * This class will have methods common to all views to reduce code duplication.
 */
public class ViewUtil {

    public static void displayToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static int getPlayerColor(Context context, String playerColor) {
        //gets the color based on the 5 values we have for color
        int color;

        switch (playerColor.toLowerCase()) {
            case "red":
                color = ContextCompat.getColor(context, R.color.red);
                break;
            case "blue":
                color = ContextCompat.getColor(context, R.color.blue);
                break;
            case "yellow":
                color = ContextCompat.getColor(context, R.color.yellow);
                break;
            case "green":
                color = ContextCompat.getColor(context, R.color.green);
                break;
            case "black":
                color = ContextCompat.getColor(context, R.color.black);
                break;
            default:
                color = ContextCompat.getColor(context, R.color.fuchsia);
        }
        return color;
    }

    public static int getColor(Context context, String c) {
        String color = c.toLowerCase();
        switch (color) {
            case "red":
                return Color.RED;
            case "orange":
                return ContextCompat.getColor(context, R.color.orange);
            case "yellow":
                return Color.YELLOW;
            case "green":
                return Color.GREEN;
            case "blue":
                return Color.BLUE;
            case "purple":
                return ContextCompat.getColor(context, R.color.purple);
            case "black":
                return Color.BLACK;
            case "white":
                return Color.WHITE;
            default:
                return Color.GRAY;
        }
    }

    public static int getTrainColor(Context context, String c) {
        String color = c.toLowerCase();
        int colorToRet;
        switch(c) {
            case "red":
                return R.color.red;
            case "orange":
                return R.color.orange;
            case "yellow":
                return R.color.yellow;
            case "green":
                return R.color.green;
            case "blue":
                return R.color.blue;
            case "purple":
                return R.color.purple;
            case "black":
                return R.color.black;
            case "white":
                return R.color.white;
            default:
                return R.color.aqua;
        }
    }
}
