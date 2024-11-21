package com.example.timetrekerforandroid.model

import com.example.timetrekerforandroid.network.BaseDataProvider
import com.example.timetrekerforandroid.network.Const
import com.example.timetrekerforandroid.network.request.CheckBoxRequest
import com.example.timetrekerforandroid.network.request.DuplicateRequest
import com.example.timetrekerforandroid.network.request.EndStatusRequest
import com.example.timetrekerforandroid.network.request.FinishedRequest
import com.example.timetrekerforandroid.network.request.SrokGodnostiRequest
import com.example.timetrekerforandroid.network.request.StatusRequest
import com.example.timetrekerforandroid.network.request.UpdateShkWpsRequest
import com.example.timetrekerforandroid.network.request.UpdateSrokGodnostiRequest
import com.example.timetrekerforandroid.network.request.UpdateStatusRequest
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.ChooseOpResponse
import com.example.timetrekerforandroid.network.response.DataWBResponse
import com.example.timetrekerforandroid.network.response.FinishedResponse
import com.example.timetrekerforandroid.network.response.PrivyazkaResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.network.response.WBDataResponse
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.TimeHelper.getTime
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

class TaskModel: BaseDataProvider() {

    fun getArtikule(nameTask: String, articul:String): Observable<List<ArticlesResponse.Articuls>>{
        return service.getArticulTask(nameTask, articul).compose(applySchedulers())
    }

    fun updateStatus(nameTask: String, articul: String, status: Int, name: String): Observable<UniversalResponse>{
        return service.changeStatus(UpdateStatusRequest(nameTask, articul, getTime(), status, name)).compose(applySchedulers())
    }

    fun updateStatusOPChoose(list: List<String>): Observable<UniversalResponse>{
        return service.updateCheckBox(CheckBoxRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), list)).compose(applySchedulers())
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

    fun setStatus(status: Int): Observable<UniversalResponse>{
        return service.setStatus(StatusRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), status)).compose(applySchedulers())
    }

    fun udpateSHKWps(shk: String): Observable<UniversalResponse>{
        return service.updateSHKWps(UpdateShkWpsRequest(SPHelper.getNameTask(),SPHelper.getArticuleWork().toInt(),shk)).compose(applySchedulers())
    }

    fun getLdu(artikul: Int, name: String): Observable<ChooseOpResponse>{
        return service.getLDU(artikul, name).compose(applySchedulers())
    }

    fun getPackingData(status: Int): Observable<ArticlesResponse> {
        return service.getTasksInWork(SPHelper.getNameTask(), status).compose(applySchedulers())
    }

    fun getPackingDataFull(): Observable<ArticlesResponse> {
        return service.getTasksInWorkFull(SPHelper.getNameTask()).compose(applySchedulers())
    }


     suspend fun getDataWB(): WBDataResponse {
        return service.getDataWb(SPHelper.getNameTask())
    }

    suspend fun calncelTask(reason: String, comment: String): UniversalResponse{
        return  service.cancelTask(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), comment, reason)
    }

    suspend fun addSrokForWB(data: String): UniversalResponse{
        return service.addSrokGodnosti(SrokGodnostiRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), data))
    }

    // Получение данных задачи
    fun getTaskData(): Observable<ChooseOpResponse> {
        return service.getLDU(SPHelper.getArticuleWork().toInt(),SPHelper.getNameTask())
    }

    suspend fun endStatusForViewModel(): UniversalResponse{
        return service.endStatusForViewModel(EndStatusRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork(), 2, getTime(), SPHelper.getNameEmployer(), 1))
    }

     fun calncelTaskNorm(reason: String, comment: String): Observable<UniversalResponse>{
        return  service.cancelTaskNorm(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), comment, reason).compose(applySchedulers())
    }
}
