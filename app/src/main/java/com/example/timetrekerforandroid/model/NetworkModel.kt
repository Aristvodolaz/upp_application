package com.example.timetrekerforandroid.model

import com.example.timetrekerforandroid.network.ApiService
import com.example.timetrekerforandroid.network.request.ChooseOpRequest
import com.example.timetrekerforandroid.network.response.ChooseOpResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.util.SPHelper
import io.reactivex.Observable
import retrofit2.Call

class NetworkModel(private val apiService: ApiService) {

    // Получение данных задачи
     suspend fun getTaskData(): ChooseOpResponse {
        return apiService.taskData(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt())
    }

    fun updateLdu(data: ChooseOpRequest): Call<UniversalResponse> {
        return apiService.changeOP(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), data)
    }

//    fun changeOP(data: ChooseOpRequest): UniversalResponse {
//        return apiService.changeOP(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), data)
//    }
}