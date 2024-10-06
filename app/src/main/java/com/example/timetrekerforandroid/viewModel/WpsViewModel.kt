    package com.example.timetrekerforandroid.viewModel

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.timetrekerforandroid.model.WpsModel
    import com.example.timetrekerforandroid.network.response.PrivyazkaResponse
    import com.example.timetrekerforandroid.network.response.Sklads
    import com.example.timetrekerforandroid.network.response.SkladsResponse
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


        // Метод добавления записи
        fun addZapisWithWps(shkWps: String, kol_vo: Int, pallet: Int) {

            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        model.addZapisWithWps(shkWps, kol_vo, pallet)
                    }

                    // Проверяем, если response успешный
                    if (response.success) {
                        val zapisList = response // Это уже может быть списком Zapis
                        _boxData.value = Result.success(zapisList)
                    } else {
                        _boxData.value = Result.failure(Exception("Записи не найдены"))
                    }
                } catch (err: Exception) {
                    Log.e("WpsViewModel", "Ошибка при получении данных: ${err.message}")
                    _boxData.value = Result.failure(err)
                }
            }

        }


        fun getListZapis() {
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        model.getListZapis() // Ожидаем ZapisResponse вместо List<ZapisResponse>
                    }

                    // Проверяем, если response успешный
                    if (response.success) {
                        val zapisList = response.value // Это уже может быть списком Zapis
                        _zapisData.value = Result.success(zapisList)
                    } else {
                        _zapisData.value = Result.failure(Exception("Записи не найдены"))
                    }
                } catch (err: Exception) {
                    Log.e("WpsViewModel", "Ошибка при получении данных: ${err.message}")
                    _zapisData.value = Result.failure(err)
                }
            }
        }


        fun endStatus() {
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        model.endStatusWb()
                    }

                    // Проверяем, если response успешный
                    if (response.success) {
                        _endData.value = Result.success(response) // Передаем успешный результат
                    } else {
                        _endData.value = Result.failure(Exception("Записи не найдены"))
                    }
                } catch (err: Exception) {
                    Log.e("WpsViewModel", "Ошибка при получении данных: ${err.message}")
                    _endData.value = Result.failure(err) // Передаем ошибку
                }
            }
        }


        fun updateWps(kolvo: Int,pallet: String, shk: String) {

            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        model.updateStatus(pallet, kolvo, shk)
                    }

                    // Проверяем, если response успешный
                    if (response.success) {
                        val zapisList = response // Это уже может быть списком Zapis
                        _addData.value = Result.success(zapisList)
                    } else {
                        _addData.value = Result.failure(Exception("Записи не найдены"))
                    }
                } catch (err: Exception) {
                    Log.e("WpsViewModel", "Ошибка при получении данных: ${err.message}")
                    _boxData.value = Result.failure(err)
                }
            }

        }

        fun getSklads() {
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        model.getSklads() // Ожидаем ZapisResponse вместо List<ZapisResponse>
                    }

                    // Проверяем, если response успешный
                    if (response.success) {
                        val zapisList = response.value // Это уже может быть списком Zapis
                        _skladData.value = Result.success(zapisList)
                    } else {
                        _skladData.value = Result.failure(Exception("Записи не найдены"))
                    }
                } catch (err: Exception) {
                    Log.e("WpsViewModel", "Ошибка при получении данных: ${err.message}")
                    _skladData.value = Result.failure(err)
                }
            }
        }

    }