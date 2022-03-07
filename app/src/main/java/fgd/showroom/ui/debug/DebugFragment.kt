package fgd.showroom.ui.debug

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fgd.showroom.MainActivity
import fgd.showroom.R
import fgd.showroom.databinding.FragmentDebugBinding
import fgd.showroom.logic.model.*
import fgd.showroom.ui.observeCommonRpInfo
import fgd.showroom.ui.observeDebugWizardRpInfo
import fgd.showroom.ui.setting.action.ActionActivity


class DebugFragment : Fragment() {

    private lateinit var viewModel: DebugViewModel
    private var _binding: FragmentDebugBinding? = null
    private var actionList = mutableListOf<Action>()
    private var devTypeList = mutableListOf<DevType>()
    private var devList = mutableListOf<Device>()
    private var fileList = mutableListOf<PlayFile>()
    private var tActionList = mutableListOf<Action>()
    private var tDevTypeList = mutableListOf<DevType>()
    private var tDevList = mutableListOf<Device>()
    private var tFileList = mutableListOf<PlayFile>()
    private var a_id: Int? = -1
    private var stepno = -1
    private var stepidx = 0
    private var actionno = -1
    private var devtype = -1
    private var devno = -1
    private var filename = ""
    private var intv = 0


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[DebugViewModel::class.java]

        _binding = FragmentDebugBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (activity is MainActivity) {
            binding.btnSave.visibility = View.GONE
            binding.inputStepIdx.visibility = View.GONE
        }

        viewModel.actionList.observe(viewLifecycleOwner) { items ->
            actionList = items
            if (tActionList.isNullOrEmpty()) {
                tActionList = actionList
            }
            refreshMenus()
        }
        viewModel.devTypeList.observe(viewLifecycleOwner) { items ->
            devTypeList = items
            if (tDevTypeList.isNullOrEmpty()) {
                tDevTypeList = devTypeList
                if (devtype > -1)
                    when {
                        devtype == 10 -> {
                            tDevTypeList = mutableListOf<DevType>(DevType(10, "电脑"))
                            tDevList = devList.filter { it.typeid == 10 }.toMutableList()
                            refreshMenus()
                        }
                        setOf(10, 20, 30, 40, 50).contains(devtype) -> {
                            tDevTypeList = devTypeList.filter { setOf(10, 20, 30, 40, 50).contains(it.typeid) }.toMutableList()
                            tDevList = devList.filter { setOf(10, 20, 30, 40, 50).contains(it.typeid) }.toMutableList()
                            refreshMenus()
                        }
                        else -> {
                            refreshMenus()
                        }
                    }
            }
            refreshMenus()
        }
        viewModel.devList.observe(viewLifecycleOwner) { items ->
            devList = items
            if (tDevList.isNullOrEmpty()) {
                tDevList = devList
                if (devtype > -1)
                    tDevList = devList.filter { it.typeid == devtype }.toMutableList()
            }
            refreshMenus()
        }
        viewModel.fileList.observe(viewLifecycleOwner) { items ->
            fileList = items
            if (tFileList.isNullOrEmpty()) tFileList = fileList
            refreshMenus()
        }

        (binding.actionMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            actionno = tActionList[position].action
            when {
                setOf(11, 12, 13, 14, 15, 16).contains(actionno) -> {
                    tDevTypeList = mutableListOf<DevType>(DevType(10, "电脑"))
                    tDevList = devList.filter { it.typeid == 10 }.toMutableList()
                    refreshMenus()
                }
                setOf(21, 22).contains(actionno) -> {
                    tDevTypeList = devTypeList.filter { setOf(10, 20, 30, 40, 50).contains(it.typeid) }.toMutableList()
                    tDevList = devList.filter { setOf(10, 20, 30, 40, 50).contains(it.typeid) }.toMutableList()
                    refreshMenus()
                }
                else -> {
                    refreshMenus()
                }
            }
        }
        (binding.devTypeMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            devtype = tDevTypeList[position].typeid
            tDevList = devList.filter { it.typeid == devtype }.toMutableList()
            refreshMenus()
        }
        (binding.devNoMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            devno = tDevList[position].devno
            Log.d("debug args:", devno.toString())
        }
        binding.brightness.addOnChangeListener { _, value, _ -> intv = value.toInt() }
        (binding.filenameMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            filename = tFileList[position].filename
        }

