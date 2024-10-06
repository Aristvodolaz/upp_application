package com.example.timetrekerforandroid.network.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckBoxRequest {
    @SerializedName("Nazvanie_Zadaniya") String zadanie;
    @SerializedName("artikul") int artikul;
    @SerializedName("columnsToUpdate") List<String> columns;

    public CheckBoxRequest(String zadanie, int artikul, List<String> columns) {
        this.zadanie = zadanie;
        this.artikul = artikul;
        this.columns = columns;
    }


}
