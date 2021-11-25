package fgd.showroom.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is status Fragment"
    }
    var switch = true
    var text: LiveData<String> = _text
}