package com.azharkova.writemesound.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.azharkova.writemesound.data.FileManager;
import com.azharkova.writemesound.IRefreshable;
import com.azharkova.writemesound.R;
import com.azharkova.writemesound.Utility;

/**
 * Created by aniou_000 on 03.08.2015.
 */
public class SaveFileDialogFragment extends DialogFragment {

String collection;
    private String extension="3gp";
    private String fileUrl;
    private IRefreshable refreshableCallback;
    private SharedPreferences preferences;
    int format= MediaRecorder.OutputFormat.THREE_GPP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.save_file_dialog_fragmen,container,false);
       final EditText etName=(EditText)view.findViewById(R.id.etName);
        TextView tvCollectionName=(TextView)view.findViewById(R.id.tvCollectionName);
        TextView btnOk, btnCancel;
        btnOk = (TextView) view.findViewById(R.id.btnOk);
        btnCancel = (TextView) view.findViewById(R.id.btnCancel);
        preferences = getActivity().getSharedPreferences(Utility.PREFERENCES, Context.MODE_PRIVATE);
        loadPreferences();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=etName.getText().toString();
                saveRecord(name);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        getDialog().setTitle(R.string.save_as);

        return view;
    }
    private  void loadPreferences() {
        //preferences = getSharedPreferences(Utility.PREFERENCES, Context.MODE_PRIVATE);

        format = preferences.getInt(Utility.AUDIO_PREFERENCES_VALUE, format);
        if (format==MediaRecorder.OutputFormat.THREE_GPP) {

            extension="3gp";
        }
        if (format==MediaRecorder.OutputFormat.MPEG_4) {

            extension="mp4";
        }
        if (format==MediaRecorder.OutputFormat.AMR_NB) {
            extension="amr";
        }
       // Toast.makeText(getActivity(), extension, Toast.LENGTH_LONG).show();


    }
    public void setCollection(String url)
    {
        this.collection=url;
    }
    public void setExtension(String extension)
    {
        this.extension=extension;
    }

    private void saveRecord(String name)
    {

        rename(name);
    }

    private void rename(String newName) {
        FileManager.getInstance().renameFile(fileUrl,newName);

       /* String oldName =FileManager.getInstance().getOldName(fileUrl);


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


    public void setRefreshableCallback(IRefreshable refreshableCallback) {
        this.refreshableCallback = refreshableCallback;
    }
    public void setUrl(String url) {
        this.fileUrl = url;
        //tvCurrentName.setText(getOldName());
    }
/*
    private String getOldName() {

        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }*/
}