        binding.btnExecute.setOnClickListener {
            val args = mapOf<String, Any>(
                "action" to actionno,
                "devtype" to devtype,
                "devno" to devno,
                "intv" to intv,
                "filename" to filename
            )
            Log.d("debug args:", args.toString())
            viewModel.execute(args)
        }

        observeCommonRpInfo(requireActivity(), requireActivity(), viewModel.commonResponse)

        binding.btnSave.setOnClickListener {
            viewModel.saveWizard(
                StepAction(
                    a_id,
                    stepno,
                    binding.inputStepIdx.editText?.text.toString().toInt(),
                    devtype,
                    actionno,
                    devno,
                    filename,
                    intv
                )
            )
        }
        observeDebugWizardRpInfo(requireActivity(), requireActivity(), viewModel.saveWizardRp, viewModel)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is MainActivity) {
            activity?.title = "调试模式"
        } else if (activity is ActionActivity) activity?.title = "设置执行动作"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun refreshMenus() {
        val actionAdapter = ArrayAdapter(requireContext(), R.layout.menu_item, tActionList.map { it.memo })
        val actionMenu = (binding.actionMenu.editText as? AutoCompleteTextView)
        actionMenu?.setAdapter(actionAdapter)
        if (tActionList.isNotEmpty()) {
            val a: Action? = tActionList.find { it.action == actionno }
            if (a == null) actionMenu?.setText("", false)
            else actionMenu?.setText(a.memo, false)
        }


        val devTypeAdapter = ArrayAdapter(requireContext(), R.layout.menu_item, tDevTypeList.map { it.typename })
        val devTypeMenu = (binding.devTypeMenu.editText as? AutoCompleteTextView)
        devTypeMenu?.setAdapter(devTypeAdapter)
        if (tDevTypeList.isNotEmpty()) {
            val a: DevType? = tDevTypeList.find { it.typeid == devtype }
            if (a == null) devTypeMenu?.setText("", false)
            else devTypeMenu?.setText(a.typename, false)
        }


        val devNoAdapter = ArrayAdapter(requireContext(), R.layout.menu_item, tDevList.map { it.devname })
        val devNoMenu = (binding.devNoMenu.editText as? AutoCompleteTextView)
        devNoMenu?.setAdapter(devNoAdapter)
        if (tDevList.isNotEmpty()) {
            val a: Device? = tDevList.find { it.devno == devno && it.typeid == devtype }
            if (a == null) devNoMenu?.setText("", false)
            else devNoMenu?.setText(a.devname, false)
        }


        val filenameAdapter = ArrayAdapter(requireContext(), R.layout.menu_item, tFileList.map { it.filename })
        val fileMenu = (binding.filenameMenu.editText as? AutoCompleteTextView)
        fileMenu?.setAdapter(filenameAdapter)
        if (tFileList.isNotEmpty()) {
            if (tFileList.none { it.filename == filename }) fileMenu?.setText("", false)
            else fileMenu?.setText(filename, false)
        }


        if (intv != binding.brightness.value.toInt()) {
            binding.brightness.value = intv.toFloat()
        }

        val stepidx_text = binding.inputStepIdx.editText?.text.toString()
        if (stepidx_text == "" || stepidx_text.toInt() == 0) {
            binding.inputStepIdx.editText?.setText(stepidx.toString())
        }
    }

    fun setData(action: StepAction) {
        this.a_id = action.id
        this.stepno = action.stepno
        this.stepidx = action.stepidx
        this.devtype = action.devtype
        this.actionno = action.action
        this.devno = action.devno
        this.filename = action.filename
        this.intv = action.intv

        tDevTypeList = devTypeList.filter { it.typeid == devtype }.toMutableList()
        tDevList = devList.filter { it.typeid == devtype }.toMutableList()
    }
}