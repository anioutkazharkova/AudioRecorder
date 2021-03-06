package com.azharkova.writemesound;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

/**
 * Created by azharkova on 03.08.2015.
 */
public class RenameFileDialog extends DialogFragment {

    private String fileUrl;
    TextView tvCurrentName;
    private IRefreshable refreshableCallback;

    public RenameFileDialog() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rename_file_dialog, container, false);
        final EditText etName = (EditText) view.findViewById(R.id.etName);
        TextView btnOk, btnCancel;
        tvCurrentName = (TextView) view.findViewById(R.id.tvCurrentName);
        if (!fileUrl.equals("")) {
            tvCurrentName.setText(FileManager.getInstance().getOldName(fileUrl));
        }
        btnOk = (TextView) view.findViewById(R.id.btnOk);
        btnCancel = (TextView) view.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newName = etName.getText().toString();
                rename(newName);

                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        getDialog().setTitle(R.string.new_name);
        return view;

    }

    private void rename(String newName) {

        FileManager.getInstance().renameFile(fileUrl,newName);
/*
        String oldName = getOldName();


        File oldFile = new File(fileUrl);
        String newUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/") + 1) + newName + fileUrl.substring(fileUrl.lastIndexOf("."));
        File newFile = new File(newUrl);
        if (!newUrl.equals(fileUrl) && !newUrl.equals("")) {
            if (newFile.exists()) {
                int index = 0;
                do {
                    index++;
                    newUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/") + 1) + newName + index + fileUrl.substring(fileUrl.lastIndexOf("."));
                    newFile = new File(newUrl);
                } while (newFile.exists());
            }

            Log.i("WRITE_ME_SOUND", newUrl);


            oldFile.renameTo(newFile);
        }*/
            if (refreshableCallback != null)
                refreshableCallback.refresh();


    }

    public void setUrl(String url) {
        this.fileUrl = url;
        //tvCurrentName.setText(getOldName());
    }

   /* private String getOldName() {

        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }*/

    public void setCallback(IRefreshable callback) {
        this.refreshableCallback = callback;
    }
}
