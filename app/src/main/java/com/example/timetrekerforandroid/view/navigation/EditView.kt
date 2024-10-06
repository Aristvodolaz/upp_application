package com.example.timetrekerforandroid.view.navigation

import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.DataWBResponse
import com.example.timetrekerforandroid.network.response.Value

interface EditView {
    fun success(msg: String)
    fun error(msg: String)
    fun getData(data: List<ArticlesResponse.Articuls>)
    fun getDataWB(data: List<DataWBResponse>)
    fun getLdu(lduList: List<Value>)

}