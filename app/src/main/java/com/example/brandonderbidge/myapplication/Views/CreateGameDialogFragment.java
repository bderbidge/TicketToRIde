package com.example.brandonderbidge.myapplication.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbstr on 10/9/2017.
 */

public class CreateGameDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    //number of players set by the spinner
    String currentNumPlayers;

    //edit box for the game name
    EditText mGameNameEdit;
    //spinner for selecting the number of players
    Spinner mNumberPlayersSpinner;

    //event listener for spinner, will be overloaded by the GameSelectionActivity to get the info from the dialog to the presenter
    NoticeDialogListener mListener;

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

        View dialogView = inflater.inflate(R.layout.dialog_create_game, null);

        mGameNameEdit = (EditText) dialogView.findViewById(R.id.new_game_edit);
        mNumberPlayersSpinner = (Spinner) dialogView.findViewById(R.id.number_players_spinner);
        mNumberPlayersSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.player_numbers_array, android.R.layout.simple_spinner_dropdown_item);
        mNumberPlayersSpinner.setAdapter(adapter);
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //User clicked ok button
                mListener.onDialogPositiveClick(CreateGameDialogFragment.this);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                mListener.onDialogNegativeClick(CreateGameDialogFragment.this);
            }
        });

        builder.setTitle("Create a New Game");

        return builder.create();

    }

    //these two methods are for the spinner so I can get the right number of players from the spinner
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        currentNumPlayers = (String) parent.getItemAtPosition(pos);
    }
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    public String getCurrentNumPlayers() {
        return currentNumPlayers;
    }

    //this is so that I can handle the events of the dialog in the GameSelectionActivity and get the info from the dialog to pass to the presenter
    //this will be implemented in the GameSelectionActivity
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(CreateGameDialogFragment dialog);
        public void onDialogNegativeClick(CreateGameDialogFragment dialog);
    }
}
