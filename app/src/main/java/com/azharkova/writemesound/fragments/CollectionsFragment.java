package com.azharkova.writemesound.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.azharkova.writemesound.data.CollectionEntity;
import com.azharkova.writemesound.activities.CollectionRecordsActivity;
import com.azharkova.writemesound.adapters.CollectionsAdapter;
import com.azharkova.writemesound.activities.MainActivity;
import com.azharkova.writemesound.R;
import com.azharkova.writemesound.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CollectionsFragment extends Fragment {

    private ListView listView;
    private List<CollectionEntity> collections;
    private CollectionsAdapter mAdapter;
    List<File> files=new ArrayList<File>();
    List<File> folders=new ArrayList<File>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_collections,container,false);
        listView=(ListView)view.findViewById(R.id.lvCollections);
        collections=new ArrayList<CollectionEntity>();
setHasOptionsMenu(true);
       /* for(int i=0;i<10;i++)
            collections.add(new CollectionEntity());*/
        mAdapter=new CollectionsAdapter(getActivity(),collections);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent recordsIntent=new Intent(getActivity(), CollectionRecordsActivity.class);
                recordsIntent.putExtra("path", ((CollectionEntity) mAdapter.getItem(i)).getPath());
                startActivity(recordsIntent);
            }
        });
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllCollections();
    }

    private void getAllCollections()
    {
        collections=new ArrayList<CollectionEntity>();
        listAllFolders(Utility.ROOT_FOLDER);
        CollectionEntity c=new CollectionEntity();
        c.setName("Main");
        c.setPath(Utility.ROOT_FOLDER);
        collections.add(c);
        for(File f:folders)
        {
            c=new CollectionEntity();
            c.setName(f.getAbsolutePath().substring(Utility.ROOT_FOLDER.length()+1));
            c.setPath(f.getAbsolutePath());
            collections.add(c);
        }
        for (CollectionEntity col:collections)
        {
            listAllFilesInDirectory(col.getPath());
            col.setRecordsCount(files.size());
        }
        mAdapter.setCollections(collections);
    }

    private void listAllFilesInDirectory(String directoryName) {
        File directory = new File(directoryName);
 files=new ArrayList<File>();

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            }
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
                listAllFilesInDirectory(file.getAbsolutePath());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collections_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itemCollection:
                NewCollectionDialogFragment dialogFragment=new NewCollectionDialogFragment();
                //dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
                dialogFragment.show(this.getActivity().getFragmentManager(),"dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
