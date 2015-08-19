package com.azharkova.writemesound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aniou_000 on 30.07.2015.
 */
public class CollectionsAdapter extends BaseAdapter {
    private Context mContext;
    private List<CollectionEntity> collections;
    private LayoutInflater mInflater;

    public CollectionsAdapter(Context context)
    {
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        collections=new ArrayList<CollectionEntity>() ;

    }
    public CollectionsAdapter(Context context,List<CollectionEntity> list)
    {
        this(context);
        collections=new ArrayList<CollectionEntity>(list);
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int i) {
        return collections.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CollectionEntity c= (CollectionEntity) getItem(i);
        if (view==null)
        {
            view=mInflater.inflate(R.layout.collection_item_layout,viewGroup,false);
        }
        TextView tvName=(TextView)view.findViewById(R.id.tvCollectionName);
        TextView tvCount=(TextView)view.findViewById(R.id.tvRecordsCount);
        tvName.setText(c.getName());
        tvCount.setText(c.getRecordsCount()+" "+mContext.getResources().getString(R.string.records));
        return view;
    }

    public void setCollections(List<CollectionEntity> list)
    {
        collections=new ArrayList<CollectionEntity>(list);
        notifyDataSetChanged();
    }
}
