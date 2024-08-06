package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

public class FinishedResponse {
    @SerializedName("success") boolean success;
    @SerializedName("message") String msg;

    public FinishedResponse(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

}
