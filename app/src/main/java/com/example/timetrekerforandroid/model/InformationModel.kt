package com.example.timetrekerforandroid.model

import com.example.timetrekerforandroid.network.BaseDataProvider
import com.example.timetrekerforandroid.network.Const
import com.example.timetrekerforandroid.network.request.DownloadExcelRequest
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.AuthResponse
import com.example.timetrekerforandroid.network.response.DBNameResponse
import com.example.timetrekerforandroid.network.response.NameDBResponse
import com.example.timetrekerforandroid.network.response.NameFilesInWaitResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.util.SPHelper
import rx.Observable

class InformationModel: BaseDataProvider() {
    suspend fun getDataInWork(): DBNameResponse {
        return service.getDataInWork(SPHelper.getSklad())
    }
    fun getDataInWait(): Observable<NameFilesInWaitResponse>{
        return service.getDataInWait(Const.HOST, Const.PORT, Const.USERNAME, Const.PASSWORD).compose(applySchedulers())
    }

    fun getArtikules(name: String, status: Int): Observable<ArticlesResponse>{
        return  service.getTasksInWork(name, status).compose(applySchedulers())
    }

    fun getAuthUser(id: String): Observable<AuthResponse>{
        return service.authUser(id).compose(applySchedulers())
    }

    fun downloadExcel(name: String ): Observable<UniversalResponse>{
        return downloadService.downloadExcel(DownloadExcelRequest(name, Const.HOST,  Const.PORT, Const.USERNAME, Const.PASSWORD)).compose(applySchedulers())
    }
}