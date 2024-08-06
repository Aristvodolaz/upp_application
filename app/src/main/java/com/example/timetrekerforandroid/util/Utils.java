package com.example.timetrekerforandroid.util;

import com.example.timetrekerforandroid.ApplicationLoader;

import java.security.SecureRandom;

public class Utils {
    public static SecureRandom random;

    public static int dpToPx(int dp){
        return (int) (dp* ApplicationLoader.getInstance().getResources().getDisplayMetrics().density);
    }

    static public void init(){ random = new SecureRandom();}
}
