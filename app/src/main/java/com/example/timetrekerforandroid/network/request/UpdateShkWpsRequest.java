package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class UpdateShkWpsRequest {
    @SerializedName("taskName") String taskName;
    @SerializedName("articul") int articul;
    @SerializedName("newSHK") String newSHk;

    public UpdateShkWpsRequest(String taskName, int articul, String newSHk) {
        this.taskName = taskName;
        this.articul = articul;
        this.newSHk = newSHk;
    }
}
