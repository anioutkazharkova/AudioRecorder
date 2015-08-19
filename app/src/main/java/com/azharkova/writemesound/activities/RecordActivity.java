package com.azharkova.writemesound.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azharkova.writemesound.data.FileManager;
import com.azharkova.writemesound.IRefreshable;
import com.azharkova.writemesound.R;
import com.azharkova.writemesound.Utility;
import com.azharkova.writemesound.fragments.AlreadySavedDialogFragment;
import com.azharkova.writemesound.fragments.SaveFileDialogFragment;

import java.io.IOException;
import java.util.Date;


public class RecordActivity extends ActionBarActivity implements IRefreshable {

    private static final String TAG = "WRITEMESOUND";
    ImageView play, stop, record;
    private MediaRecorder mRecorder;
    private String outputFile = "";
    private TextView tvTime;
    private volatile boolean isRecording = false;
    private volatile boolean isPlaying = false;

    TextView tvStatus;

    ImageView recordFrame, pauseFrame, playFrame;

    String path = "";
    private MediaPlayer mPlayer;
    private AudioRecord mAudioRecord;

    int myBufferSize = 8192;

    boolean isReading = false;
    private boolean isSaved = false;
    private boolean isPaused = false;
    private boolean hasRecorded = false;
    Thread playingCountThread, recordCountThread;
    private Runnable recordCountRunnable, playingCountRunnable;
    private SharedPreferences preferences;
    int channel =  1, rate = 8000, format = MediaRecorder.OutputFormat.THREE_GPP;
    private String extension = "3gp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        play = (ImageView) findViewById(R.id.btnPlay);
        stop = (ImageView) findViewById(R.id.btnPause);
        record = (ImageView) findViewById(R.id.btnRecord);

        playFrame = (ImageView) findViewById(R.id.btnPlayFrame);
        pauseFrame = (ImageView) findViewById(R.id.btnPauseFrame);
        recordFrame = (ImageView) findViewById(R.id.btnRecordFrame);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        changeEnabled(false, stop, pauseFrame);
        changeEnabled(false, play, playFrame);

        path = getIntent().getStringExtra("path");
        if (path.equals("")) {
            path = Utility.ROOT_FOLDER;
        }
        preferences = getSharedPreferences(Utility.PREFERENCES, Context.MODE_PRIVATE);
        loadPreferences();

