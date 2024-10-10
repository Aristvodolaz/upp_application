package com.example.timetrekerforandroid.model

import com.example.timetrekerforandroid.network.ApiService
import com.example.timetrekerforandroid.network.request.PrivyazkaRequest
import com.example.timetrekerforandroid.network.request.SrokGodnostiRequest
import com.example.timetrekerforandroid.network.response.PrivyazkaResponse
import com.example.timetrekerforandroid.network.response.SkladsResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.network.response.ZapisResponse
import com.example.timetrekerforandroid.util.SPHelper

class WpsModel(private val apiService: ApiService) {
    suspend fun addZapisWithWps(shkWps: String, kol_vo: Int, pallet: String): PrivyazkaResponse {
        return apiService.addZapisInPrivyzka(PrivyazkaRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(),kol_vo, pallet, shkWps))
    }

    suspend fun getListZapis(): ZapisResponse{
        return apiService.getZapisList(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt())
    }

    suspend fun addSrokGodnosti(srok: String): UniversalResponse{
        return apiService.addSrokGodnosti(SrokGodnostiRequest(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), srok))
    }

    suspend fun endStatusWb(): PrivyazkaResponse{
        return apiService.endStatusWb(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt())
    }

    suspend fun updateStatus(pallet: String, kolvo:Int, shk: String): PrivyazkaResponse{
        return apiService.updateData(SPHelper.getNameTask(), SPHelper.getArticuleWork().toInt(), pallet, kolvo, shk)
    }

    suspend fun getSklads(): SkladsResponse{
        return apiService.getSklads()
    }

    suspend fun  checkWps(shk: String): UniversalResponse{
        return apiService.checkWps(SPHelper.getNameTask(), shk)
    }

}