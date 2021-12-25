package fgd.showroom.logic

import android.util.Log
import androidx.lifecycle.liveData
import fgd.showroom.logic.dao.ServerUrlDao
import fgd.showroom.logic.network.ShowRoomNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext

val svsMmcActionMap = mapOf(
    "poweron" to "总开机",
    "poweroff" to "总关机",
    "alllighton" to "开灯",
    "alllightoff" to "关灯",
    "emergence" to "启动应急模式",
    "reset" to "复位",
    "opendoor" to "开门",
    "closedoor" to "关门",
    "powerall" to "全部开机",
    "shutall" to "全部关机",
)

object Repository {
    fun isConnected() = loop(Dispatchers.IO) {
        val isConnected = ShowRoomNetwork.isConnected()
        val result = if (isConnected == "Hello,World!") {
            Log.i("Repository isConnected", isConnected)
            Result.success(true)
        } else {
            Log.i("Repository isConnected", isConnected)
            Result.failure(RuntimeException("连接服务器失败, $isConnected"))
        }
        result
    }

    fun listTypeState(devtype: String) = loop(Dispatchers.IO) { Result.success(ShowRoomNetwork.listTypeState(devtype)) }

    fun svsMmcRequest(action: String, type: Int = -1) = fire(Dispatchers.IO) {
        val commonResponse = ShowRoomNetwork.svsMmcRequest(action, type)
        if (commonResponse.success == 1) {
            Result.success("${svsMmcActionMap[action]}完毕")
        } else {
            Result.failure(RuntimeException("${svsMmcActionMap[action]}失败"))
        }
    }

    fun saveUrl(Url: String): Boolean = ServerUrlDao.saveUrl(Url)

    fun getSavedUrl() = ServerUrlDao.getSavedUrl()

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Log.d("fire", e.toString())
                Result.failure<T>(RuntimeException("请求失败, ${System.currentTimeMillis()}"))
            }
            emit(result)
        }

    private fun <T> loop(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            while (true) {
                val result = try {
                    block()
                } catch (e: Exception) {
                    Log.d("loop", e.toString())
                    Result.failure<T>(RuntimeException(e.toString()))
                }
                emit(result)
                delay(1000)
            }
        }


}