package com.example.timetrekerforandroid.view

interface StartView {
    fun getDataInWait(data: List<String>)
    fun getDataInWork(data: List<String>)
    fun msg(msg: String)
}