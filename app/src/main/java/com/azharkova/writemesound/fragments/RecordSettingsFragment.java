package com.azharkova.writemesound.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.azharkova.writemesound.activities.MainActivity;
import com.azharkova.writemesound.data.PreferenceEntity;
import com.azharkova.writemesound.R;
import com.azharkova.writemesound.Utility;

import java.util.ArrayList;
import java.util.List;


public class RecordSettingsFragment extends Fragment {
    List<PreferenceEntity> Channels=new ArrayList<PreferenceEntity>();
    List<PreferenceEntity> Encodings=new ArrayList<PreferenceEntity>();
    List<PreferenceEntity> Rates=new ArrayList<PreferenceEntity>();
    List<PreferenceEntity> Formats=new ArrayList<PreferenceEntity>();

    PreferenceEntity selectedRate;

    LinearLayout layout;
    private ArrayList<String> ratesNames;
    private TextView tvRateName,tvRateValue;
    private String[] chNames={"Mono","Stereo"};
    String[] formatsNames={"3GP","MP4","AMR"};
    private PreferenceEntity selectedChannel;
    private TextView tvChannelName,tvChannelValue;
    private PreferenceEntity selectedFormat;
    private TextView tvFormatName;
    private TextView tvFormatValue;
    private SharedPreferences preferences;
    private int channel,rate,format;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.activity_record_settings,container,false);

        setHasOptionsMenu(true);
        layout=(LinearLayout)view.findViewById(R.id.layout);

        int[] channels = {1, 2};

        int[] encodings = {AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT};
        String[] encNames={"8bit","16bit"};

        int[] formats={MediaRecorder.OutputFormat.THREE_GPP,MediaRecorder.OutputFormat.MPEG_4,MediaRecorder.OutputFormat.AMR_NB,MediaRecorder.OutputFormat.AMR_WB};


        for(int i=0;i<chNames.length;i++)
        {
            PreferenceEntity c=new PreferenceEntity();
            c.Value=channels[i];
            c.Name=chNames[i];
            Channels.add(c);
        }

        for(int i=0;i<encNames.length;i++)
        {
            PreferenceEntity c=new PreferenceEntity();
            c.Value=encodings[i];
            c.Name=encNames[i];
            Encodings.add(c);
        }

        for(int i=0;i<formatsNames.length;i++)
        {
            PreferenceEntity c=new PreferenceEntity();
            c.Value=formats[i];
            c.Name=formatsNames[i];
            Formats.add(c);

        }
        Rates=new ArrayList<PreferenceEntity>(getRates());

        layout.addView(getChannelView());

        layout.addView(getRateView());

        layout.addView(getFormatView());
        preferences=getActivity().getSharedPreferences(Utility.PREFERENCES, Context.MODE_PRIVATE);
        loadPreferences();
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
    }
    private  void loadPreferences()
    {

        channel=preferences.getInt(Utility.CH_PREFERENCES, channel);
        rate=preferences.getInt(Utility.RATE_PREFERENCES, rate);
        format=preferences.getInt(Utility.AUDIO_PREFERENCES, format);

        tvChannelValue.setText(chNames[channel]);
        tvRateValue.setText(ratesNames.get(rate));
        tvFormatValue.setText(formatsNames[format]);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.itemDone:

                savePreferences();
                ((MainActivity)getActivity()).onNavigationDrawerItemSelected(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    View getChannelView()
    {
        View channelView= LayoutInflater.from(getActivity()).inflate(R.layout.pref_item_layout, null);
        tvChannelName=(TextView)channelView.findViewById(R.id.tvPrefName);
        tvChannelValue=(TextView)channelView.findViewById(R.id.tvPrefValue);
        tvChannelName.setText(R.string.channel_label);
        tvChannelValue.setText(Channels.get(0).Name);

        channelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelDialogFragmet channelFragment = new ChannelDialogFragmet();
                channelFragment.show(getActivity().getFragmentManager(), "channel_dialog");
            }
        });

        return  channelView;
    }

    View getRateView()
    {
        View rateView= LayoutInflater.from(getActivity()).inflate(R.layout.pref_item_layout, null);
        tvRateName=(TextView)rateView.findViewById(R.id.tvPrefName);
        tvRateValue=(TextView)rateView.findViewById(R.id.tvPrefValue);
        tvRateName.setText(R.string.bitrate_label);
        tvRateValue.setText(Rates.get(0).Name);

        rateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateDialogFragment rateDialogFragment=new RateDialogFragment();
                rateDialogFragment.show(getActivity().getFragmentManager(),"rate_dialog");

            }
        });
        return  rateView;
    }
    View getFormatView()
    {  View formatView= LayoutInflater.from(getActivity()).inflate(R.layout.pref_item_layout, null);
        tvFormatName=(TextView)formatView.findViewById(R.id.tvPrefName);
        tvFormatValue=(TextView)formatView.findViewById(R.id.tvPrefValue);
        tvFormatName.setText(R.string.audio_format_label);
        tvFormatValue.setText(Formats.get(0).Name);

        formatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormatDialogFragmet formatDialogFragmet = new FormatDialogFragmet();
                formatDialogFragmet.show(getActivity().getFragmentManager(), "format_dialog");
            }
        });

        return  formatView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_record_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    private void savePreferences() {

        SharedPreferences.Editor edit=preferences.edit();
        edit.putInt(Utility.CH_PREFERENCES,channel);
        edit.putInt(Utility.AUDIO_PREFERENCES,format);
        edit.putInt(Utility.RATE_PREFERENCES,rate);

        edit.putInt(Utility.CH_PREFERENCES_VALUE,Channels.get(channel).Value);

        edit.putInt(Utility.AUDIO_PREFERENCES_VALUE,  Formats.get(format).Value);
        edit.putInt(Utility.RATE_PREFERENCES_VALUE,Rates.get(rate).Value);

        edit.apply();

    }

    private List<PreferenceEntity> getRates() {
        List<PreferenceEntity> validRates = new ArrayList<PreferenceEntity>();
        ratesNames=new ArrayList<String>();
        int[] rates = {8000, 11025, 22050, 44100, 48000, 96000};


        for (PreferenceEntity enc:Encodings) {
            for (PreferenceEntity ch : Channels) {
                for (int rate : rates) {
                    int t = AudioRecord.getMinBufferSize(rate, ch.Value, enc.Value);

                    if ((t != AudioRecord.ERROR) && (t != AudioRecord.ERROR_BAD_VALUE)) {
                        // добавляем формат
                        PreferenceEntity r=new PreferenceEntity();
                        r.Value=rate;
                        r.Name=((float)rate/1000f)+"kHz";

                        if (ratesNames.indexOf(r.Name)==-1) {
                            ratesNames.add(r.Name);
                            validRates.add(r);
                        }
                    }
                }
            }
        }
        return  validRates;
    }

    class RateDialogFragment extends DialogFragment
    {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.list_pref_dialog_fragment,null);
            ListView listView=(ListView)view.findViewById(R.id.lvPref);

            getDialog().setTitle(R.string.bitrate);

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,ratesNames);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedRate=Rates.get(i);
                    rate=i;
                    tvRateValue.setText(selectedRate.Name);
                    dismiss();
                }
            });

            return view;
        }
    }

    class ChannelDialogFragmet extends DialogFragment
    {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.list_pref_dialog_fragment,null);
            ListView listView=(ListView)view.findViewById(R.id.lvPref);

            getDialog().setTitle(R.string.channel);

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,chNames);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedChannel=Channels.get(i);
                    channel=i;
                    tvChannelValue.setText(selectedChannel.Name);
                    dismiss();
                }
            });

            return view;
        }
    }

    class FormatDialogFragmet extends DialogFragment
    {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.list_pref_dialog_fragment,null);
            ListView listView=(ListView)view.findViewById(R.id.lvPref);

            getDialog().setTitle(R.string.format);

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,formatsNames);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedFormat=Formats.get(i);
                    format=i;
                    tvFormatValue.setText(selectedFormat.Name);
                    dismiss();
                }
            });

            return view;
        }
    }

}
