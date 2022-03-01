package fgd.showroom.ui.status

import android.content.Intent
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
import fgd.showroom.ui.observeCommonRpInfo
import fgd.showroom.ui.status.computers.ComputersActivity
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

//      总开机 总关机 复位 应急模式 开关灯 开关门
//      注册点击请求
        registeClickEvents()
//

//      点击服务图片，注册(编辑url)事件
        urlDialog()

//      轮询监听服务url是否已连接
        serverIsConnected()

//      点击电脑按钮，查看状态详情
        binding.btnComputer.setOnClickListener {
            val intent = Intent(requireContext(), ComputersActivity::class.java)
            this.startActivity(intent)
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        展示按键返回消息
        observeCommonRpInfo(requireActivity(), requireActivity(), statusViewModel.commonResponse)
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

    private fun registeClickEvents() {
        binding.btnPowerOn.setOnClickListener { statusViewModel.svsMmcRequest("poweron") }
        binding.btnPowerOff.setOnClickListener { statusViewModel.svsMmcRequest("poweroff") }
        binding.btnReset.setOnClickListener { statusViewModel.svsMmcRequest("reset") }
        binding.btnEmergence.setOnClickListener { statusViewModel.svsMmcRequest("emergence") }
    }

    fun saveUrl() {
        val inputText = sbinding.textInputSeverSetting.editText?.text.toString()
        val saved = statusViewModel.saveUrl(inputText)
        if (!saved) {
            Toast.makeText(
                activity,
                "不合法url地址，请检查",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun urlDialog() {
        //      点击服务图片，弹出dialog输入url
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
    }


    private fun serverIsConnected() {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) { statusViewModel.monitorServiceStatus() }
        }
        statusViewModel.isConnected.observe(viewLifecycleOwner) { result ->
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
        }

        statusViewModel.computersStatus.observe(viewLifecycleOwner) { result ->
            val computerList = result.getOrNull() ?: listOf()
            var hasOnline = false
            var hasOffline = false
            computerList.forEach { if (it.state == 1) hasOnline = true else hasOffline = true }
            var img = R.drawable.icon_red_72_45
            if (hasOnline) {
                if (hasOffline)
                    img = R.drawable.icon_yellow_72_45
                else if (!hasOffline) {
                    img = R.drawable.icon_green_72_45
                }
            }
            binding.btnComputer.setImageDrawable(ContextCompat.getDrawable(requireContext(), img))
        }

    }
}