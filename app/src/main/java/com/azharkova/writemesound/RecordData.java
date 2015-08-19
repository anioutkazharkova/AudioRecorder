package com.azharkova.writemesound;

import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by azharkova on 30.07.2015.
 */
public class RecordData {

    private long id;
    private String name;
    private String date;
    private String url;
    private String collection;
    private int duration;

    private String TIME_FORMAT="dd.MM.yyyy, HH:mm";

    public RecordData()
    {
        id=0;
        name="Record";
        date=new SimpleDateFormat(TIME_FORMAT).format(new Date().getTime());
        url= Environment.getExternalStorageDirectory().getAbsolutePath() + "/WriteMeSound/recording.3gp";
        collection ="Collection";
        duration=60;
        //date=
    }

    public long getId()
    {
        return  this.id;
    }
    public void setId(long id)
    {
        this.id=id;
    }
    public String getName()
    {
        return  this.name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getDate()
    {
        return  this.date;
    }
    public void setDate(String date)
    {
        this.date=date;
    }
    public void setDate(long date)
    {
        this.date=new SimpleDateFormat(TIME_FORMAT).format(new Date(date));
    }
    public String getUrl()
    {
        return  this.url;
    }
    public void setUrl(String url)
    {
        this.url=url;
    }
    public int getDuration()
    {
        return  this.duration;
    }
    public void setCollection(String id)
    {
        this.collection =id;
    }
    public String getCollection()
    {
        return  this.collection;
    }
    public void setDuration(int duration)
    {
        this.duration=duration;
    }

}
