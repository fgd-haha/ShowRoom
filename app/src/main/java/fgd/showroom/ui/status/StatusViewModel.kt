package fgd.showroom.ui.status

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import fgd.showroom.logic.Repository

class StatusViewModel : ViewModel() {

    private val actionLiveData = MutableLiveData<String>()
    private val monitor = MediatorLiveData<String>()

    var isConnected = Transformations.switchMap(monitor) {
        Repository.isConnected()
    }

    val commonResponse = Transformations.switchMap(actionLiveData) { action ->
        Repository.svsMmcRequest(action)
    }

    var computersStatus = Transformations.switchMap(monitor) {
        Repository.listTypeState(devtype = "10")
    }

    fun svsMmcRequest(action: String) {
        actionLiveData.value = action
    }

    fun saveUrl(Url: String): Boolean = Repository.saveUrl(Url)

    fun getSavedUrl() = Repository.getSavedUrl()

    fun monitorServiceStatus() = monitor.postValue("start ${System.currentTimeMillis()}")
}