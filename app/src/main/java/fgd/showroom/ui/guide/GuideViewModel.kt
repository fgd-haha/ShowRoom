package fgd.showroom.ui.guide

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fgd.showroom.logic.Repository
import fgd.showroom.logic.model.Step
import kotlinx.coroutines.launch

class GuideViewModel : ViewModel() {

    private val _stepList = MutableLiveData<MutableList<Step>>().apply { viewModelScope.launch { value = Repository.getStepList() } }
    private val _gotostepRp = MutableLiveData<Map<String, Any>>()

    val stepList = _stepList
    val gotostepRp = _gotostepRp

    fun gotostep(stepno: Int) {
        viewModelScope.launch { _gotostepRp.value = Repository.gotostep(stepno) }
    }
}