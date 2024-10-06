package com.example.timetrekerforandroid.network.response

import com.google.gson.annotations.SerializedName

data class ZapisResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("value") val value: List<Zapis>,
    @SerializedName("errorCode") val errorCode: Int

)

data class Zapis(
    @SerializedName("Nazvanie_Zadaniya") val nazvanieZadaniya: String,
    @SerializedName("Artikul") val artikul: Int,
    @SerializedName("Srok_Godnosti") val srokGodnosti: String,
    @SerializedName("SHK_WPS") val shkWps: String,
    @SerializedName("Pallet_No") val palletNo: Int,
    @SerializedName("Kolvo_Tovarov") val kolvoTovarov: Int
)