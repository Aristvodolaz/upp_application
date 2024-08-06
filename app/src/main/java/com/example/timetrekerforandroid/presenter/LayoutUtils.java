package com.example.timetrekerforandroid.presenter;

import android.view.ViewGroup;

import com.example.timetrekerforandroid.util.Utils;

public class LayoutUtils {
    public static int MATCH_PARENT = -1;
    public static int WRAP_CONTENT = -2;

    public static ViewGroup.LayoutParams createViewGroup(int width, int height) {
        return new ViewGroup.LayoutParams(
                transformParam(width), transformParam(height));
    }

    public static int transformParam(int param) {
        return param < 0 ? param : Utils.dpToPx(param);
    }
}

