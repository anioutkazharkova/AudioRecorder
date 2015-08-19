package com.azharkova.writemesound.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
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

import com.azharkova.writemesound.IRecordCallback;
import com.azharkova.writemesound.IRefreshable;
import com.azharkova.writemesound.activities.MainActivity;
import com.azharkova.writemesound.activities.PlayRecordActivity;
import com.azharkova.writemesound.R;
import com.azharkova.writemesound.activities.RecordActivity;
import com.azharkova.writemesound.data.RecordData;
import com.azharkova.writemesound.adapters.RecordsAdapter;
import com.azharkova.writemesound.activities.SortingActivity;
import com.azharkova.writemesound.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AllRecordsFragment extends Fragment implements IRecordCallback,IRefreshable {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SharedPreferences preferences;
    int sortOrder=0;

    public AllRecordsFragment()
    {

    }
    RecordDialogFragment dialogFragment=new RecordDialogFragment();
    List<RecordData> records;
    List<File> files=new ArrayList<File>();
    ListView listView;
    RecordsAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_records,container, false);
        listView=(ListView)view.findViewById(R.id.lvRecords);
         records=new ArrayList<RecordData>();
preferences=getActivity().getSharedPreferences(Utility.PREFERENCES, Context.MODE_PRIVATE);
        loadPreferences();
       final IRefreshable callback=this;
     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           dialogFragment.setUrl(((RecordData)mAdapter.getItem(i)).getUrl());
             dialogFragment.setCallback(callback);
           dialogFragment.show(getActivity().getFragmentManager(),"dialog");

         }
     });

        mAdapter=new RecordsAdapter(this.getActivity(),records);

        listView.setAdapter(mAdapter);
        setHasOptionsMenu(true);

        getAllRecords();
       // Toast.makeText(this.getActivity(),mAdapter.getCount()+"",Toast.LENGTH_LONG).show();
        return view;
}
    private void loadPreferences() {
        sortOrder = (preferences.getInt(Utility.SORT_PREFERENCES, 0));
    }
    @Override
    public void onResume() {
        super.onResume();
        getAllRecords();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.all_records_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.itemRecord:
                Intent newRecordActivity=new Intent(getActivity(), RecordActivity.class);
                newRecordActivity.putExtra("path",Utility.ROOT_FOLDER);
                startActivity(newRecordActivity);
                break;
            case R.id.itemSort:
                startActivityForResult(new Intent(getActivity(),SortingActivity.class),24567);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAllRecords()
    {
        records=new ArrayList<RecordData>();
        files=new ArrayList<File>();

        listAllFiles(Utility.ROOT_FOLDER);
        switch (sortOrder) {
            case 0:  Collections.sort(files, new NameComparator());
                break;
            case 1:Collections.sort(files, new DateComparator());
                break;
            case 2:Collections.sort(files, new CollectionComparator());
                break;
        }
        for(File f:files)
        {
            RecordData recordData=getRecordInfo(f);
            records.add(recordData);
        }
        mAdapter.setRecords(records);
        mAdapter.setRecordCallback(this);
    }
    public class NameComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {

                return getName(o1.getName()).compareTo(getName(o2.getName()));

                }
    }
    public class DateComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {

                return Long.valueOf(o1.lastModified()).compareTo(o2.lastModified());

        }
    }

    public class CollectionComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {

            return getCollection(o1.getParent()).compareTo(getCollection(o2.getParent()));

        }
    }

    private String getCollection(String path)
    {
        if (path.equals(Utility.ROOT_FOLDER))
        {
            return "Main";
        }
        else
            return
            path.substring(path.lastIndexOf("/")+1);
    }
    private String getName(String path)
    {
        return path.substring(0,path.indexOf("."));
    }
    private  RecordData getRecordInfo(File f)
    {
        RecordData recordData=new RecordData();
        recordData.setName(f.getName());

        recordData.setUrl(f.getAbsolutePath());
        String parent=f.getParent();
        if (parent.equals(Utility.ROOT_FOLDER))
        {
            recordData.setCollection("Main");
        }
        else
        {
            recordData.setCollection(parent.substring(Utility.ROOT_FOLDER.length()+1));
        }
        recordData.setDate(f.lastModified());
        recordData.setDuration(getDuration(f.getAbsolutePath()));

        return recordData;
    }

    private int getDuration(String path)
    {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        return Integer.parseInt(duration);
    }

    private void listAllFiles(String directoryName) {
        File directory = new File(directoryName);


        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listAllFiles(file.getAbsolutePath());
            }
        }
    }


    @Override
    public void playRecord(int position) {
        Intent playIntent=new Intent(getActivity(),PlayRecordActivity.class);
        playIntent.putExtra("source",((RecordData)mAdapter.getItem(position)).getUrl());
        startActivity(playIntent);
    }

    @Override
    public void refresh() {
        getAllRecords();
    }
}
