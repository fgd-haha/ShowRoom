package fgd.showroom.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData


fun observeCommonRpInfo(owner: LifecycleOwner, context: Context, commonRp: LiveData<Result<String>>) {
    commonRp.observe(owner, { result ->
        Toast.makeText(
            context,
            result.getOrElse { result.exceptionOrNull().toString() },
            Toast.LENGTH_SHORT
        ).show()
    })
}