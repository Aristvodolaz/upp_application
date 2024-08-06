package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.view.ChooseOpView
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChooseOpPresenter(private var view: ChooseOpView) {
    private var model = TaskModel()


    fun sendChooseOpInDB(list: List<String>) {
        model.updateStatusOPChoose(list)
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                // Обрабатываем успешный ответ
                if (response.isSuccess) {
                    view.msgSuccess("Данные успешно записаны")
                } else {
                    view.msgError("При записи произошла ошибка, попробуйте еще раз")
                }
            }, { error ->
                // Обрабатываем ошибку
                view.msgError("Ошибка: ${error.localizedMessage}")
            })
    }
}