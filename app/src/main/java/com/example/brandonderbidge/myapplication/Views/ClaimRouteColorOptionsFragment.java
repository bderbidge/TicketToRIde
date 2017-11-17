package com.example.brandonderbidge.myapplication.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IMapTempPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Communication.Route;

/**
 * Created by emilyprigmore on 11/13/17.
 */

public class ClaimRouteColorOptionsFragment extends DialogFragment {

    private static IMapTempPresenter mPresenter;
    private static String[] colorOptions;
    private static Map<Integer, String> colorOptionOrder;
    private static List<Integer> selectedColor;
    private static Route clickedRoute;

    public static ClaimRouteColorOptionsFragment newInstance(List<String> colors, Route route,
                                                             IMapTempPresenter presenter) {
        ClaimRouteColorOptionsFragment fragment = new ClaimRouteColorOptionsFragment();
        mPresenter = presenter;
        setColorOptions(colors);
        selectedColor = new ArrayList<>();
        selectedColor.add(0);
        setColorOptionOrder(colors);
        clickedRoute = route;
        return fragment;
    }

    private static void setColorOptionOrder(List<String> colors) {
        colorOptionOrder = new HashMap<>();
        for(int i = 0; i < colors.size(); i++) {
            colorOptionOrder.put(i, colors.get(i));
        }
    }

    private static void setColorOptions(List<String> colors) {
        colorOptions = new String[colors.size()];
        for(int i = 0; i < colors.size(); i++) {
            colorOptions[i] = colors.get(i);
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Which color card would you like to use? Your wilds will be used automatically.")
                .setSingleChoiceItems(colorOptions, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedColor.size() == 1) {
                            selectedColor.remove(0);
                            selectedColor.add(which);
                        }
                        else
                            selectedColor.add(which);
                    }
                })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //hands to presenter for processing
                        mPresenter.claimRoute(clickedRoute, colorOptionOrder.get(selectedColor.get(0)));
                        mPresenter.endMyTurn();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        return dialog;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof ClaimRouteColorOptionsFragment.ClaimRouteColorOptionsCloseListener)
            ((ClaimRouteColorOptionsFragment.ClaimRouteColorOptionsCloseListener)activity)
                    .handleClaimRouteColorOptionsClose(dialog);
    }

    public interface ClaimRouteColorOptionsCloseListener {
        void handleClaimRouteColorOptionsClose(DialogInterface dialog);
    }
}
