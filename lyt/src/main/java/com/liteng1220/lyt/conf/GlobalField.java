package com.liteng1220.lyt.conf;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.liteng1220.lyt.BuildConfig;

public class GlobalField {

    public static void saveField(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences != null) {
            Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putString(key, value);
                editor.apply();
            }
        }
    }

    public static void saveField(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences != null) {
            Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putInt(key, value);
                editor.apply();
            }
        }
    }

    public static void saveField(Context context, String key, float value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences != null) {
            Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putFloat(key, value);
                editor.apply();
            }
        }
    }

    public static void saveField(Context context, String key, long value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences != null) {
            Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putLong(key, value);
                editor.apply();
            }
        }
    }

    public static void saveField(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences != null) {
            Editor editor = sharedPreferences.edit();
            if (editor != null) {
                editor.putBoolean(key, value);
                editor.apply();
            }
        }
    }

    public static String readFieldString(Context context, String key, String defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(key, defValue);
    }

    public static int readFieldInt(Context context, String key, int defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences == null) {
            return 0;
        }
        return sharedPreferences.getInt(key, defValue);
    }

    public static float readFieldFloat(Context context, String key, float defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences == null) {
            return 0;
        }
        return sharedPreferences.getFloat(key, defValue);
    }

    public static long readFieldLong(Context context, String key, long defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences == null) {
            return 0;
        }
        return sharedPreferences.getLong(key, defValue);
    }

    public static boolean readFieldBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences == null) {
            return false;
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (context == null) {
            return null;
        }

        return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }

}
