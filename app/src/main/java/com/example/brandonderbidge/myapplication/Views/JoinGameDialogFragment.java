package com.example.brandonderbidge.myapplication.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.brandonderbidge.myapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Communication.Game;
import Communication.GameState;
import Communication.Player;

/**
 * Created by pbstr on 10/9/2017.
 */


/**
 * the dialog for joining a game in the GameSelectionActivity
 */
public class JoinGameDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_SELECTED_COLORS = "com.example.brandonderbidge.myapplication.selected_colors";
    List<String> mAlreadySelectedColors = new ArrayList<>(); //initialize at the beginning to not get null pointer exceptions
    String selectedColor; //the color selected by the spinner
    Spinner mColorSpinner; //displays available colors
    NoticeDialogListener mListener; //used to be overwritten by the activity so the activity can get the info from this dialog

    public static JoinGameDialogFragment newInstance(Game game) {
        Bundle args = new Bundle();
        List<String> colors = new ArrayList<String>();
        //sometimes game is null (just by timing by the poller or if someone clicks join too quickly
        if (game != null) {
            for (Map.Entry<String, Player> entry : game.getCurrentGameState().getCurrentPlayers().entrySet()) {
                colors.add(entry.getValue().getColor());
            }
        }
        JoinGameDialogFragment fragment = new JoinGameDialogFragment();
        fragment.setArguments(args);
        fragment.setAlreadySelectedColors(colors);

        return fragment;
    }

    private void setAlreadySelectedColors(List<String> colors) {
       this.mAlreadySelectedColors = colors;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_join_game, null);

        mColorSpinner = (Spinner) dialogView.findViewById(R.id.select_color_spinner);
        mColorSpinner.setOnItemSelectedListener(this);

        //creates the spinner to use all the colors in the player_colors_array in the strings resource
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.player_colors_array, android.R.layout.simple_spinner_dropdown_item);

        //get all the colors the players can have to be given to the adapter to show the user the colors
        CharSequence[] colorArray = getResources().getStringArray(R.array.player_colors_array);
        List<CharSequence> colors = new ArrayList<CharSequence>();
        for(CharSequence availableColor : colorArray) {
            colors.add(availableColor);
        }
        //remove already selected colors from the list so that the user only sees the available colors
        for(String color : mAlreadySelectedColors) {
            CharSequence charColor = (CharSequence) color;
            colors.remove(charColor);
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, colors);

        mColorSpinner.setAdapter(adapter);
        builder.setView(dialogView);

        //ok button
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //User clicked ok button, sends info to Activity that implements the listener
                mListener.onDialogPositiveClick(JoinGameDialogFragment.this);
            }
        });

        //cancel button
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog, sends info to Activity that implements the listener
                mListener.onDialogNegativeClick(JoinGameDialogFragment.this);
            }
        });

        builder.setTitle("Join Game");

        return builder.create();

    }

    //these two methods are for the spinner so I can get the right number of players
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        selectedColor = (String) parent.getItemAtPosition(pos);
    }
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    //used in GameSelectionActivity to get the color selected
    public String getSelectedColor() {
        return selectedColor;
    }

    //this is so that I can handle the events of the dialog in the GameSelectionActivity
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(JoinGameDialogFragment dialog);
        public void onDialogNegativeClick(JoinGameDialogFragment dialog);
    }
}
