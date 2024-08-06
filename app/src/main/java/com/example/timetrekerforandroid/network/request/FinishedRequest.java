package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class FinishedRequest {
    @SerializedName("taskName") String taskName;
    @SerializedName("shk") String shk;
    @SerializedName("mesto") String mesto;
    @SerializedName("vlozhennost") String vlozhennost;
    @SerializedName("palletNo") String palletNo;
    @SerializedName("timeEnd") String timeEnd;

    public FinishedRequest(String taskName, String shk, String mesto, String vlozhennost, String palletNo,  String timeEnd) {
        this.taskName = taskName;
        this.shk = shk;
        this.mesto = mesto;
        this.vlozhennost = vlozhennost;
        this.palletNo = palletNo;
        this.timeEnd = timeEnd;
    }
}
