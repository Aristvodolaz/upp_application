package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class EndStatusRequest {
    @SerializedName("taskName") String taskName;
    @SerializedName("articul") String articul;
    @SerializedName("status") int status;
    @SerializedName("endTime") String endTime;
    @SerializedName("ispolnitel") String ispolnitel;
    @SerializedName("statusZadaniya") int statusZdaniya;

    public EndStatusRequest(String taskName, String articul, int status, String endTime, String ispolnitel, int statusZdaniya) {
        this.taskName = taskName;
        this.articul = articul;
        this.status = status;
        this.endTime = endTime;
        this.ispolnitel = ispolnitel;
        this.statusZdaniya = statusZdaniya;
    }
}
