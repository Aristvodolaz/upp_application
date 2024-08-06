package com.example.timetrekerforandroid.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeHelper {

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}
