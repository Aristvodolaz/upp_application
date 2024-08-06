package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckBoxRequest {
    @SerializedName("Nazvanie_Zadaniya") String zadanie;
    @SerializedName("SHK") String shk;
    @SerializedName("columnsToUpdate") List<String> columns;

    public CheckBoxRequest(String zadanie, String shk, List<String> columns) {
        this.zadanie = zadanie;
        this.shk = shk;
        this.columns = columns;
    }


}
