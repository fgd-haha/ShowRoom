package fgd.showroom.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import fgd.showroom.logic.model.CommonResponse
import fgd.showroom.ui.debug.DebugViewModel
import fgd.showroom.ui.setting.SettingViewModel


fun observeCommonRpInfo(owner: LifecycleOwner, context: Context, commonRp: LiveData<Result<String>>) {
    commonRp.observe(owner) { result ->
        Toast.makeText(
            context,
            result.getOrElse { result.exceptionOrNull().toString() },
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun observeStepRpInfo(owner: LifecycleOwner, context: Context, stepRp: LiveData<Result<CommonResponse>>, vm: SettingViewModel) {
    stepRp.observe(owner) { result ->
        if (result.isSuccess) {
            Toast.makeText(
                context,
                result.getOrDefault(CommonResponse(1, "成功")).message,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                result.exceptionOrNull().toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
        vm.refreshStepList()
    }
}

fun observeWizardRpInfo(owner: LifecycleOwner, context: Context, wizardRp: LiveData<Result<CommonResponse>>, vm: SettingViewModel) {
    wizardRp.observe(owner) { result ->
        if (result.isSuccess) {
            Toast.makeText(
                context,
                result.getOrDefault(CommonResponse(1, "成功")).message,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                result.exceptionOrNull().toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
        vm.refreshStepAction(-1)
    }
}

fun observeDebugWizardRpInfo(owner: LifecycleOwner, context: Context, wizardRp: LiveData<Result<CommonResponse>>, vm: DebugViewModel) {
    wizardRp.observe(owner) { result ->
        if (result.isSuccess) {
            Toast.makeText(
                context,
                result.getOrDefault(CommonResponse(1, "成功")).message,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                result.exceptionOrNull().toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}