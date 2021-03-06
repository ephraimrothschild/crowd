package com.itc.crowd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;

import com.itc.crowd.view.PlaylistActivity;

public class CreatePlaylistDialogFragment extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
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
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_create_playlist, null))
                // Add action buttons
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(getActivity(), PlaylistActivity.class);
                         getActivity().startActivity(myIntent);
//                        mListener.onDialogPositiveClick(CreatePlaylistDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(CreatePlaylistDialogFragment.this);
                    }
                });
        AlertDialog dialog =  builder.create();
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 900;
        dialog.getWindow().setAttributes(lp);

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if(positiveButton != null) {
            positiveButton.setBackgroundColor(getResources().getColor(R.color.create_button_backgroundColor));
            positiveButton.setTextColor(getResources().getColor(R.color.create_button_textColor));
        }

        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if(negativeButton != null) {
            negativeButton.setTextColor(getResources().getColor(R.color.apptheme_color));
        }

        return dialog;
    }

}
