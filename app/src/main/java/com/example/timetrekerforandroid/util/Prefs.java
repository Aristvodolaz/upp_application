package com.example.timetrekerforandroid.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String STORAGE_NAME = "packer_file";

    private static SharedPreferences sharedPreferences = null;

    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(STORAGE_NAME, Activity.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void clear() {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    public static void remove(String key) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * @see SharedPreferences#contains(String)
     */
    private static boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    /**
     * @return Returns the preference value if it exists, or empty string.
     * @see SharedPreferences#getString(String, String)
     */
    public static String getString(String key) {
        return getString(key, "");
    }

    /**
     * @see SharedPreferences#getString(String, String)
     */
    public static String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    /**
     * @see SharedPreferences.Editor#putString(String, String)
     */
    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * @return Returns the preference value if it exists, or <b>-1</b>.
     * @see SharedPreferences#getInt(String, int)
     */
    public static int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * @see SharedPreferences#getInt(String, int)
     */
    public static int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    /**
     * @see SharedPreferences.Editor#putInt(String, int)
     */
    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * @see SharedPreferences#getLong(String, long)
     */
    public static long getLong(String key, long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    /**
     * @return Returns the preference value if it exists, or <b>-1</b>.
     * @see SharedPreferences#getLong(String, long)
     */
    public static long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * @see SharedPreferences#getLong(String, long)
     */
    public static long getLong(String key, int defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    /**
     * @see SharedPreferences.Editor#putLong(String, long)
     */
    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * @see SharedPreferences#getBoolean(String, boolean)
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * @see SharedPreferences.Editor#putBoolean(String, boolean)
     */
    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

}

