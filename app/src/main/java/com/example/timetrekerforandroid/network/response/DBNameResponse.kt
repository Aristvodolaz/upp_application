package com.example.timetrekerforandroid.network.response

data class DBNameResponse (
    val success: Boolean,
    val value: List<Data>,
    val errorCode: Int
)

data class Data(
    val Nazvanie_Zadaniya: String,
    val Scklad_Pref: String
)