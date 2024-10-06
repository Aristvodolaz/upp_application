package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.view.ChooseOpView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChooseOpPresenter(private var view: ChooseOpView) {
    private var model = TaskModel()


    fun sendChooseOpInDB(list: List<String>) {
        model.updateStatusOPChoose(list)
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                if (response.isSuccess) {
                    sendStatus()
                } else {
                    view.msgError("При записи произошла ошибка, попробуйте еще раз")
                }
            }, { error ->
                view.msgError("Ошибка: ${error.localizedMessage}")
            })
    }

    fun sendStatus(){
        model.setStatus(3)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.isSuccess) {
                    view.msgSuccess("Данные успешно записаны")
                } else {
                    view.msgError("При записи произошла ошибка, попробуйте еще раз")
                }
            }, { error ->
                view.msgError("Ошибка: ${error.localizedMessage}")
            })
    }


    fun getData(){
        model.getLdu(SPHelper.getArticuleWork().toInt(), SPHelper.getNameTask())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.isSuccess) {
                    view.getData(response)
                } else {
                    view.msgError("При записи произошла ошибка, попробуйте еще раз")
                }
            }, { error ->
                view.msgError("Ошибка: ${error.localizedMessage}")
            })

    }
}