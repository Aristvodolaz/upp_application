package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.InformationModel
import com.example.timetrekerforandroid.view.TasksView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class TasksPresenter (private  var view: TasksView) {
    private var model = InformationModel()

    fun getArtikules(name: String) {
        model.getArtikules(name)
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                if (response.isSuccess) {
                    view.getData(response.articuls)
                } else {
                    view.msg("Ошибка получения данных, проверьте ввод или попробуйте позже.")
                }
            }, { error ->
                view.msg("Ошибка соединения, проверьте подключение.")
            })
    }
}