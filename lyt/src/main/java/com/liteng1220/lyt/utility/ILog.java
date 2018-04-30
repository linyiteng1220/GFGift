package com.liteng1220.lyt.utility;

import android.util.Log;

import com.liteng1220.lyt.BuildConfig;

public class ILog {

    private static final String TAG = "LYT";

    public static void debug(String info) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, info);
        }
    }

    public static void info(String info) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, info);
        }
    }

    public static void warn(String info) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, info);
        }
    }

    public static void error(String location, Exception exception) {
        Log.e(TAG, "Runtime Error: " + location, exception);
    }

}
