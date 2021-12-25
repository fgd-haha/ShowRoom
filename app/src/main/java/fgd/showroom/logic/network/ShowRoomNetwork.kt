package fgd.showroom.logic.network

import android.util.Log
import fgd.showroom.logic.model.CommonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ShowRoomNetwork {
    private val TAG = "ShowRoomNetwork"

    suspend fun isConnected() = ServiceCreator.create(SvsMmcService::class.java, jsonConverter = false, timeout = "short").isConnected().await()

    suspend fun listTypeState(devtype: String) = ServiceCreator.create(SvsMmcService::class.java, timeout = "short").listTypeState(devtype).await()

    suspend fun svsMmcRequest(action: String, type: Int = -1): CommonResponse {
        return if (type == -1) ServiceCreator.create(SvsMmcService::class.java).svsMmcRequest(action).await()
        else ServiceCreator.create(SvsMmcService::class.java).svsMmcRequest(action, type).await()
    }

    suspend fun powerAllRequest(type: Int) = ServiceCreator.create(SvsMmcService::class.java).powerAllRequest(type).await()
    suspend fun shutAllRequest(type: Int) = ServiceCreator.create(SvsMmcService::class.java).shutAllRequest(type).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                        Log.d(TAG, "response body is null")
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    Log.d(TAG, "request failed!")
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}