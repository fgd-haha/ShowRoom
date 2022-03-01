package fgd.showroom.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fgd.showroom.logic.Repository
import fgd.showroom.logic.model.Step
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    private val addStepLiveData = MutableLiveData<Step>()
    private val deleteStepLiveData = MutableLiveData<Step>()
    private val stepnoLiveData = MutableLiveData<Int>()
    private val _stepList = MutableLiveData<MutableList<Step>>().apply { viewModelScope.launch { value = Repository.getStepList() } }
    val stepList = _stepList

    fun refreshStepList() = viewModelScope.launch { _stepList.value = Repository.getStepList() }

    val stepActionListLiveData = Transformations.switchMap(stepnoLiveData) { step -> Repository.getStepAction(step) }

    fun refreshStepAction(step: Int) = stepnoLiveData.postValue(step)

    val addStepRp = Transformations.switchMap(addStepLiveData) { step -> Repository.addStep(step.stepno, step.stepname) }
    fun addStep(stepno: Int, stepname: String) {
        addStepLiveData.value = Step(stepno, stepname, 0, 0)
    }

    val deleteStepRp = Transformations.switchMap(deleteStepLiveData) { step -> Repository.deleteStep(step.stepno) }
    fun deleteStep(stepno: Int) {
        deleteStepLiveData.value = Step(stepno, "", 0, 0)
    }

    fun modStepPos(stepno: Int, posx: Int, posy: Int) = Repository.modStepPos(stepno, posx, posy)
}