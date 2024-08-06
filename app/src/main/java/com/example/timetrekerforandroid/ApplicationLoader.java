package com.example.timetrekerforandroid;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;


import com.example.timetrekerforandroid.util.Utils;

public class ApplicationLoader extends MultiDexApplication {
    public static ApplicationLoader instance;
    public static Context applicationContext;
    public static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //disable night theme
        instance = this;
        handler = new Handler(Looper.getMainLooper());
        Utils.init();
    }
        public static ApplicationLoader getInstance(){return instance;}

}
