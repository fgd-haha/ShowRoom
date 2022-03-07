package fgd.showroom.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fgd.showroom.logic.Repository
import fgd.showroom.logic.model.DevType
import fgd.showroom.logic.model.Device
import fgd.showroom.logic.model.Step
import fgd.showroom.logic.model.StepAction
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    private val addStepLiveData = MutableLiveData<Step>()
    private val deleteStepLiveData = MutableLiveData<Step>()
    private val stepnoLiveData = MutableLiveData<Int>()
    private val deleWizardLiveData = MutableLiveData<Int>()
    private val saveWizardLiveData = MutableLiveData<StepAction>()
    private val _stepList = MutableLiveData<MutableList<Step>>().apply { viewModelScope.launch { value = Repository.getStepList() } }
    val devTypeList = MutableLiveData<MutableList<DevType>>().apply { viewModelScope.launch { value = Repository.getDevTypeList() } }
    val devList = MutableLiveData<MutableList<Device>>().apply { viewModelScope.launch { value = Repository.getDevList() } }

    val stepList = _stepList
    var nowStepno = -1

    fun refreshStepList() = viewModelScope.launch { _stepList.value = Repository.getStepList() }

    val stepActionListLiveData = Transformations.switchMap(stepnoLiveData) { step -> Repository.getStepAction(step) }

    fun refreshStepAction(stepno: Int) {
        if (stepno >= 0) nowStepno = stepno
        if (nowStepno < 0) {
            nowStepno = if (_stepList.value != null && _stepList.value!!.size > 0) {
                _stepList.value!![0].stepno
            } else 1
        }
        stepnoLiveData.postValue(nowStepno)
    }

    val addStepRp = Transformations.switchMap(addStepLiveData) { step -> Repository.addStep(step.stepno, step.stepname) }
    fun addStep(stepno: Int, stepname: String) {
        addStepLiveData.value = Step(stepno, stepname, 0, 0)
    }

    val deleteStepRp = Transformations.switchMap(deleteStepLiveData) { step -> Repository.deleteStep(step.stepno) }
    fun deleteStep(stepno: Int) {
        deleteStepLiveData.value = Step(stepno, "", 0, 0)
    }

    fun modStepPos(stepno: Int, posx: Int, posy: Int) = Repository.modStepPos(stepno, posx, posy)

    val saveWizardRp = Transformations.switchMap(saveWizardLiveData) { stepAction -> Repository.saveWizard(stepAction) }
    fun saveWizard(stepAction: StepAction) {
        saveWizardLiveData.value = stepAction
    }

    val deleWizardRp = Transformations.switchMap(deleWizardLiveData) { id -> Repository.deleWizard(id) }
    fun deleWizard(id: Int) {
        deleWizardLiveData.value = id
    }

    fun getDevTypeName(typeid: Int): String {
        val typeList = this.devTypeList.value
        if (!typeList.isNullOrEmpty()) {
            val devType = typeList.find { it.typeid == typeid }
            if (devType != null) {
                return devType.typename
            }
        }
        return "机器"
    }

    fun getDevName(typeid: Int, devno: Int): String {
        val devList = this.devList.value
        if (!devList.isNullOrEmpty()) {
            val dev = devList.find { it.typeid == typeid && it.devno == devno }
            if (dev != null) {
                return dev.devname
            }
        }
        return "设备"
    }
}