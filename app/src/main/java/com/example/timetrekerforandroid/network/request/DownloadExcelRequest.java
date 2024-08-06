package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

public class DownloadExcelRequest {
    @SerializedName("fileName") String fileName;
    @SerializedName("host") String host;
    @SerializedName("port") int port;
    @SerializedName("username") String username;
    @SerializedName("password") String password;

    public DownloadExcelRequest(String fileName, String host, int port, String username, String password) {
        this.fileName = fileName;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
}
