package com.azharkova.writemesound.activities;

import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.azharkova.writemesound.IRefreshable;
import com.azharkova.writemesound.R;
import com.azharkova.writemesound.fragments.RenameFileDialog;
import com.azharkova.writemesound.VisualizerView;
import com.azharkova.writemesound.fragments.MoveFileDialogFragment;
import com.azharkova.writemesound.fragments.RemoveFileDialogFragment;

import java.io.File;
import java.io.IOException;


public class PlayRecordActivity extends ActionBarActivity implements IRefreshable {

    ImageView btnPlay;
    private SeekBar seekBar;
    TextView tvDuration, tvCurrentTime,tvName;
    String source;
    MediaPlayer mPlayer;

    boolean isPlaying = false;
    private boolean isPrepared;

    long length=0;
    private static final float VISUALIZER_HEIGHT_DIP = 200f;

    private Visualizer mVisualizer;

    private LinearLayout mLinearLayout;
    private VisualizerView mVisualizerView;
    android.os.Handler durationHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_record);
        mLinearLayout = (LinearLayout)findViewById(R.id.equalizerLayout);
        btnPlay = (ImageView) findViewById(R.id.btnPlay);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        tvName=(TextView)findViewById(R.id.tvName);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                length=seekBar.getProgress()*1000;
                if (mPlayer!=null && isPrepared) {
                    mPlayer.seekTo((int) length);
                    tvCurrentTime.setText(prepareTime((int)length/1000));
                }

                return false;
            }
        });

        source = getIntent().getStringExtra("source");

        tvName.setText(new File(source).getName());

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isPlaying) {
                    isPlaying = false;
                    pause();
                    btnPlay.setImageResource(R.drawable.ic_play_circle_outline_black_36dp);
                } else {
                    isPlaying = true;
                    try {
                        if (isPrepared) {
                            playRecord();
                            btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_black_36dp);
                        } else preparePlayer(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preparePlayer(false);
    }

    private void preparePlayer(final boolean startImediately)
    {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(source);
        }catch (IOException e){}

        mPlayer.prepareAsync();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                setupVisualizerFxAndUI();
                mVisualizer.setEnabled(true);
                isPrepared = true;
                tvDuration.setText(prepareTime(mPlayer.getDuration() / 1000));
                seekBar.setMax(mPlayer.getDuration() / 1000);
                if (startImediately) {
                    try {
                        playRecord();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_black_36dp);
                }
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });
    }
    private void playRecord() throws IOException {

        isPlaying=true;
        mVisualizer.setEnabled(true);
        mPlayer.start();
        if (length!=0)
            mPlayer.seekTo((int)length);
        durationHandler = new android.os.Handler();
        durationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    if (mPlayer.isPlaying() && isPlaying) {
                        int progress = mPlayer.getCurrentPosition();
                        seekBar.setProgress(progress / 1000);
                        tvCurrentTime.setText(prepareTime(progress / 1000));
                        if (!mPlayer.isPlaying() && isPlaying) {
                            length = 0;
                            isPlaying = false;
                            mPlayer.stop();
                            mPlayer.release();
                            btnPlay.setImageResource(R.drawable.ic_play_circle_outline_black_36dp);
                            durationHandler.removeCallbacksAndMessages(null);
                        } else
                            durationHandler.postDelayed(this, 100);
                    } else {
                        length = 0;
                        isPlaying = false;
                        mVisualizer.setEnabled(false);
                        mPlayer.pause();
                        //mPlayer.release();
                        btnPlay.setImageResource(R.drawable.ic_play_circle_outline_black_36dp);
                        durationHandler.removeCallbacksAndMessages(null);
                    }
                }catch (Exception e){}
            }
        }, 100);

    }

    private String prepareTime(int progress) {
        int hours = progress / (60 * 60);
        String time = "";
        time += (hours >= 10) ? hours : ("0" + hours);
        int minutes = (progress - hours * 60 * 60) / 60;
        time += ":" + ((minutes >= 10) ? minutes : ("0" + minutes));
        int seconds = (progress - hours * 60 * 60 - minutes * 60);
        time += ":" + ((seconds >= 10) ? seconds : ("0" + seconds));
        return time;
    }

    private void pause() {
        mPlayer.pause();
        isPlaying = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (durationHandler!=null)
        durationHandler.removeCallbacksAndMessages(null);
        if (mPlayer!=null)
        {
            try{
                mPlayer.pause();
                mVisualizer.release();

                 mPlayer.release();

            }
            catch (Exception e)
            {

            }
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemDelete) {
            RemoveFileDialogFragment fileDialog=new RemoveFileDialogFragment();
            fileDialog.setUrl(source);
            fileDialog.setRefreshableCallback(this);
            fileDialog.show(getFragmentManager(),"remove_dialog");
        }
        if (id==R.id.itemRename)
        {
            RenameFileDialog fileDialog=new RenameFileDialog();
            fileDialog.setUrl(source);
            fileDialog.setCallback(this);
            fileDialog.show(getFragmentManager(),"rename_dialog");
        }
        if (id==R.id.itemMove)
        {
            MoveFileDialogFragment fileDialog=new MoveFileDialogFragment();
            fileDialog.setUrl(source);
            fileDialog.setRefreshableCallback(this);
            fileDialog.show(getFragmentManager(),"move_dialog");
        }
        if (id==android.R.id.home)
            finish();


        return super.onOptionsItemSelected(item);
    }

    private void setupVisualizerFxAndUI() {
        // Create a VisualizerView (defined below), which will render the simplified audio
        // wave form to a Canvas.
        mVisualizerView = new VisualizerView(this);
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int)(VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)));
        mLinearLayout.removeAllViews();
        mLinearLayout.addView(mVisualizerView);

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                mVisualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {

                mVisualizerView.updateVisualizerFFT(bytes);
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

    @Override
    public void refresh() {
        finish();
    }
}
