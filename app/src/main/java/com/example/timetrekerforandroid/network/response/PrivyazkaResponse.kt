package com.example.timetrekerforandroid.network.response

import com.google.gson.annotations.SerializedName

data class PrivyazkaResponse (
    @SerializedName("success") val success: Boolean,
    @SerializedName("value") val msg: String,
    @SerializedName("errorCode") val code: Int
)