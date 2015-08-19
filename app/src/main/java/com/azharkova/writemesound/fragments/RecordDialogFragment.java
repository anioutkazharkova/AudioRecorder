package com.azharkova.writemesound.fragments;

import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azharkova.writemesound.IRefreshable;
import com.azharkova.writemesound.R;

/**
 * Created by azharkova on 03.08.2015.
 */
public class RecordDialogFragment extends DialogFragment {

    private IRefreshable refreshableCallback;

    public RecordDialogFragment(){}


    String fileUrl="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(R.string.select_action);
        View view=inflater.inflate(R.layout.record_dialog_fragment,container,false);
        LinearLayout layout= (LinearLayout) view.findViewById(R.id.layout);
        for(int i=0;i<3;i++)
        {
           final View itemView= inflateItem(i,inflater);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((String)itemView.getTag()).equals("Rename"))
                    {

                        if (!fileUrl.equals(""))
                        {
                            RenameFileDialog fileDialog=new RenameFileDialog();
                            fileDialog.setUrl(fileUrl);
                            fileDialog.setCallback(refreshableCallback);
                            fileDialog.show(getActivity().getFragmentManager(),"rename_dialog");

                        }
                    }
                    else
                    if (((String)itemView.getTag()).equals("Delete"))
                    {
                        RemoveFileDialogFragment fileDialog=new RemoveFileDialogFragment();
                        fileDialog.setUrl(fileUrl);
                        fileDialog.setRefreshableCallback(refreshableCallback);
                        fileDialog.show(getActivity().getFragmentManager(),"remove_dialog");
                    }
                    else
                    if (((String)itemView.getTag()).equals("Move"))
                    {
                        MoveFileDialogFragment fileDialog=new MoveFileDialogFragment();
                        fileDialog.setUrl(fileUrl);
                        fileDialog.setRefreshableCallback(refreshableCallback);
                        fileDialog.show(getActivity().getFragmentManager(),"move_dialog");
                    }
                    //action
                    dismiss();
                }
            });
            layout.addView(itemView);
        }
        return view;
    }

    private View inflateItem(int i, LayoutInflater inflater) {
        View itemView=inflater.inflate(R.layout.record_dialog_item,null);
        TextView tvName= (TextView)itemView.findViewById(R.id.tvItemName);
        ImageView imageIcon=(ImageView)itemView.findViewById(R.id.imItemIcon);
        switch (i)
        {
            case 0:
                tvName.setText(R.string.rename);
                imageIcon.setImageResource(R.drawable.ic_mode_edit_black_24dp);
                itemView.setTag("Rename");
                break;
            case 1:
                tvName.setText(R.string.move);
                imageIcon.setImageResource(R.drawable.ic_keyboard_tab_black_24dp);
                itemView.setTag("Move");
                break;
            case 2:
                tvName.setText(R.string.delete);
                imageIcon.setImageResource(R.drawable.ic_clear_black_24dp);
                itemView.setTag("Delete");
                break;

        }
        return itemView;
    }
    public void setUrl(String url) {
        this.fileUrl = url;
Log.i("WRITE_ME_SOUND",fileUrl);
        //Toast.makeText(getActivity(),fileUrl,Toast.LENGTH_LONG).show();
    }
    public void setCallback(IRefreshable callback)
    {
        this.refreshableCallback=callback;
    }

}
