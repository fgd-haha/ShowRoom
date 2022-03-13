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

    @GET("addstep")
    fun addStep(@Query("stepno") stepno: Int, @Query("stepname") stepname: String): Call<CommonResponse>

    @GET("delestep")
    fun deleteStep(@Query("stepno") stepno: Int): Call<CommonResponse>

    @GET("copystep")
    fun copystep(@Query("srcstep") srcstep: Int, @Query("dststep") dststep: Int): Call<CommonResponse>

    @GET("gotostep")
    fun gotostep(@Query("step") stepno: Int): Call<CommonResponse>

    @GET("modsteppos")
    fun modStepPos(@Query("stepno") stepno: Int, @Query("posx") posx: Int, @Query("posy") posy: Int): Call<CommonResponse>

    @GET("getstepinfo")
    fun getStepAction(@Query("step") step: Int): Call<List<StepAction>>

    @GET("savewizard")
    fun saveWizard(
        @Query("id") id: Int?,
        @Query("stepno") stepno: Int,
        @Query("stepidx") stepidx: Int,
        @Query("devtype") devtype: Int,
        @Query("action") action: Int,
        @Query("devno") devno: Int,
        @Query("filename") filename: String,
        @Query("intv") intv: Int
    ): Call<CommonResponse>

    @GET("delewizard")
    fun deleWizard(@Query("id") id: Int): Call<CommonResponse>

    @GET("execute")
    fun execute(
        @Query("action") action: Int,
        @Query("devtype") devtype: Int,
        @Query("devno") devno: Int,
        @Query("intv") intv: Int,
        @Query("filename") filename: String
    ): Call<CommonResponse>


}