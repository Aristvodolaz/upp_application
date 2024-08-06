package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class SearchShkInExcelRequest {
    @SerializedName("taskName") String taskName;
    @SerializedName("shk") String shk;

    public SearchShkInExcelRequest(String taskName, String shk) {
        this.taskName = taskName;
        this.shk = shk;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getShk() {
        return shk;
    }
}
