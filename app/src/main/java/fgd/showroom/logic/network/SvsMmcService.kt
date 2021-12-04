package fgd.showroom.logic.network

import fgd.showroom.logic.model.CommonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SvsMmcService {
    @GET("{action}")
    fun svsMmcRequest(@Path("action") action: String): Call<CommonResponse>

    @GET(".")
    fun isConnected(): Call<String>
}