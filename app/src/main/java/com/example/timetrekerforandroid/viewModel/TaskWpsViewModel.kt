package com.example.timetrekerforandroid.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.model.TaskWpsModel
import com.example.timetrekerforandroid.network.response.PrivyazkaResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskWpsViewModel(private val model: TaskWpsModel) : ViewModel() {
    private val _cancelTaskResult = MutableLiveData<Result<UniversalResponse>>()
    val cancelTaskResult: LiveData<Result<UniversalResponse>> get() = _cancelTaskResult

    private val _endStatusResult = MutableLiveData<Result<UniversalResponse>>()
    val endStatusResult: LiveData<Result<UniversalResponse>> get() = _endStatusResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private fun <T> handleResponse(response: T, liveData: MutableLiveData<Result<T>>, errorMessage: String) {
        if (response is PrivyazkaResponse && !response.success) {
            liveData.value = Result.failure(Exception(errorMessage))
            _errorMessage.value = errorMessage // Отправляем сообщение об ошибке
        } else {
            liveData.value = Result.success(response)
        }
    }
    private fun launchDataLoad(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (err: Exception) {
                Log.e("TasksViewModel", "Ошибка при загрузке данных: ${err.message}")
                _errorMessage.value = "Данный ШК уже был использован для этого задания"
            }
        }
    }

    fun cancelTask(reason: String, comment: String) {
        launchDataLoad {
            val response = withContext(Dispatchers.IO) {
                model.calncelTask(reason, comment)
            }
            handleResponse(response, _cancelTaskResult, "Записи не найдены")
        }
    }

    fun sendEndStatus() {
        launchDataLoad {
            val response = withContext(Dispatchers.IO) {
                model.endStatusForViewModel()
            }
            handleResponse(response, _endStatusResult, "Записи не найдены")
        }
    }

}
