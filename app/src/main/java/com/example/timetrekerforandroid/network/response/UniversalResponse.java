package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

public class UniversalResponse {
    @SerializedName("success") boolean success;
//    @SerializedName("value")
    @SerializedName("errorCode") int errorCode;

    public UniversalResponse(boolean success, int errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