        mRecorder = new MediaRecorder();
        //createAudioRecorder();
        recordCountRunnable = new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (isRecording) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final String time = prepareTime(progress);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTime.setText(time);
                        }
                    });
                    progress += 1;

                }
            }
        };
        playingCountRunnable = new Runnable() {
            @Override
            public void run() {

                int progress = 0;
                while (isPlaying) {

                    progress = mPlayer.getCurrentPosition() / 1000;
                    final String time = prepareTime(progress);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTime.setText(time);
                            if (!mPlayer.isPlaying()) {
                                play.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                                changeEnabled(true, play, playFrame);

                                //mPlayer.stop();
                                //mPlayer.release();
                                isPlaying = false;
                            }
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        recordFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareRecorder(false);
                try {
                    //recordStart();


                    mRecorder.prepare();
                    mRecorder.start();
                    isRecording = true;
                    hasRecorded = true;
                    if (recordCountThread != null) {
                        if (!recordCountThread.isInterrupted())
                            recordCountThread.interrupt();
                    }
                    recordCountThread = new Thread(recordCountRunnable);
                    recordCountThread.start();
                    tvStatus.setText(R.string.recording);

                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                record.setImageResource(R.drawable.record_button_disabled);
                recordFrame.setImageResource(R.drawable.button_frame_disabled);
                recordFrame.setEnabled(false);
                stop.setEnabled(true);
                pauseFrame.setEnabled(true);
                changeEnabled(true, stop, pauseFrame);
                //  Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        pauseFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recordStop();
                mRecorder.stop();
                tvStatus.setText(R.string.stop_must_save);
                isRecording = false;
                mRecorder.reset();
                pauseFrame.setEnabled(false);
                playFrame.setEnabled(true);
                prepareMediaPlayer();
                changeEnabled(false, stop, pauseFrame);
                changeEnabled(true, play, playFrame);

                record.setImageResource(R.drawable.record_button);
                recordFrame.setImageResource(R.drawable.button_frame);
                recordFrame.setEnabled(true);


                saveFile();

                record.setImageResource(R.drawable.record_button);
                // Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
            }
        });

        playFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {

                if (mPlayer==null)
                {
                    prepareMediaPlayer();
                }
                //  play.setEnabled(false);
                //changeEnabled(false, play, playFrame);
                if (!isPlaying) {
                    play.setImageResource(R.drawable.ic_pause_black_24dp);
                    isPlaying = true;
                    tvStatus.setText(R.string.play_must_save);


                    tvTime.setText("00:00:00");

                    mPlayer.start();
                    int progress = mPlayer.getCurrentPosition() / 1000;
                    String time = prepareTime(progress);
                    tvTime.setText(time);
                    if (playingCountThread != null) {
                        if (!playingCountThread.isInterrupted()) {
                            playingCountThread.interrupt();
                        }
                    }
                    playingCountThread = new Thread(playingCountRunnable);
                    playingCountThread.start();

                    if (isPaused) {

                        isPaused = false;
                    }
                    //  Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
                } else {
                    tvStatus.setText(R.string.play_stopped);
                    stopPlaying();
                }
            }
        });
    }

    private void stopPlaying() {
        if (mPlayer != null && mPlayer.isPlaying()) {

            mPlayer.pause();
            isPaused = true;
            isPlaying = false;

            play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }

    }

    private void prepareMediaPlayer() {
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTempFile() {
        if (!outputFile.equals("")) {
            FileManager.getInstance().deleteFile(outputFile);
        }
        outputFile = path + "/recording" + (new Date().getTime()) + "." + extension;
    }

    private void prepareRecorder(boolean isColdStart) {

        mRecorder = new MediaRecorder();
        createTempFile();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(format);
        mRecorder.setAudioChannels(channel);
        mRecorder.setAudioSamplingRate(rate);
        if (format == MediaRecorder.OutputFormat.THREE_GPP) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            extension = "3gp";
        }
        if (format == MediaRecorder.OutputFormat.MPEG_4) {

            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            extension = "mp4";
        }
        if (format == MediaRecorder.OutputFormat.AMR_NB) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            extension = "amr";
        }

        createTempFile();
        //Toast.makeText(this,extension,Toast.LENGTH_LONG).show();
        mRecorder.setOutputFile(outputFile);
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

    @Override
    protected void onDestroy() {
        if (!isSaved) {
            FileManager.getInstance().deleteFile(outputFile);
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (hasRecorded) {
                    if (!isSaved) {
                        FileManager.getInstance().deleteFile(outputFile);
                    }
                }
                finish();
                break;
            case R.id.itemDone:
                if (hasRecorded) {
                    if (!isSaved)
                        saveFile();
                    else {
                        AlreadySavedDialogFragment savedDialogFragment = new AlreadySavedDialogFragment();
                        savedDialogFragment.show(getFragmentManager(), "already_saved");
                    }
                }
                else finish();
                break;
            case R.id.itemSettings:
                Intent settingsIntent = new Intent(this, RecordSettingsActivity.class);
                startActivityForResult(settingsIntent, 1234);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    private void changeEnabled(boolean flag, ImageView button, ImageView frame) {
        if (!flag) {
            button.getDrawable().mutate().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);

            frame.setImageResource(R.drawable.button_frame_disabled);
        } else {
            button.getDrawable().mutate().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_IN);
            frame.setImageResource(R.drawable.button_frame);
        }
        button.setEnabled(flag);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            //   mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
             mPlayer = null;
        }
    }

    @Override
    public void refresh() {
        isSaved = true;
        // if (isToFinish)
        finish();
    }

    private void saveFile() {
        SaveFileDialogFragment dialogFragment = new SaveFileDialogFragment();
        dialogFragment.setUrl(outputFile);
        dialogFragment.setRefreshableCallback(this);
        dialogFragment.show(getFragmentManager(), "save_dialog");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            loadPreferences();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadPreferences() {
        //preferences = getSharedPreferences(Utility.PREFERENCES, Context.MODE_PRIVATE);
        channel = preferences.getInt(Utility.CH_PREFERENCES_VALUE, channel);
        rate = preferences.getInt(Utility.RATE_PREFERENCES_VALUE, rate);
        format = preferences.getInt(Utility.AUDIO_PREFERENCES_VALUE, format);


    }
/* void createAudioRecorder() {
        int sampleRate = 8000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, audioFormat);
        int internalBufferSize = minInternalBufferSize * 4;
        Log.d(TAG, "minInternalBufferSize = " + minInternalBufferSize
                + ", internalBufferSize = " + internalBufferSize
                + ", myBufferSize = " + myBufferSize);

        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfig, audioFormat, internalBufferSize);
    }

    public void recordStart() {
        Log.d(TAG, "record start");
        mAudioRecord.startRecording();
        int recordingState = mAudioRecord.getRecordingState();
        Log.d(TAG, "recordingState = " + recordingState);

        try {
            readStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordStop() {
        Log.d(TAG, "record stop");
        mAudioRecord.stop();
        mAudioRecord.release();
        readStop();
    }

    public void readStart() throws FileNotFoundException,IOException{
        Log.d(TAG, "read start");
        isReading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mAudioRecord == null)
                    return;

                byte[] myBuffer = new byte[myBufferSize];
                int readCount = 0;
                int totalCount = 0;
                File newFile=new File(outputFile);
                FileOutputStream out=null;
                try {
                    out = new FileOutputStream(newFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                while (isReading) {
                    readCount = mAudioRecord.read(myBuffer, 0, myBufferSize);
                    try {
                        out.write(myBuffer,0,readCount);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if(readCount == AudioRecord.ERROR_INVALID_OPERATION)
                    {
                        System.err.println("read() returned ERROR_INVALID_OPERATION");
                        return;
                    }

                    if(readCount == AudioRecord.ERROR_BAD_VALUE)
                    {
                        System.err.println("read() returned ERROR_BAD_VALUE");
                        return;
                    }
                    totalCount += readCount;
                    Log.d(TAG, "readCount = " + readCount + ", totalCount = "
                            + totalCount);
                }

                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void readStop() {
        Log.d(TAG, "read stop");
        isReading = false;
    }*/

}
