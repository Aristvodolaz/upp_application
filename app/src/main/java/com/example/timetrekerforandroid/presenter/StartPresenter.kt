package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.InformationModel
import com.example.timetrekerforandroid.network.response.NameDBResponse
import com.example.timetrekerforandroid.network.response.NameFilesInWaitResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.view.StartView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartPresenter(private var view: StartView) {
    private val model = InformationModel()

    // Создаём CoroutineScope с основным потоком
    private val presenterScope = CoroutineScope(Dispatchers.Main)

    fun getDataInWork() {
        // Запускаем корутину в основном потоке
        presenterScope.launch {
            try {
                // Выполняем запрос в фоновом потоке (Dispatchers.IO)
                val response = withContext(Dispatchers.IO) {
                    model.getDataInWork()
                }

                // Обрабатываем результат на основном потоке
                if (response.success) {
                    view.getDataInWork(response.value)
                } else {
                    view.msg("Ошибка получения данных.")
                }
            } catch (e: Exception) {
                // Обработка ошибок
                view.msg("Ошибка соединения, проверьте подключение.")
            }
        }
    }
}
