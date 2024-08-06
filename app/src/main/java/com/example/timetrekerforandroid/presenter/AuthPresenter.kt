package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.InformationModel
import com.example.timetrekerforandroid.network.response.AuthResponse
import com.example.timetrekerforandroid.view.AuthView
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AuthPresenter(private val view: AuthView) {
    private var model = InformationModel()

    fun getAuthUser(id: String) {
        model.getAuthUser(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                handleResponse(response)
            }, { error ->
                view.errorMessage("Ошибка соединения, повторите попытку.")
            })
    }

    private fun handleResponse(response: AuthResponse?) {
        if (response != null && response.isSuccess && response.value.isNotEmpty()) {
            view.successMessage(response.value.first().name)
        } else {
            view.errorMessage("Ошибка обработки данных, повторите попытку.")
        }
    }

}

