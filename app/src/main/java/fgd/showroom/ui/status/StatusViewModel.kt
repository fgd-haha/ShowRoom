package fgd.showroom.ui.status

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import fgd.showroom.logic.Repository

class StatusViewModel : ViewModel() {

    private val actionLiveData = MutableLiveData<String>()
    private val monitor = MutableLiveData<String>()

    var isConnected = Transformations.switchMap(monitor) { _ ->
        Repository.isConnected()
    }

    val commonResponse = Transformations.switchMap(actionLiveData) { action ->
        Repository.svsMmcRequest(action)
    }

    fun svsMmcRequest(action: String) {
        actionLiveData.value = action
    }

    fun saveUrl(Url: String) = Repository.saveUrl(Url)

    fun getSavedUrl() = Repository.getSavedUrl()

    suspend fun monitorServiceStatus() {
        while (true) {
            sleep(500)
            monitor.postValue("start")
        }
    }
}