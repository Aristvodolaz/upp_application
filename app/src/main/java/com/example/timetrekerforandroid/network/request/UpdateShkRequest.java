package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class UpdateShkRequest {
    @SerializedName("taskName") String taskName;
    @SerializedName("articul") String articul;
    @SerializedName("newSHK") String newShk;

    public UpdateShkRequest(String taskName, String articul, String newShk) {
        this.taskName = taskName;
        this.articul = articul;
        this.newShk = newShk;
    }


}
