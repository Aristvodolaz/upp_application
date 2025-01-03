package com.example.timetrekerforandroid.presenter

import android.util.Log
import com.example.timetrekerforandroid.model.ScanModel
import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.FinishedResponse
import com.example.timetrekerforandroid.network.response.ShkInDbResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.view.AddInformationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AddInformationPresenter(private var view: AddInformationView) {
    private var model = TaskModel()
    private var scanModel = ScanModel()

    fun sendFinishedInformation(mesto: String, vlozhennost: String, pallet: String){
        model.sendFinishedInformation(mesto,vlozhennost,pallet).subscribe(object : Subscriber<FinishedResponse>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.msgError("Ошибка соединения, повторите попытку")
            }

            override fun onNext(response: FinishedResponse) {
                if(response.isSuccess) {
                    Log.d("RESULT", response.toString())
                    view.msgSuccess("Данные успешно записаны")
                }
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

    fun findInExcel(shk: String, name: String){
        scanModel.findShkInExcel(name, shk).subscribe(object : Subscriber<ArticlesResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.errorMessage(e.localizedMessage)
            }

            override fun onNext(response: ArticlesResponse) {
                if(response.isSuccess && response.articuls!=null){
                    updateShk(shk)
                    SPHelper.setShkWork(shk)
                    view.success()
                } else {
                    searchArticleInDb(SPHelper.getArticuleWork())
                }
            }
        })

    }

    fun searchArticleInDb(article: String){
        scanModel.findShkInDBWithArticule(article).subscribe(object: Subscriber<ShkInDbResponse>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.errorMessage(e.localizedMessage)
            }

            override fun onNext(response: ShkInDbResponse) {
                if(response.isSuccess && response.value!=null){
                    if(response.value[0].shk==null || response.value[0].shk.equals("null")){
                        view.createNewShk(SPHelper.getShkWork())
                    } else {
                        SPHelper.setShkWork(response.value[0].shk)
                        updateShk(response.value[0].shk)
                    }
                } else {
                    view.createNewShk(SPHelper.getShkWork())
                }
            }
        })
    }

    fun updateShk(shk: String){
        scanModel.updateShk(shk)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Subscriber<UniversalResponse>(){
                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    view.errorMessage(e.localizedMessage)
//                Log.d("ERROR", e.localizedMessage)
                }

                override fun onNext(response: UniversalResponse) {
                    if(response!=null){
                        if(response.isSuccess ){
                            view.success()
                        } else view.errorMessage("Ошибка обновления ШК, попробуйте позднее")
                    }else view.errorMessage("Ошибка обновления ШК, попробуйте позднее") }
            })
    }


    fun cancelTask(reason: String, comment: String) {

        model.calncelTaskNorm(reason, comment)
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe ({ response ->
                // Обработка ответа
                if (response.isSuccess) {
                    view.successEndStatus()
                } else {
                    view.errorMessage("Ошибка соединения, повторите попытку")
                }
            },  {
                view.errorMessage("Ошибка соединения, проверьте подключение.")
        })
    }

    fun sendEndStatus(){
        model.endStatus()
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                if (response.isSuccess) {
                    view.successGoToPacking()
                } else {
                    view.errorMessage("Ошибка соединения, проверьте подключение.")
                }
            }, { error ->
                Log.d("FFFFFF", error.localizedMessage)
                view.errorMessage("Ошибка соединения, проверьте подключение.")
            })
    }
}