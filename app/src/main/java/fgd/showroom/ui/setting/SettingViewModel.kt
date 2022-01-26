package fgd.showroom.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fgd.showroom.logic.Repository
import fgd.showroom.logic.model.Step
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    private val stepLiveData = MutableLiveData<Int>()
    private val _stepList = MutableLiveData<MutableList<Step>>().apply { viewModelScope.launch { value = Repository.getStepList() } }
    val stepList = _stepList

    fun refreshStepList() = viewModelScope.launch { _stepList.value = Repository.getStepList() }

    val stepActionListLiveData = Transformations.switchMap(stepLiveData) { step ->
        Repository.getStepAction(step)
    }

    fun refreshStepAction(step: Int) {
        stepLiveData.postValue(step)
    }
}