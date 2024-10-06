package com.example.timetrekerforandroid.network.response

import com.google.gson.annotations.SerializedName

data class SkladsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("value") val value: List<Sklads>,
    @SerializedName("errorCode") val code: Int
)

data class Sklads(
    @SerializedName("Pref") val pref: String,
    @SerializedName("Name") val name: String,
    @SerializedName("City") val city: String
)
