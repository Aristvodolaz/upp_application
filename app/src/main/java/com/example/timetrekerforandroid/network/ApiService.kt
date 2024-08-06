package com.example.timetrekerforandroid.network


import com.example.timetrekerforandroid.network.request.CheckBoxRequest
import com.example.timetrekerforandroid.network.request.DownloadExcelRequest
import com.example.timetrekerforandroid.network.request.DuplicateRequest
import com.example.timetrekerforandroid.network.request.EndStatusRequest
import com.example.timetrekerforandroid.network.request.FinishedRequest
import com.example.timetrekerforandroid.network.request.UpdateShkRequest
import com.example.timetrekerforandroid.network.request.UpdateSrokGodnostiRequest
import com.example.timetrekerforandroid.network.request.UpdateStatusRequest
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.AuthResponse
import com.example.timetrekerforandroid.network.response.FinishedResponse
import com.example.timetrekerforandroid.network.response.NameDBResponse
import com.example.timetrekerforandroid.network.response.NameFilesInWaitResponse
import com.example.timetrekerforandroid.network.response.ShkInDbResponse
import com.example.timetrekerforandroid.network.response.UniversalResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import rx.Observable

interface ApiService {

    //todo получения названия эксель файла для дальнейшей работы в бд
    @GET(Const.GET_NAME_FOR_WAIT) fun getDataInWait(@Query ("host")  host: String, @Query("port") port: Int, @Query("username") username: String, @Query("password") password: String): Observable<NameFilesInWaitResponse>
    @GET(Const.GET_NAME_FOR_WORK) fun getDataInWork(): Observable<NameDBResponse>


    @GET(Const.TASKS_WORK) fun getTasksInWork(@Query("taskNumber") taskName: String): Observable<ArticlesResponse>
    @GET(Const.SEARCH_SHK) fun getShk(@Query("taskName") nameTask: String, @Query("shk") shk: String): Observable<ArticlesResponse>
    @GET(Const.SEARCH_ARTIKUL_TASKS) fun getArticulTask(@Query("taskName") taskName: String, @Query("articul") articul: String): Observable<List<ArticlesResponse.Articuls>>
    @PUT(Const.UPDATE_STATUS) fun changeStatus(@Body data: UpdateStatusRequest): Observable<UniversalResponse>
    @PUT(Const.END_STATUS) fun endStatus(@Body data: EndStatusRequest): Observable<UniversalResponse>

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
}