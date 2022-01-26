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
    private var devList = mutableListOf<Device>()
    private var devTypeList = mutableListOf<DevType>()
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

        viewModel.actionList.observe(viewLifecycleOwner, { items ->
            actionList = items
            refreshMenus()
        })
        viewModel.devList.observe(viewLifecycleOwner, { items ->
            devList = items
            refreshMenus()
        })
        viewModel.devTypeList.observe(viewLifecycleOwner, { items ->
            devTypeList = items
            refreshMenus()
        })
        viewModel.fileList.observe(viewLifecycleOwner, { items ->
            fileList = items
            refreshMenus()
        })

//        todo 增加列表关联关系
        (binding.actionMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ -> action = actionList[position].action }
        (binding.devTypeMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            devtype = devTypeList[position].typeid
        }
        (binding.devNoMenu.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ -> devno = devList[position].devno }
        binding.brightness.addOnChangeListener { _, value, _ -> intv = value.toInt() }


        binding.btnExecute.setOnClickListener {
            val args = mapOf<String, Any>(
                "action" to action,
                "devtype" to devtype,
                "devno" to devno,
                "intv" to intv,
                "filename" to binding.filename.editText?.text.toString()
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

    fun refreshMenus() {
        (binding.devTypeMenu.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.menu_item,
                devTypeList.map { it.typename })
        )
        (binding.actionMenu.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.menu_item,
                actionList.map { it.memo })
        )
        (binding.devNoMenu.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.menu_item,
                devList.map { it.devname })
        )
        (binding.filename.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.menu_item,
                fileList.map { it.filename })
        )
    }
}