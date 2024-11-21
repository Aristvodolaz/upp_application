package com.example.timetrekerforandroid.model

import com.example.timetrekerforandroid.network.ApiService
import com.example.timetrekerforandroid.network.request.EndStatusRequest
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.TimeHelper.getTime

class TaskWpsModel(private val service: ApiService) {
    suspend fun endStatusForViewModel(): UniversalResponse {
        return service.endStatusForViewModel(EndStatusRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork(), 2, getTime(), SPHelper.getNameEmployer(), 1))
    }
    suspend fun calncelTask(reason: String, comment: String): UniversalResponse{
        return  service.cancelTask(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), comment, reason)
    }

}