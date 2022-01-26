package fgd.showroom.logic.network

import fgd.showroom.logic.model.CommonResponse
import fgd.showroom.logic.model.DevStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SvsMmcService {
    @GET(".")
    fun isConnected(): Call<String>

    @GET("{action}")
    fun svsMmcRequest(@Path("action") action: String): Call<CommonResponse>

    @GET("{action}")
    fun svsMmcRequest(@Path("action") action: String, @Query("type") type: Int): Call<CommonResponse>

    @GET("listtypestate")
    fun listTypeState(@Query("devtype") devtype: String): Call<List<DevStatus>>

}