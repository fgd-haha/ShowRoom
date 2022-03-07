package fgd.showroom.ui.debug

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fgd.showroom.logic.Repository
import fgd.showroom.logic.model.*
import kotlinx.coroutines.launch

class DebugViewModel : ViewModel() {
    private val actionLiveData = MutableLiveData<Map<String, Any>>()
    private val _actionList = MutableLiveData<MutableList<Action>>().apply { viewModelScope.launch { value = Repository.getActionList() } }
    private val _devList = MutableLiveData<MutableList<Device>>().apply { viewModelScope.launch { value = Repository.getDevList() } }
    private val _devTypeList = MutableLiveData<MutableList<DevType>>().apply { viewModelScope.launch { value = Repository.getDevTypeList() } }
    private val _fileList = MutableLiveData<MutableList<PlayFile>>().apply { viewModelScope.launch { value = Repository.getFileList() } }
    private val saveWizardLiveData = MutableLiveData<StepAction>()
    val actionList = _actionList
    val devList = _devList
    val devTypeList = _devTypeList
    val fileList = _fileList

    val commonResponse = Transformations.switchMap(actionLiveData) { args ->
        Repository.execute(args["action"] as Int, args["devtype"] as Int, args["devno"] as Int, args["intv"] as Int, args["filename"] as String)
    }

    fun execute(args: Map<String, Any>) {
        actionLiveData.postValue(args)
    }

    val saveWizardRp = Transformations.switchMap(saveWizardLiveData) { stepAction -> Repository.saveWizard(stepAction) }
    fun saveWizard(stepAction: StepAction) {
        saveWizardLiveData.value = stepAction
    }
}