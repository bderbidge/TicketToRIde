package com.example.brandonderbidge.myapplication.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.brandonderbidge.myapplication.R;

/**
 * Created by emilyprigmore on 10/27/17.
 */

public class RouteConfirmationDialogFragment extends DialogFragment {

    NoticeDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        builder.setTitle(bundle.getString("message"));

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // user clicked the correct route
                mListener.onDialogPositiveClick(RouteConfirmationDialogFragment.this);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // not the correct route
                mListener.onDialogNegativeClick(RouteConfirmationDialogFragment.this);
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    /* MapActivity must implement this interface in order to receive callbacks from the dialog */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(RouteConfirmationDialogFragment dialog);
        void onDialogNegativeClick(RouteConfirmationDialogFragment dialog);
    }
}
