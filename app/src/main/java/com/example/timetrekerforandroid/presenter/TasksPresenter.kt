package com.example.timetrekerforandroid.presenter

import android.util.Log
import com.example.timetrekerforandroid.model.InformationModel
import com.example.timetrekerforandroid.view.TasksView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class TasksPresenter (private  var view: TasksView) {
    private var model = InformationModel()

    fun getArtikules(name: String) {
        model.getArtikules(name, 0)
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                println(response)
                if (response.isSuccess) {
                    view.getData(response.articuls)
                } else {
                    view.msg("Ошибка получения данных, проверьте ввод или попробуйте позже.")
                }
            }, { error ->
                Log.d("ERROR", error.localizedMessage)
                view.msg("Ошибка соединения, проверьте подключение.")
            })
    }
}