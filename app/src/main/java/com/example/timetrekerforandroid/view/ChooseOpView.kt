package com.example.timetrekerforandroid.view

import com.example.timetrekerforandroid.network.response.ChooseOpResponse

interface ChooseOpView {
    fun msgError(msg: String)
    fun msgSuccess(msg: String)
    fun getData(data: ChooseOpResponse)

}