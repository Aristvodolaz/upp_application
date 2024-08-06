package com.example.timetrekerforandroid.model

import com.example.timetrekerforandroid.network.BaseDataProvider
import com.example.timetrekerforandroid.network.request.CheckBoxRequest
import com.example.timetrekerforandroid.network.request.DuplicateRequest
import com.example.timetrekerforandroid.network.request.EndStatusRequest
import com.example.timetrekerforandroid.network.request.FinishedRequest
import com.example.timetrekerforandroid.network.request.UpdateSrokGodnostiRequest
import com.example.timetrekerforandroid.network.request.UpdateStatusRequest
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.FinishedResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.TimeHelper.getTime
import rx.Observable

class TaskModel: BaseDataProvider() {

    fun getArtikule(nameTask: String, articul:String): Observable<List<ArticlesResponse.Articuls>>{
        return service.getArticulTask(nameTask, articul).compose(applySchedulers())
    }

    fun updateStatus(nameTask: String, articul: String, status: Int, name: String): Observable<UniversalResponse>{
        return service.changeStatus(UpdateStatusRequest(nameTask, articul, getTime(), status, name)).compose(applySchedulers())
    }

    fun updateStatusOPChoose(list: List<String>): Observable<UniversalResponse>{
        return service.updateCheckBox(CheckBoxRequest(SPHelper.getNameTask(), SPHelper.getShkWork(), list)).compose(applySchedulers())
    }

    fun sendFinishedInformation(mesto: String, vlozhennost: String, palet: String): Observable<FinishedResponse>{
        return service.finishedSend(FinishedRequest(SPHelper.getNameTask(), SPHelper.getShkWork(),  mesto,vlozhennost,palet,  getTime())).compose(applySchedulers())

    }

    fun getDuiplicate(mesto: String, vlozhennost: String, palet: String): Observable<UniversalResponse>{
        return service.getDuplicate(DuplicateRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), mesto, vlozhennost, palet)).compose(applySchedulers())
    }

    fun sendSrokGodnosti(date: String, persent: String): Observable<UniversalResponse>{
        return service.sendSrokGodnosti(UpdateSrokGodnostiRequest(date, persent, SPHelper.getArticuleWork(), SPHelper.getNameTask())).compose(applySchedulers())
    }

    fun endStatus(): Observable<UniversalResponse>{
        return service.endStatus(EndStatusRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork(), 2, getTime(), SPHelper.getNameEmployer(), 1)).compose(applySchedulers())
    }
}