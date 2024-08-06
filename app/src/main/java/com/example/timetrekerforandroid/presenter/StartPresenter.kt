package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.InformationModel
import com.example.timetrekerforandroid.network.response.NameDBResponse
import com.example.timetrekerforandroid.network.response.NameFilesInWaitResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.view.StartView
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class StartPresenter(private  var view: StartView) {
    private var model = InformationModel()

    fun getDataInWait() {
        model.getDataInWait()
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                view.getDataInWait(response.names)
            }, { error ->
                view.msg("Ошибка соединения, проверьте подключение.")
            })
    }

    fun getDataInWork() {
        model.getDataInWork()
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                if (response.isSuccess) {
                    view.getDataInWork(response.value)
                } else {
                    view.msg("Ошибка получения данных.")
                }
            }, { error ->
                view.msg("Ошибка соединения, проверьте подключение.")
            })
    }

    fun downloadExcel(name: String) {
        model.downloadExcel(name)
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                if (response.isSuccess) {
                    getDataInWork()
                } else {
                    view.msg("Ошибка получения данных.")
                }
            }, { error ->
                view.msg("Ошибка соединения, проверьте подключение.")
            })
    }
}