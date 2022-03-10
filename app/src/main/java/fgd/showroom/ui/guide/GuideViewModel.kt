package fgd.showroom.ui.guide

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fgd.showroom.logic.Repository
import fgd.showroom.logic.model.Step
import kotlinx.coroutines.launch

class GuideViewModel : ViewModel() {

    private val _stepList = MutableLiveData<MutableList<Step>>().apply { viewModelScope.launch { value = Repository.getStepList() } }

    val stepList = _stepList
}