package com.example.timetrekerforandroid.view

import com.example.timetrekerforandroid.network.response.Data

interface StartView {
    fun getDataInWork(data: List<Data>)
    fun msg(msg: String)
}