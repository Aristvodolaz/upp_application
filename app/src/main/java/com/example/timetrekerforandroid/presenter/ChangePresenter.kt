package com.example.timetrekerforandroid.presenter

import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.network.response.FinishedResponse
import com.example.timetrekerforandroid.view.ChangeView
import rx.Subscriber

class ChangePresenter(private var view: ChangeView) {
    private var model = TaskModel()

    fun sendFinishedInformation(mesto: String, vlozhennost: String, pallet: String){
        model.sendFinishedInformation(mesto,vlozhennost,pallet).subscribe(object : Subscriber<FinishedResponse>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.error("Ошибка соединения, повторите попытку")
            }

            override fun onNext(response: FinishedResponse) {
                if(response!=null && response.isSuccess)
                    view.success()
                else view.error("Ошибка записи данных, повторите попытку")
            }

        })
    }

}