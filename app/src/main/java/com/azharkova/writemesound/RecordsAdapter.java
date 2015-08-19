package com.azharkova.writemesound;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azharkova on 30.07.2015.
 */
public class RecordsAdapter extends BaseAdapter {

    private List<RecordData> records;
    private Context mContext;
    private  LayoutInflater mInflater;
    private IRecordCallback callback;

    public RecordsAdapter(Context context)
    {
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        records=new ArrayList<RecordData>();
    }
    public RecordsAdapter(Context context,List<RecordData> list)
    {
        this(context);
       records=new ArrayList<RecordData>(list);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordData r= (RecordData) getItem(i);
        ViewHolder viewHolder;
       if (view==null)
        {
            view= mInflater.inflate(R.layout.record_item_layout,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.tvName=(TextView)view.findViewById(R.id.tvName);
            viewHolder.tvCollection=(TextView)view.findViewById(R.id.tvCollectionName);
            viewHolder.tvDate=(TextView)view.findViewById(R.id.tvDate);
            viewHolder.tvDuration=(TextView)view.findViewById(R.id.tvDuration);
            viewHolder.btnPlayRecord=(ImageView)view.findViewById(R.id.btnPlayRecord);
viewHolder.btnPlayRecord.setTag(i);
            viewHolder.btnPlayRecord.setFocusable(false);
            viewHolder.btnPlayRecord.setClickable(false);

            view.setTag(viewHolder);
       }
        else
       {
           viewHolder=(ViewHolder)view.getTag();
       }
      /*  TextView tvName=(TextView)view.findViewById(R.id.tvName);
        TextView tvCollection=(TextView)view.findViewById(R.id.tvCollectionName);
        TextView tvDate=(TextView)view.findViewById(R.id.tvDate);
        TextView tvDuration=(TextView)view.findViewById(R.id.tvDuration);*/
        viewHolder.btnPlayRecord.setTag(i);
      if (!viewHolder.btnPlayRecord.hasOnClickListeners())
      {
          final int id=(int)viewHolder.btnPlayRecord.getTag();
          viewHolder.btnPlayRecord.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  callback.playRecord((int)view.getTag());
              }
          });
      }
        viewHolder.tvDate.setText(r.getDate());
        viewHolder.tvName.setText(r.getName());
        viewHolder.tvCollection.setText(r.getCollection());
        String duration="";
        int d=r.getDuration()/1000;
        if (d>=3600)
        {
            if (d/3600>=10)
            {
                duration+=d/3600;
            }
            else
                duration+="0"+d/3600+":";
            d-=(d/3600)*3600;
        }
        else duration+="00:";
        if (d>=60)
        {
            if (d/60>=10)
            {
                duration+=d/60;
            }
            else
                duration+="0"+d/60+":";
            d-=(d/60)*60;
        }
        else duration+="00:";
        if (d>=0)
        {
            if (d>=10)
            {
                duration+=d;
            }
            else duration+="0"+d;
        }
        viewHolder.tvDuration.setText(duration);

        return view;
    }

class ViewHolder{
    public TextView tvName,tvCollection,tvDuration,tvDate;
    public ImageView btnPlayRecord;
}

    public  void setRecords(List<RecordData> list)
    {
        records=new ArrayList<RecordData>(list);
        notifyDataSetChanged();
    }
    
    public void setRecordCallback(IRecordCallback callback)
    {
        this.callback=callback;
    }
}
