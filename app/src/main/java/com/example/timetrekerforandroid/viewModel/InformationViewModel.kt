package com.example.timetrekerforandroid.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.timetrekerforandroid.model.NetworkModel
import com.example.timetrekerforandroid.network.request.ChooseOpRequest
import com.example.timetrekerforandroid.network.response.ChooseOpResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.util.SPHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call


class InformationViewModel(private val model: NetworkModel) : ViewModel() {

    private val _finishData = MutableLiveData<Result<UniversalResponse>>()
    val finishData: LiveData<Result<UniversalResponse>> get() = _finishData

    private val _taskData = MutableLiveData<Result<ChooseOpResponse>>()
    val taskData: LiveData<Result<ChooseOpResponse>> get() = _taskData

    fun getTaskData(shk: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    model.getTaskData() // This should return ChooseOpResponse
                }
                _taskData.value = Result.success(response)
            } catch (err: Exception) {
                Log.e("WpsViewModel", "Ошибка при получении данных: ${err.message}")
                _taskData.value = Result.failure(err)
            }
        }
    }


    fun setUpdateLDU(data: ChooseOpRequest) {
        val call = model.updateLdu(data)

        // Execute the call asynchronously
        call.enqueue(object : retrofit2.Callback<UniversalResponse> {
            override fun onResponse(call: Call<UniversalResponse>, response: retrofit2.Response<UniversalResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _finishData.value = Result.success(body)
                    } else {
                        _finishData.value = Result.failure(Exception("Error: Response body is null"))
                    }
                } else {
                    _finishData.value = Result.failure(Exception("Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<UniversalResponse>, t: Throwable) {
                Log.e("WPSVIEWMODEL", "ERRORO{${t.message}}")
                _finishData.value = Result.failure(t)
            }
        })
    }


}
