package com.example.timetrekerforandroid.model


import com.example.timetrekerforandroid.network.BaseDataProvider
import com.example.timetrekerforandroid.network.request.SearchShkInExcelRequest
import com.example.timetrekerforandroid.network.request.UpdateShkRequest
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.ShkInDbResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.util.SPHelper
import rx.Observable

class ScanModel: BaseDataProvider() {

    //todo шаг  №1, если находит тут, значит просто перезаписываем в артикул точный шк, им переходим к заполннеию данных
    fun findShkInExcel(name: String, shk: String): Observable<ArticlesResponse>{
        return service.getShk(name, shk).compose(applySchedulers());
    }

    //todo шаг №2, если не найлено в эксель тогда идем искать в  бд, сначала по номеру шк, потом по артикулу
    fun findShkInDB(shk: String): Observable<ShkInDbResponse>{
        return service.searchInDbForShk(shk).compose(applySchedulers());
    }
    fun findShkInDBWithArticule(articul: String): Observable<ShkInDbResponse>{
        return service.searchInDbForArticule(articul).compose(applySchedulers())
    }

    fun updateShk(shk: String): Observable<UniversalResponse>{
        return service.updateShk(UpdateShkRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork(), shk)).compose(applySchedulers())
    }


}