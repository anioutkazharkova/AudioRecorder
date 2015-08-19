package com.azharkova.writemesound.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.azharkova.writemesound.R;

/**
 * Created by aniou_000 on 09.08.2015.
 */
public class AlreadySavedDialogFragment extends DialogFragment {

    public AlreadySavedDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.file_already_saved);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        builder.setTitle(R.string.attention);
        return builder.create();
    }
}
