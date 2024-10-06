package com.example.timetrekerforandroid.view

import com.example.timetrekerforandroid.network.response.ArticlesResponse

interface TasksView {
    fun getData(tasks: List<ArticlesResponse.Articuls>)
    fun msg(msg: String)
}