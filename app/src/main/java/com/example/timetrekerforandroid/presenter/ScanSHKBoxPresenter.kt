package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.view.ScanSHKBoxView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ScanSHKBoxPresenter (private val view: ScanSHKBoxView) {
    private var model = TaskModel()

    fun setShkBox(id: String) {
        model.udpateSHKWps(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                handleResponse(response)
            }, { error ->
                view.errorMessage("Ошибка соединения, повторите попытку.")
            })
    }

    private fun handleResponse(response: UniversalResponse?) {
        if (response != null && response.isSuccess) {
            view.successMessage("ШК ВПС успешно записан")
        } else {
            view.errorMessage("Ошибка обработки данных, повторите попытку.")
        }
    }

}

