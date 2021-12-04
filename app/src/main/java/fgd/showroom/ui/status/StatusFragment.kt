package fgd.showroom.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fgd.showroom.R
import fgd.showroom.databinding.FragmentStatusBinding
import fgd.showroom.databinding.TextInputServerSettingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusFragment : Fragment() {

    private lateinit var statusViewModel: StatusViewModel
    private var _binding: FragmentStatusBinding? = null
    private var _sbinding: TextInputServerSettingBinding? = null

    private val binding get() = _binding!!
    private val sbinding get() = _sbinding!!
    private val TAG = "StatusFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        statusViewModel =
            ViewModelProvider(this).get(StatusViewModel::class.java)

        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        _sbinding = TextInputServerSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        点击上面与下面按键发送请求
        binding.btnPowerOn.setOnClickListener { statusViewModel.svsMmcRequest("poweron") }
        binding.btnPowerOff.setOnClickListener { statusViewModel.svsMmcRequest("poweroff") }
        binding.btnReset.setOnClickListener { statusViewModel.svsMmcRequest("reset") }
        binding.btnEmergence.setOnClickListener { statusViewModel.svsMmcRequest("emergence") }
        binding.btnOpenFirstFloorDoor.setOnClickListener { statusViewModel.svsMmcRequest("opendoor") }
        binding.btnOpenSecondFloorDoor.setOnClickListener { statusViewModel.svsMmcRequest("opendoor") }
        binding.btnCloseFirstFloorDoor.setOnClickListener { statusViewModel.svsMmcRequest("closedoor") }
        binding.btnCloseSecondFloorDoor.setOnClickListener { statusViewModel.svsMmcRequest("closedoor") }
        binding.btnFirstLightOn.setOnClickListener { statusViewModel.svsMmcRequest("alllighton") }
        binding.btnSecondLightOn.setOnClickListener { statusViewModel.svsMmcRequest("alllighton") }
        binding.btnFirstLightOff.setOnClickListener { statusViewModel.svsMmcRequest("alllightoff") }
        binding.btnSecondLightOff.setOnClickListener { statusViewModel.svsMmcRequest("alllightoff") }
//       展示按键返回消息
        statusViewModel.commonResponse.observe(viewLifecycleOwner, { result ->
            Toast.makeText(
                activity,
                result.getOrElse { result.exceptionOrNull().toString() },
                Toast.LENGTH_SHORT
            ).show()
        })


//        点击服务图片，弹出dialog输入url
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setView(sbinding.root)
            .setNeutralButton("取消") { _, _ -> }
            .setPositiveButton("保存") { _, _ -> saveUrl() }
            .create()
        binding.btnServer.setOnClickListener { dialog.show() }
//        输入框绑定回车按键，等同于"保存"
        sbinding.textInputSeverSetting.editText?.setText(statusViewModel.getSavedUrl())
        sbinding.textInputSeverSetting.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveUrl()
                dialog.hide()
                true
            } else false
        }
        statusViewModel.isConnected.observe(viewLifecycleOwner, { result ->
            if (result.isSuccess) {
                binding.btnServer.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.icon_green_72_45
                    )
                )
            } else {
                binding.btnServer.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.icon_red_72_45
                    )
                )
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) { statusViewModel.monitorServiceStatus() }
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.title = "状态监控"


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _sbinding = null
    }

    fun saveUrl() {
        val inputText = sbinding.textInputSeverSetting.editText?.text.toString()
        statusViewModel.saveUrl(inputText)
    }

//    todo 电脑页面
}