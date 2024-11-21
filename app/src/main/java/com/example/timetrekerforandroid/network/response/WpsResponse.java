package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

public class WpsResponse {
    @SerializedName("vlozhennost") String vlozhennost;
    @SerializedName("pallet") String pallet;

    public WpsResponse(String vlozhennost, String pallet) {
        this.vlozhennost = vlozhennost;
        this.pallet = pallet;
    }
}
