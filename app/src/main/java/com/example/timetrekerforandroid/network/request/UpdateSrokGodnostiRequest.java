package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class UpdateSrokGodnostiRequest {
    @SerializedName("srokGodnosti") String srok;
    @SerializedName("persent") String persent;
    @SerializedName("articul") String articul;
    @SerializedName("taskName") String taskName;

    public UpdateSrokGodnostiRequest(String srok, String persent, String articul, String taskName) {
        this.srok = srok;
        this.persent = persent;
        this.articul = articul;
        this.taskName = taskName;
    }
}
