package com.example.timetrekerforandroid.model

import com.example.timetrekerforandroid.network.BaseDataProvider
import com.example.timetrekerforandroid.network.Const
import com.example.timetrekerforandroid.network.request.DownloadExcelRequest
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.AuthResponse
import com.example.timetrekerforandroid.network.response.NameDBResponse
import com.example.timetrekerforandroid.network.response.NameFilesInWaitResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import rx.Observable

class InformationModel: BaseDataProvider() {
    fun getDataInWork(): Observable<NameDBResponse> {
        return service.getDataInWork().compose(applySchedulers())
    }
    fun getDataInWait(): Observable<NameFilesInWaitResponse>{
        return service.getDataInWait(Const.HOST, Const.PORT, Const.USERNAME, Const.PASSWORD).compose(applySchedulers())
    }

    fun getArtikules(name: String): Observable<ArticlesResponse>{
        return  service.getTasksInWork(name).compose(applySchedulers())
    }

    fun getAuthUser(id: String): Observable<AuthResponse>{
        return service.authUser(id).compose(applySchedulers())
    }

    fun downloadExcel(name: String ): Observable<UniversalResponse>{
        return downloadService.downloadExcel(DownloadExcelRequest(name, Const.HOST,  Const.PORT, Const.USERNAME, Const.PASSWORD)).compose(applySchedulers())
    }
}