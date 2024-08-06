package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NameFilesInWaitResponse {
    @SerializedName("files") List<String> names;

    public NameFilesInWaitResponse(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }
}
