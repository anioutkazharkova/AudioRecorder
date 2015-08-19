package com.azharkova.writemesound;

/**
 * Created by aniou_000 on 30.07.2015.
 */
public class CollectionEntity {
    private  String name;
    private  long id;
    private int recordsCount;
    private String path;

    public String getName()
    {
        return  this.name;

    }
    public void setName(String name)
    {
        this.name=name;
    }
    public long getId()
    {
        return  this.id;
    }

    public void setId(long id)
    {
        this.id=id;
    }

    public void setPath(String path)
    {
        this.path=path;
    }
    public  String getPath()
    {
        return  this.path;
    }
    public int getRecordsCount()
    {
        return  this.recordsCount;
    }
    public void setRecordsCount(int count)
    {
        this.recordsCount=count;
    }
    public CollectionEntity(){}
}
