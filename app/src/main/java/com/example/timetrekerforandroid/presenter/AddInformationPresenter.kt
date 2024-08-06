package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.network.response.FinishedResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.view.AddInformationView
import rx.Subscriber

class AddInformationPresenter(private var view: AddInformationView) {
    private var model = TaskModel()

    fun sendFinishedInformation(mesto: String, vlozhennost: String, pallet: String){
        model.sendFinishedInformation(mesto,vlozhennost,pallet).subscribe(object : Subscriber<FinishedResponse>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.msgError("Ошибка соединения, повторите попытку")
            }

            override fun onNext(response: FinishedResponse) {
                if(response!=null && response.isSuccess)
                    view.msgSuccess("Данные успешно записаны")
                else view.msgError("Ошибка записи данных, повторите попытку")
            }

        })
    }

    fun createDuplicate( mesto: String, vlozhennost: String, pallet: String){
        model.getDuiplicate( mesto, vlozhennost, pallet).subscribe(object : Subscriber<UniversalResponse>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.msgError("Ошибка соединения, повторите попытку")
            }

            override fun onNext(response: UniversalResponse) {
                if(response!=null && response.isSuccess)
                    view.msgSuccessDuplicate("Данные успешно записаны")
                else view.msgError("Ошибка записи данных, повторите попытку")
            }

        })
    }

}