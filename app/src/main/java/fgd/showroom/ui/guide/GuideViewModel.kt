package fgd.showroom.ui.guide

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fgd.showroom.logic.Repository
import fgd.showroom.logic.model.Step
import kotlinx.coroutines.launch

class GuideViewModel : ViewModel() {

    private val _stepList = MutableLiveData<MutableList<Step>>().apply { viewModelScope.launch { value = Repository.getStepList() } }
    private val _gotostepRp = MutableLiveData<Map<String, Any>>()
    private val modStepPosLiveData = MutableLiveData<List<Int>>()

    val stepList = _stepList
    val gotostepRp = _gotostepRp

    fun gotostep(stepno: Int) {
        viewModelScope.launch { _gotostepRp.value = Repository.gotostep(stepno) }
    }

    val modStepPosRp = Transformations.switchMap(modStepPosLiveData) { l -> Repository.modStepPos(l[0], l[1], l[2]) }
    fun modStepPos(stepno: Int, posx: Int, posy: Int) {
        modStepPosLiveData.value = listOf(stepno, posx, posy)
    }
}