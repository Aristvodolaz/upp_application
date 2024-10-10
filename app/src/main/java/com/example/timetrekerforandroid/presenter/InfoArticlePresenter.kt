package com.example.timetrekerforandroid.presenter

import android.util.Log
import com.example.timetrekerforandroid.model.ScanModel
import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.ShkInDbResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.TimeHelper.getTime
import com.example.timetrekerforandroid.view.InfoArticleView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class InfoArticlePresenter(private var view: InfoArticleView) {
    private var taskModel = TaskModel()
    private var model = ScanModel()
    private val presenterScope = CoroutineScope(Dispatchers.Main)

    fun changeStatusTask(article: String, status: Int){
        taskModel.updateStatus(SPHelper.getNameTask(),article, status, SPHelper.getNameEmployer()).subscribe(object : Subscriber<UniversalResponse>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.errorMessage("Ошибка соединения")
            }

            override fun onNext(response: UniversalResponse) {
                if(response.isSuccess){
                    view.successFindShk("Артикул взят в работу")
                } else view.errorMessage("Статус задания не изменен, повторите попытку сканирования")
            }
        })
    }

    fun setInSharedPref(shk: String, article: String, name: String){
        SPHelper.setShkWork(shk)
        SPHelper.setArticuleWork(article)
        SPHelper.setNameStuffWork(name)
    }

    fun findInExcel(shk: String, name: String){
        model.findShkInExcel(name, shk).subscribe(object : Subscriber<ArticlesResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.errorMessage(e.localizedMessage)
            }

            override fun onNext(response: ArticlesResponse) {
                if(response.isSuccess){
                    setInSharedPref(response.articuls[0].shk, response.articuls[0].artikul.toString(), response.articuls[0].nazvanieTovara)
                    updateShk(shk)
                    SPHelper.setShkWork(shk)
                } else {
                    searchArticleInDb(SPHelper.getArticuleWork())
                }
            }
        })

    }

    fun searchArticleInDb(article: String){
        model.findShkInDBWithArticule(article).subscribe(object: Subscriber<ShkInDbResponse>(){
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

    fun searchArticleInDbForSG(article: String){
        model.findShkInDBWithArticule(article).subscribe(object: Subscriber<ShkInDbResponse>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.errorMessage(e.localizedMessage)
            }

            override fun onNext(response: ShkInDbResponse) {
                if(response.isSuccess && response.value!=null){
                    if(response.value[0].periodWatch == 1 && response.value[0].periodDays>0){
                        view.checkLastPeriodDate(response.value[0].periodDays)
                    } else{
                        view.writeLastDate()
                    }
                } else {
                    view.errorMessage( "Артикул не найден!")
                }
            }
        })
    }


    fun updateShk(shk: String){
        model.updateShk(shk)
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

    fun sendSrokGodnosti(date: String, persent: String) {
        taskModel.sendSrokGodnosti(date, persent).subscribe(object: Subscriber<UniversalResponse>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.errorMessage("Ошибка соединения")
            }

            override fun onNext(response: UniversalResponse) {
                if(response.isSuccess ){
                    if(SPHelper.getPrefics().equals("WB")) sendWBSrok(date)
                    else view.successWriteSG()
                } else view.errorWriteSg()
            }
        })
    }

    fun sendWBSrok(data: String) {
        presenterScope.launch {
            try {
                // Выполнение в фоновом потоке
                withContext(Dispatchers.IO) {
                    taskModel.addSrokForWB(data)
                }
                view.successWriteSG()
                // Здесь можно обработать успех, если нужно
            } catch (e: Exception) {
                // Обработка ошибок, если операция завершилась неудачей
                Log.e("WpsViewModel", "Ошибка при отправке срока для WB: ${e.message}")
            }
        }
    }

    fun sendEndStatus(){
        taskModel.endStatus()
            .subscribeOn(Schedulers.io()) // Выполняем запрос в фоновом потоке
            .observeOn(AndroidSchedulers.mainThread()) // Обрабатываем результат на основном потоке
            .subscribe({ response ->
                if (response.isSuccess) {
                    view.successEndSG()
                } else {
                    view.errorMessage("Ошибка соединения, проверьте подключение.")
                }
            }, { error ->
                view.errorMessage("Ошибка соединения, проверьте подключение.")
            })
    }

    fun cancelTask(reason: String, comment: String) {
        // Запуск корутины
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Вызов метода cancelTask в модели
                val response = withContext(Dispatchers.IO) {
                    taskModel.calncelTask(reason, comment) // Предполагается, что это suspend функция
                }

                // Обработка ответа
                if (response.isSuccess) {
                    view.successEndStatus()
                } else {
                    view.errorWriteSg()
                }
            } catch (e: Exception) {
                // Обработка ошибок
                view.errorMessage("Ошибка соединения: ${e.localizedMessage}")
            }
        }
    }

}