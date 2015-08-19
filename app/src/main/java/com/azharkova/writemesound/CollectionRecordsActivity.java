package com.azharkova.writemesound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CollectionRecordsActivity extends ActionBarActivity implements  IRecordCallback,IRefreshable{

    int sortOrder=0;
    private ListView listView;
private  RecordsAdapter mAdapter;
    private ArrayList<File> files;
    String path="";
    private ArrayList<RecordData> records;
    private RecordDialogFragment dialogFragment=new RecordDialogFragment();
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_records);

        path=getIntent().getStringExtra("path");
        preferences=getSharedPreferences(Utility.PREFERENCES, Context.MODE_PRIVATE);
        loadPreferences();

        listView=(ListView)findViewById(R.id.lvRecords);
        List<RecordData> records=new ArrayList<RecordData>();
        for(int i=0;i<10;i++)
        {
            RecordData r=new RecordData();
            records.add(r);
        }

        mAdapter=new RecordsAdapter(this,records);
        listView.setAdapter(mAdapter);
        final IRefreshable callback=this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogFragment.setUrl(((RecordData) mAdapter.getItem(i)).getUrl());
                dialogFragment.setCallback(callback);
                dialogFragment.show(getFragmentManager(), "dialog");
            /* Intent playIntent=new Intent(getActivity(),PlayRecordActivity.class);
             playIntent.putExtra("source",);
             startActivity(playIntent);*/
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void loadPreferences() {
        sortOrder = (preferences.getInt(Utility.SORT_PREFERENCES, 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFolderRecords();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_records_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

            case R.id.itemRecord:
                Intent newRecordActivity=new Intent(this, RecordActivity.class);
                newRecordActivity.putExtra("path",path);
                startActivity(newRecordActivity);
                break;
            case R.id.itemSort:
                startActivityForResult(new Intent(this, SortingActivity.class), 24567);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getFolderRecords()
    {
        loadPreferences();
        records=new ArrayList<RecordData>();
        listAllFilesInDirectory(path);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data==null)
            return;
        getFolderRecords();
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void playRecord(int position) {
        Intent playIntent=new Intent(this,PlayRecordActivity.class);
        playIntent.putExtra("source",((RecordData)mAdapter.getItem(position)).getUrl());
        startActivity(playIntent);
    }

    @Override
    public void refresh() {
        getFolderRecords();
    }
}
