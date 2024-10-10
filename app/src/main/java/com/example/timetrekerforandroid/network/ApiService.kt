package com.example.timetrekerforandroid.network


import com.example.timetrekerforandroid.network.request.CheckBoxRequest
import com.example.timetrekerforandroid.network.request.ChooseOpRequest
import com.example.timetrekerforandroid.network.request.DownloadExcelRequest
import com.example.timetrekerforandroid.network.request.DuplicateRequest
import com.example.timetrekerforandroid.network.request.EndStatusRequest
import com.example.timetrekerforandroid.network.request.FinishedRequest
import com.example.timetrekerforandroid.network.request.PrivyazkaRequest
import com.example.timetrekerforandroid.network.request.SrokGodnostiRequest
import com.example.timetrekerforandroid.network.request.StatusRequest
import com.example.timetrekerforandroid.network.request.UpdateShkRequest
import com.example.timetrekerforandroid.network.request.UpdateShkWpsRequest
import com.example.timetrekerforandroid.network.request.UpdateSrokGodnostiRequest
import com.example.timetrekerforandroid.network.request.UpdateStatusRequest
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.AuthResponse
import com.example.timetrekerforandroid.network.response.ChooseOpResponse
import com.example.timetrekerforandroid.network.response.DBNameResponse
import com.example.timetrekerforandroid.network.response.FinishedResponse
import com.example.timetrekerforandroid.network.response.NameFilesInWaitResponse
import com.example.timetrekerforandroid.network.response.PrivyazkaResponse
import com.example.timetrekerforandroid.network.response.ShkInDbResponse
import com.example.timetrekerforandroid.network.response.SkladsResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import com.example.timetrekerforandroid.network.response.WBDataResponse
import com.example.timetrekerforandroid.network.response.ZapisResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import rx.Observable

interface ApiService {

    //todo получения названия эксель файла для дальнейшей работы в бд
    @GET(Const.GET_NAME_FOR_WAIT) fun getDataInWait(@Query ("host")  host: String, @Query("port") port: Int, @Query("username") username: String, @Query("password") password: String): Observable<NameFilesInWaitResponse>
    @GET(Const.GET_NAME_FOR_WORK) suspend fun getDataInWork(@Query("sk") sk: String): DBNameResponse
    @POST(Const.CANCEL_TASK) suspend  fun cancelTask(@Query("taskName") name: String, @Query("articul") articule: Int, @Query("comment") comment: String, @Query("reason") reason: String): UniversalResponse

    @GET(Const.TASKS_WORK) fun getTasksInWorkFull(@Query("taskNumber") taskName: String): Observable<ArticlesResponse>

    @GET(Const.GET_TASK_WITH_STATUS) fun getTasksInWork(@Query("taskNumber") taskName: String, @Query("status") status: Int): Observable<ArticlesResponse>
    @GET(Const.SEARCH_SHK) fun getShk(@Query("taskName") nameTask: String, @Query("shk") shk: String): Observable<ArticlesResponse>
    @GET(Const.SEARCH_ARTIKUL_TASKS) fun getArticulTask(@Query("taskName") taskName: String, @Query("articul") articul: String): Observable<List<ArticlesResponse.Articuls>>
    @PUT(Const.UPDATE_STATUS) fun changeStatus(@Body data: UpdateStatusRequest): Observable<UniversalResponse>
    @PUT(Const.END_STATUS) fun endStatus(@Body data: EndStatusRequest): Observable<UniversalResponse>
    @POST(Const.SET_STATUS) fun setStatus(@Body data: StatusRequest): Observable<UniversalResponse>

    @POST(Const.UPDATE_CHECK_BOX) fun updateCheckBox(@Body data: CheckBoxRequest): Observable<UniversalResponse>
    @POST(Const.FINISHED_REQUEST) fun finishedSend(@Body data: FinishedRequest): Observable<FinishedResponse>

    //todo для поиска в бд по артикулу или по шк
    @GET(Const.SEARCH_IN_DB_FOR_ARTICLE_OR_SHK) fun searchInDbForShk(@Query("shk") shk: String): Observable<ShkInDbResponse>
    @GET(Const.SEARCH_IN_DB_FOR_ARTICLE_OR_SHK) fun searchInDbForArticule(@Query("articul") articule: String): Observable<ShkInDbResponse>


    @GET(Const.AUTH) fun authUser(@Query("id") id: String): Observable<AuthResponse>
    @POST(Const.UPDATE_SHK) fun updateShk(@Body data: UpdateShkRequest): Observable<UniversalResponse>

    @POST(Const.DUPLICATE) fun getDuplicate(@Body data: DuplicateRequest): Observable<UniversalResponse>
    @POST(Const.DOWNLOAD_FROM_SERVER_EXCEL) fun downloadExcel(@Body data: DownloadExcelRequest): Observable<UniversalResponse>

    @POST(Const.SEND_SROK) fun sendSrokGodnosti(@Body data: UpdateSrokGodnostiRequest) : Observable<UniversalResponse>
    @POST(Const.UPDATE_SHK_WPS) fun updateSHKWps(@Body data: UpdateShkWpsRequest): Observable<UniversalResponse>
    @GET(Const.GET_RECORD_BY_SHK) fun getLDU(@Query("artikul") articul: Int , @Query("name") name: String): Observable<ChooseOpResponse>

    @POST(Const.PRIVYAZKA)
    suspend fun addZapisInPrivyzka(@Body data: PrivyazkaRequest): PrivyazkaResponse

    @GET(Const.GET_ZAPIS_LIST)
    suspend fun getZapisList(@Query("name") name: String, @Query("artikul") artikul: Int): ZapisResponse
    @POST(Const.ADD_SROK_GODNOSTI)
    suspend fun addSrokGodnosti(@Body data: SrokGodnostiRequest): UniversalResponse

    @POST(Const.END_STATUS_WB)
    suspend fun endStatusWb(@Query("name") name: String, @Query("artikul") artikul: Int): PrivyazkaResponse


    @GET(Const.GET_DATA_FOR_WB)
    suspend fun getDataWb(@Query("name") name: String): WBDataResponse
    @PUT(Const.UPDATE_INFO_FOR_WB)
    suspend fun updateData(@Query("name") name: String, @Query("artikul") artikul: Int, @Query("pallet") pallet: String, @Query("kolvo") kolvo: Int, @Query("shk") shk: String): PrivyazkaResponse

    @GET(Const.TASKS_WORK) //todo надо поменять и добавить обработку на бэк части
    fun getPackingData(@Query("taskNumber") name: String): ArticlesResponse

    @GET(Const.SKLADS)
    suspend fun getSklads():SkladsResponse

    @PUT(Const.CHANGE_OP)
    fun changeOP(
        @Query("taskName") id: String,
        @Query("articul") artikul: Int,
        @Body data: ChooseOpRequest
    ): Call<UniversalResponse>

    @GET(Const.GET_LDU) fun taskData(@Query("taskName") taskName: String, @Query("artikul") artikul: Int): ChooseOpResponse


    @GET(Const.CHECK_WPS) suspend fun checkWps(@Query("name") taskName: String, @Query("shk") shk: String): UniversalResponse
}