package fgd.showroom.ui.status.computers


import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import fgd.showroom.logic.Repository

class ComputersViewModel : ViewModel() {
    //    全部开机 全部关机
    private val actionLiveData = MutableLiveData<String>()
    var actionResult = Transformations.switchMap(actionLiveData) {
        Repository.svsMmcRequest(it, 10)
    }

    fun powerAllRequest() = actionLiveData.postValue("powerall")
    fun shutAllRequest() = actionLiveData.postValue("shutall")


    //    刷新计算机状态
    private val monitor = MediatorLiveData<String>()

    var computersStatus = Transformations.switchMap(monitor) {
        Repository.listTypeState(devtype = "10")
    }

    fun monitorComputersStatus() = monitor.postValue("start ${System.currentTimeMillis()}")
}