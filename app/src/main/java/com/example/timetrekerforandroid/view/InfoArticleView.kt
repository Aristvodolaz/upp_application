package com.example.timetrekerforandroid.view

import com.example.timetrekerforandroid.network.response.ArticlesResponse

interface InfoArticleView {
    fun getDataInfo(data: ArticlesResponse.Articuls)
    fun errorMessage(msg: String)
    fun successFindShk(msg: String)
    fun success()
    fun createNewShk(shk: String);
    fun checkLastPeriodDate(days: Int);
    fun writeLastDate()

    fun successWriteSG()
    fun errorWriteSg()

    fun successEndStatus()
    fun successEndSG()
}