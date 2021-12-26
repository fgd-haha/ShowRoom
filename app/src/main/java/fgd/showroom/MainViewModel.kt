package fgd.showroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import fgd.showroom.logic.Repository

class MainViewModel : ViewModel() {
    private val actionLiveData = MutableLiveData<String>()
    var actionResult = Transformations.switchMap(actionLiveData) {
        Repository.svsMmcRequest(it)
    }

    fun allLightOn() = actionLiveData.postValue("alllighton")
    fun allLightOff() = actionLiveData.postValue("alllightoff")
}