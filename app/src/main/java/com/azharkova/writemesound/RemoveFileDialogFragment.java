package com.azharkova.writemesound;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.io.File;

/**
 * Created by azharkova on 03.08.2015.
 */
public class RemoveFileDialogFragment extends DialogFragment {

    private String fileUrl;
    private IRefreshable refreshableCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.remove_file);
        builder.setMessage(R.string.confirm_remove);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (!fileUrl.equals(""))
                    removeFile();
                dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }

    public void setUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    private void removeFile() {
       FileManager.getInstance().deleteFile(fileUrl);
        if (refreshableCallback != null)
            refreshableCallback.refresh();
    }

    public void setRefreshableCallback(IRefreshable callback) {
        this.refreshableCallback = callback;
    }
}
