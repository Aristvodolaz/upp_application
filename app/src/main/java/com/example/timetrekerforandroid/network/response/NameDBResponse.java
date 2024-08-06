package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NameDBResponse {
    @SerializedName("success") boolean success;
    @SerializedName("value") List<String> value;
    @SerializedName("errorCode") int errorCode;

    public NameDBResponse(boolean success, List<String> value, int errorCode) {
        this.success = success;
        this.value = value;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getValue() {
        return value;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
