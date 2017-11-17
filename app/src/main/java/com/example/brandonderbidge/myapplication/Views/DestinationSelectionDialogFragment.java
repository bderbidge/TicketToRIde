package com.example.brandonderbidge.myapplication.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.brandonderbidge.myapplication.Presenters.IPresenters.IDestinationSelectionPresenter;
import com.example.brandonderbidge.myapplication.R;
import com.example.brandonderbidge.myapplication.Views.IViews.IDestinationSelectionView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import Communication.DestinationCard;
import Dagger.MyApplication;

/**
 * Created by pbstr on 10/28/2017.
 */

public class DestinationSelectionDialogFragment extends DialogFragment implements IDestinationSelectionView {


    private static final String ARG_MINNUMCARDS = "com.example.brandonderbidge.myapplication.minnumcards";

    @Inject
    IDestinationSelectionPresenter mPresenter;

    public static DestinationSelectionDialogFragment newInstance(int minNumCards) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MINNUMCARDS, minNumCards);
        DestinationSelectionDialogFragment fragment = new DestinationSelectionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    List<DestinationCard> mGivenCards = new ArrayList<>();
    final List<Integer> selectedItems = new ArrayList<>();
    int minNumCards;
    //event listener for spinner, will be overloaded by the GameSelectionActivity to get the info from the dialog to the presenter

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    /*
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
    */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        minNumCards = (int) getArguments().getSerializable(ARG_MINNUMCARDS);

        MyApplication.instance().setIDestinationSelectionView(this);
        (MyApplication.instance().getBasicInjectionComponent()).inject(this);

        String[] mDestinationCardNames = new String[mGivenCards.size()];
        for( int i = 0; i < mGivenCards.size(); i++) {
            mDestinationCardNames[i] = mGivenCards.get(i).getCity1() + " to " + mGivenCards.get(i).getCity2() + " -- " + mGivenCards.get(i).getPoints() + "  points";
        }

        builder.setTitle(R.string.title_select_destination_cards).setMultiChoiceItems(mDestinationCardNames, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if (isChecked) {
                        selectedItems.add(which);
                    //If the user checked the item, add  it to the selected items
                } else if (selectedItems.contains(which)) {
                    selectedItems.remove(Integer.valueOf(which));
                }
                //enable/disable the dialogbutton if the minimum cards are selected
                if (selectedItems.size() >= minNumCards) {
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
                else{
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        })

        // Set the action buttons
           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog

                //getting selected cards and not selected cards from the dialog
                List<DestinationCard> selectedCards = new ArrayList<DestinationCard>();
                List<DestinationCard> notSelectedCards = new ArrayList<DestinationCard>();
                for(int i = 0; i < mGivenCards.size(); i++) {
                    //adds the selected cards if it was selected in the dialog
                    if(selectedItems.contains(i)){
                        selectedCards.add(mGivenCards.get(i));
                    }
                    //adds to the not selected cards if it was not selected
                    else {
                        notSelectedCards.add(mGivenCards.get(i));
                    }
                }

                //hands to presenter for processing
                mPresenter.selectDestinationCards(selectedCards, notSelectedCards, minNumCards);
                mPresenter.onViewDestroy();
                }
            });
                /*
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //do nothing, I just want to close the dialog box
                }
            });
            */

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        return dialog;

    }

    public void updateDestinationCards(List<DestinationCard> availableCards) {
       this.mGivenCards = availableCards;
    }

    public void displayMessage(String msg) {
        ViewUtil.displayToast(getActivity(), msg);
    }


    //this is so that I can handle the events of the dialog in the GameSelectionActivity and get the info from the dialog to pass to the presenter
    //this will be implemented in the GameSelectionActivity
    /*
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(CreateGameDialogFragment dialog);
        public void onDialogNegativeClick(CreateGameDialogFragment dialog);
    }
    */

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof DestinationDialogCloseListener)
            ((DestinationDialogCloseListener)activity).handleDestinationDialogClose(dialog);
    }

    public interface DestinationDialogCloseListener {
        void handleDestinationDialogClose(DialogInterface dialog);
    }
}

