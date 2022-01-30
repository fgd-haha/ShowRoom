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
import fgd.showroom.R
import fgd.showroom.databinding.FragmentDebugBinding
import fgd.showroom.logic.model.Action
import fgd.showroom.logic.model.DevType
import fgd.showroom.logic.model.Device
import fgd.showroom.logic.model.PlayFile
import fgd.showroom.ui.observeCommonRpInfo


class DebugFragment : Fragment() {

    private lateinit var viewModel: DebugViewModel
    private var _binding: FragmentDebugBinding? = null
    private var actionList = mutableListOf<Action>()
    private var devTypeList = mutableListOf<DevType>()
    private var devList = mutableListOf<Device>()
    private var fileList = mutableListOf<PlayFile>()
    private var action = 0
    private var devtype = 0
    private var devno = 0
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

        viewModel.actionList.observe(viewLifecycleOwner) { items ->
            actionList = items
            refreshMenus(actionList, devTypeList, devList, fileList)
        }
        viewModel.devTypeList.observe(viewLifecycleOwner) { items ->
            devTypeList = items
        }
        viewModel.devList.observe(viewLifecycleOwner) { items ->
            devList = items
        }
        viewModel.fileList.observe(viewLifecycleOwner) { items ->
            fileList = items
        }

        (binding.actionMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            action = actionList[position].action
            when {
                setOf(11, 12, 13, 14, 15, 16).contains(action) -> {
                    val tDevTypeList = mutableListOf<DevType>(DevType(10, "电脑"))
                    val tDevList = devList.filter { it.typeid == 10 }.toMutableList()
                    refreshMenus(actionList, tDevTypeList, tDevList, fileList)
                }
                setOf(21, 22).contains(action) -> {
                    val tDevTypeList = devTypeList.filter { setOf(10, 20, 30, 40, 50).contains(it.typeid) }.toMutableList()
                    val tDevList = devList.filter { setOf(10, 20, 30, 40, 50).contains(it.typeid) }.toMutableList()
                    refreshMenus(actionList, tDevTypeList, tDevList, mutableListOf())
                }
                else -> {
                    refreshMenus(actionList, devTypeList, devList, fileList)
                }
            }
        }
        (binding.devTypeMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            devtype = devTypeList[position].typeid
            val tDevList = devList.filter { it.typeid == devtype }.toMutableList()
            refreshMenus(null, null, tDevList, null)
        }
        (binding.devNoMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ -> devno = devList[position].devno }
        binding.brightness.addOnChangeListener { _, value, _ -> intv = value.toInt() }

        binding.btnExecute.setOnClickListener {
            val args = mapOf<String, Any>(
                "action" to action,
                "devtype" to devtype,
                "devno" to devno,
                "intv" to intv,
                "filename" to binding.filenameMenu.editText?.text.toString()
            )
            Log.d("debug args:", args.toString())
            viewModel.execute(args)
        }

        observeCommonRpInfo(requireActivity(), requireActivity(), viewModel.commonResponse)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "调试模式"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun refreshMenus(
        tActionList: MutableList<Action>?,
        tDevTypeList: MutableList<DevType>?,
        tDevList: MutableList<Device>?,
        tFileList: MutableList<PlayFile>?
    ) {
        if (tActionList != null) {
            val actionAdapter = ArrayAdapter(requireContext(), R.layout.menu_item, tActionList.map { it.memo })
            val actionMenu = (binding.actionMenu.editText as? AutoCompleteTextView)
            actionMenu?.setAdapter(actionAdapter)
            if (tActionList.isNotEmpty() && tActionList.none { it.memo == actionMenu?.text.toString() }) actionMenu?.setText("", false)
        }

        if (tDevTypeList != null) {
            val devTypeAdapter = ArrayAdapter(requireContext(), R.layout.menu_item, tDevTypeList.map { it.typename })
            val devTypeMenu = (binding.devTypeMenu.editText as? AutoCompleteTextView)
            devTypeMenu?.setAdapter(devTypeAdapter)
            if (tDevTypeList.isNotEmpty() && tDevTypeList.none { it.typename == devTypeMenu?.text.toString() }) devTypeMenu?.setText("", false)
        }

        if (tDevList != null) {
            val devNoAdapter = ArrayAdapter(requireContext(), R.layout.menu_item, tDevList.map { it.devname })
            val devNoMenu = (binding.devNoMenu.editText as? AutoCompleteTextView)
            devNoMenu?.setAdapter(devNoAdapter)
            if (tDevList.isNotEmpty() && tDevList.none { it.devname == devNoMenu?.text.toString() }) devNoMenu?.setText("", false)
        }

        if (tFileList != null) {
            val filenameAdapter = ArrayAdapter(requireContext(), R.layout.menu_item, tFileList.map { it.filename })
            val fileMenu = (binding.filenameMenu.editText as? AutoCompleteTextView)
            fileMenu?.setAdapter(filenameAdapter)
            if (tFileList.isNotEmpty() && tFileList.none { it.filename == fileMenu?.text.toString() }) fileMenu?.setText("", false)
        }
    }
}