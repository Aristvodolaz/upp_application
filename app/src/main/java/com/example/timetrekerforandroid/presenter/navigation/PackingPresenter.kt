package com.example.timetrekerforandroid.presenter.navigation

import android.util.Log
import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.ChooseOpResponse
import com.example.timetrekerforandroid.view.navigation.PackingView
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class PackingPresenter(private val view: PackingView) {
    private val model = TaskModel()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var disposable: Disposable? = null  // Добавляем переменную для хранения подписки

    fun getPackingData() {
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) { model.getPackingData(3) }
                handleResponse(response)
            } catch (throwable: Throwable) {
                Log.d("PackingPresenter", "Ошибка: ${throwable.message}")
                view.error("Ошибка соединения, повторите попытку.")
            }
        }
    }

    private fun handleResponse(response: Observable<ArticlesResponse>) {
        response.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                view.getData(result.articuls)
            }, { error ->
                // Обработка ошибки
                Log.d("PackingPresenter", "Ошибка: ${error.message}")
                view.error("Ошибка обработки данных, повторите попытку.")
            })
    }

    fun getPackingDataLDU() {
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) { model.getTaskData() }
                handleResponseLDU(response)
            } catch (throwable: Throwable) {
                Log.d("PackingPresenter", "Ошибка: ${throwable.message}")
                view.error("Ошибка соединения, повторите попытку.")
            }
        }
    }

    private fun handleResponseLDU(response: Observable<ChooseOpResponse>) {
        response.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                view.getLdu(result.value)
            }, { error ->
                // Обработка ошибки
                Log.d("PackingPresenter", "Ошибка: ${error.message}")
                view.error("Ошибка обработки данных, повторите попытку.")
            })
    }


    // Освобождение ресурсов
    fun clear() {
        disposable?.dispose()
    }
}
