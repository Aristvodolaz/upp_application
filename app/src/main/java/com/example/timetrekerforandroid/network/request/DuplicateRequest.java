package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class DuplicateRequest {
    @SerializedName("taskName") String taskName;
    @SerializedName("articul") int articul;
    @SerializedName("mesto") String mesto;
    @SerializedName("vlozhennost") String vlozhennost;
    @SerializedName("pallletNo") String palletNo;

    public DuplicateRequest(String taskName, int articul, String mesto, String vlozhennost, String palletNo) {
        this.taskName = taskName;
        this.articul = articul;
        this.mesto = mesto;
        this.vlozhennost = vlozhennost;
        this.palletNo = palletNo;
    }
}
