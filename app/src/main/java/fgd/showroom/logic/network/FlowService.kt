package fgd.showroom.logic.network


import fgd.showroom.logic.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlowService {

    @GET("actionlist")
    fun getActionList(): Call<List<Action>>

    @GET("typelist")
    fun getDevTypeList(): Call<List<DevType>>

    @GET("devicelist")
    fun getDevList(): Call<List<Device>>

    @GET("getsteplist")
    fun getStepList(): Call<List<Step>>

    @GET("getstepinfo")
    fun getStepAction(@Query("step") step: Int): Call<List<StepAction>>

    @GET("execute")
    fun execute(
        @Query("action") action: Int,
        @Query("devtype") devtype: Int,
        @Query("devno") devno: Int,
        @Query("intv") intv: Int,
        @Query("filename") filename: String
    ): Call<CommonResponse>


}