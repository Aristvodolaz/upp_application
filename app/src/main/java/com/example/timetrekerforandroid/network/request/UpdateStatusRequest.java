package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class UpdateStatusRequest {
    @SerializedName("taskName") String taskName;
    @SerializedName("articul") String articul;
    @SerializedName("startTime") String startTime;
    @SerializedName("status") int status;
    @SerializedName("ispolnitel") String ispolnitel;

    public UpdateStatusRequest(String taskName, String articul, String startTime, int status, String ispolnitel) {
        this.taskName = taskName;
        this.articul = articul;
        this.startTime = startTime;
        this.status = status;
        this.ispolnitel = ispolnitel;
    }
}
