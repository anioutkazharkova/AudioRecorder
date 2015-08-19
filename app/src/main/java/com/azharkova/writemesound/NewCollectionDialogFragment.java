package com.azharkova.writemesound;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

/**
 * Created by azharkova on 31.07.2015.
 */
public class NewCollectionDialogFragment extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.new_collection_dialog_layout, container, false);
        final EditText etName = (EditText) view.findViewById(R.id.etName);
        TextView btnOk, btnCancel;
        btnOk = (TextView) view.findViewById(R.id.btnOk);
        btnCancel = (TextView) view.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCollection(etName.getText().toString());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        getDialog().setTitle("New collection");
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
    }

    private void createCollection(String name) {
        File folder = new File(Utility.ROOT_FOLDER + "/" + name);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
            Intent intent = new Intent(getActivity(), CollectionRecordsActivity.class);
            intent.putExtra("path", String.format("%s/%s", Utility.ROOT_FOLDER, name));
            startActivity(intent);
            dismiss();
        } else {
            //Already exists
        }
    }


}
