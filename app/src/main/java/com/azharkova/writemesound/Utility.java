package com.azharkova.writemesound;

import android.os.Environment;

/**
 * Created by azharkova on 31.07.2015.
 */
public class Utility {
    public static String ROOT_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WriteMeSound";

    public static String PREFERENCES="record_preferences";
    public static String CH_PREFERENCES="channel_preferences";
    public static String AUDIO_PREFERENCES="audio_preferences";
    public static String RATE_PREFERENCES="rate_preferences";

    public static String SORT_PREFERENCES="sorting_preferences";

    public static String CH_PREFERENCES_VALUE="channel_preferences_value";
    public static String AUDIO_PREFERENCES_VALUE="audio_preferences_value";
    public static String RATE_PREFERENCES_VALUE="rate_preferences_value";
}
