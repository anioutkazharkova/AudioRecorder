package com.azharkova.writemesound.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.azharkova.writemesound.data.CollectionEntity;
import com.azharkova.writemesound.data.FileManager;
import com.azharkova.writemesound.IRefreshable;
import com.azharkova.writemesound.R;
import com.azharkova.writemesound.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by azharkova on 03.08.2015.
 */
public class MoveFileDialogFragment extends DialogFragment {

    List<String> collections=new ArrayList<String>();
    List<CollectionEntity> collectionEntities=new ArrayList<CollectionEntity>();
    private ArrayList<File> folders;
    private String fileUrl;
    IRefreshable refreshableCallback;

    int selectedItem=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.move_file);
        View view=inflater.inflate(R.layout.move_file_dialog_fragment,container,false);

getAllCollections();

        Spinner spinner=(Spinner)view.findViewById(R.id.spCollections);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.collections_list_row, R.id.tvCollectionName,collections);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        TextView btnOk, btnCancel;
        btnOk = (TextView) view.findViewById(R.id.btnOk);
        btnCancel = (TextView) view.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    move(selectedItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    private void getAllCollections()
    {
        collectionEntities=new ArrayList<CollectionEntity>();
        collections=new ArrayList<String>();
        listAllFolders(Utility.ROOT_FOLDER);
        CollectionEntity c=new CollectionEntity();
        c.setName("Main");
        collections.add("Main");
        c.setPath(Utility.ROOT_FOLDER);
        collectionEntities.add(c);
        for(File f:folders)
        {
            c=new CollectionEntity();

            c.setName(f.getAbsolutePath().substring(Utility.ROOT_FOLDER.length()+1));
            collections.add(c.getName());
            c.setPath(f.getAbsolutePath());
            collectionEntities.add(c);
        }


    }
    private void listAllFolders(String directoryName)
    {
        File directory = new File(directoryName);
        folders=new ArrayList<File>();

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isDirectory()) {
                folders.add(file);

            }
        }
    }
    public void setUrl(String url) {
        this.fileUrl = url;
        //tvCurrentName.setText(getOldName());
    }



    public void move(int position) throws FileNotFoundException,IOException
    {

        String collection="";

        if (position==0)
        {
            collection=Utility.ROOT_FOLDER;

        }
        else
        {
            collection=Utility.ROOT_FOLDER+"/"+collections.get(position);
        }
        FileManager.getInstance().move(fileUrl,collection);

       /* String oldUrl=fileUrl.substring(0, fileUrl.lastIndexOf("/"));
        String newUrl="";

        if (!newUrl.equals(oldUrl)) {
            String newFileUrl = newUrl + "/" + getOldName();
            File newFile = new File(newFileUrl);
            File oldFile = new File(fileUrl);

            if (!newFile.exists()) {
                FileInputStream in = new FileInputStream(oldFile);
                FileOutputStream out = new FileOutputStream(newFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file
                out.flush();
                out.close();
                out = null;

                oldFile.delete();
            }
        }*/
            if (refreshableCallback!=null)
            {
                refreshableCallback.refresh();
            }


    }

    public void setRefreshableCallback(IRefreshable refreshableCallback) {
        this.refreshableCallback = refreshableCallback;
    }
}
