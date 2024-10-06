package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class StatusRequest{
    @SerializedName("taskName") String name;
    @SerializedName("articul") int articul;
    @SerializedName("status") int status;

    public StatusRequest(String name, int articul, int status) {
        this.name = name;
        this.articul = articul;
        this.status = status;
    }
}
