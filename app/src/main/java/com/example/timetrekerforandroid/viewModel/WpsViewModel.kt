package com.example.timetrekerforandroid.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetrekerforandroid.model.WpsModel
import com.example.timetrekerforandroid.network.response.PrivyazkaResponse
import com.example.timetrekerforandroid.network.response.Sklads
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.network.response.Zapis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WpsViewModel(private val model: WpsModel) : ViewModel() {

    private val _boxData = MutableLiveData<Result<PrivyazkaResponse>>()
    val boxData: LiveData<Result<PrivyazkaResponse>> get() = _boxData

    private val _endData = MutableLiveData<Result<PrivyazkaResponse>>()
    val endData: LiveData<Result<PrivyazkaResponse>> get() = _endData

    private val _zapisData = MutableLiveData<Result<List<Zapis>>>()
    val zapisData: LiveData<Result<List<Zapis>>> get() = _zapisData

    private val _addData = MutableLiveData<Result<PrivyazkaResponse>>()
    val addData: LiveData<Result<PrivyazkaResponse>> get() = _addData

    private val _skladData = MutableLiveData<Result<List<Sklads>>>()
    val skladData: LiveData<Result<List<Sklads>>> get() = _skladData

    private val _checkShk = MutableLiveData<Result<UniversalResponse>>()
    val checkShk: LiveData<Result<UniversalResponse>> get() = _checkShk

    // Добавляем LiveData для сообщений об ошибке
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
                Log.e("WpsViewModel", "Ошибка при загрузке данных: ${err.message}")
                _errorMessage.value = "Данный ШК уже был использован для этого задания"
            }
        }
    }

    fun addZapisWithWps(shkWps: String, kolVo: Int, pallet: String) {
        launchDataLoad {
            val response = withContext(Dispatchers.IO) {
                model.addZapisWithWps(shkWps, kolVo, pallet)
            }
            handleResponse(response, _boxData, "Записи не найдены")
        }
    }

    fun getListZapis() {
        launchDataLoad {
            val response = withContext(Dispatchers.IO) {
                model.getListZapis()
            }
            handleResponse(response.value ?: emptyList(), _zapisData, "Записи не найдены")
        }
    }

    fun endStatus() {
        launchDataLoad {
            val response = withContext(Dispatchers.IO) {
                model.endStatusWb()
            }
            handleResponse(response, _endData, "Записи не найдены")
        }
    }

    fun updateWps(kolVo: Int, pallet: String, shk: String) {
        launchDataLoad {
            val response = withContext(Dispatchers.IO) {
                model.updateStatus(pallet, kolVo, shk)
            }
            handleResponse(response, _addData, "Записи не найдены")
        }
    }

    fun getSklads() {
        launchDataLoad {
            val response = withContext(Dispatchers.IO) {
                model.getSklads()
            }
            handleResponse(response.value ?: emptyList(), _skladData, "Записи не найдены")
        }
    }

    fun checkWps(shk: String){
        launchDataLoad {
            val response = withContext(Dispatchers.IO){
                model.checkWps(shk)
            }
            handleResponse(response,_checkShk,"Данный ШК ВПС уже используется в заказе!")
        }
    }
}
