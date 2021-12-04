package fgd.showroom.logic

import android.util.Log
import androidx.lifecycle.liveData
import fgd.showroom.logic.dao.ServerUrlDao
import fgd.showroom.logic.network.ShowRoomNetwork
import kotlinx.coroutines.Dispatchers
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
//    "powerall" to "总开机",
//    "shutall" to "总关机",
)

object Repository {
    fun isConnected() = fire(Dispatchers.IO) {
        val isConnected = ShowRoomNetwork.isConnected()
        val result = if (isConnected == "Hello,World!") {
            Log.i("Repository isConnected", isConnected)
            Result.success(true)
        } else {
            Log.i("Repository isConnected", isConnected)
            Result.success(false)
        }
        result
    }

    fun svsMmcRequest(action: String) = fire(Dispatchers.IO) {
        val commonResponse = ShowRoomNetwork.svsMmcRequest(action)
        if (commonResponse.success == 1) {
            Result.success("${svsMmcActionMap[action]}完毕")
        } else {
            Result.failure(RuntimeException("${svsMmcActionMap[action]}失败"))
        }
    }

    fun saveUrl(Url: String) = ServerUrlDao.saveUrl(Url)

    fun getSavedUrl() = ServerUrlDao.getSavedUrl()

    fun isUrlSaved() = ServerUrlDao.isUrlSaved()

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}