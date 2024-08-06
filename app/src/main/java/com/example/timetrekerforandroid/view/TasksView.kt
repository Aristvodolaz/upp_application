package com.example.timetrekerforandroid.view

import com.example.timetrekerforandroid.network.response.ArticlesResponse.Articuls

interface TasksView {
    fun getData(tasks: List<Articuls>)
    fun msg(msg: String)
}