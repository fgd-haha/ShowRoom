package fgd.showroom.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fgd.showroom.databinding.FragmentSettingBinding
import fgd.showroom.databinding.StepInputDialogBinding
import fgd.showroom.logic.model.Step
import fgd.showroom.logic.model.StepAction
import fgd.showroom.ui.observeStepRpInfo


class SettingFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this)[SettingViewModel::class.java] }
    private var _binding: FragmentSettingBinding? = null
    private var _sbinding: StepInputDialogBinding? = null

    private var stepList = mutableListOf<Step>()
    private var stepActionList = mutableListOf<StepAction>()
    private val sbinding get() = _sbinding!!
    private val binding get() = _binding!!
    private val TAG = "SettingFragment"

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        _sbinding = StepInputDialogBinding.inflate(inflater, container, false)

//      step list
        val steplayoutManager = LinearLayoutManager(activity)
        binding.stepListRecyclerView.layoutManager = steplayoutManager
        val stepAdapter = StepListAdapter(this, stepList)
        binding.stepListRecyclerView.adapter = stepAdapter
        viewModel.stepList.observe(viewLifecycleOwner) { items ->
            Log.d("SettingFragment", items.toString())
            stepList = items
            stepAdapter.setStepList(stepList)
            stepAdapter.notifyDataSetChanged()
            if (stepList.size > 0) viewModel.refreshStepAction(stepList[0].stepno)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @Override
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(viewHolder.itemView.context)
                    .setMessage("确定删除此步骤?")
                    .setPositiveButton("删除") { _, _ ->
                        val position = viewHolder.adapterPosition
                        viewModel.deleteStep(stepList[position].stepno)
                        viewModel.refreshStepList()
                        viewModel.refreshStepAction(stepList[0].stepno)
                    }
                    .setNegativeButton("取消") { _, _ ->
                        stepAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                    .create()
                    .show()
            }
        }).attachToRecyclerView(binding.stepListRecyclerView)

        observeStepRpInfo(requireActivity(), requireActivity(), viewModel.deleteStepRp, viewModel)
        observeStepRpInfo(requireActivity(), requireActivity(), viewModel.addStepRp, viewModel)

        stepDialog()


//      action list
        val layoutManager = LinearLayoutManager(activity)
        binding.stepActionRecyclerView.layoutManager = layoutManager
        val adapter = StepActionAdapter(stepActionList)
        binding.stepActionRecyclerView.adapter = adapter
        viewModel.stepActionListLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                stepActionList = result.getOrDefault(listOf()).toMutableList()
                adapter.setStepActionList(stepActionList)
                adapter.notifyDataSetChanged()
            }
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @Override
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(viewHolder.itemView.context)
                    .setMessage("确定删除此动作?")
                    .setPositiveButton("删除") { _, _ ->
                        val position = viewHolder.adapterPosition
                        stepActionList.removeAt(position)
                        adapter.setStepActionList(stepActionList)
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("取消") { _, _ ->
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                    .create()
                    .show()
            }
        }).attachToRecyclerView(binding.stepActionRecyclerView)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "步骤设置"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _sbinding = null
    }


    private fun stepDialog() {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setView(sbinding.root)
            .setNeutralButton("取消") { _, _ -> }
            .setPositiveButton("保存") { _, _ -> }
            .create()
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val stepnoStr = sbinding.inputStepNo.editText?.text.toString()
                val stepno = try {
                    stepnoStr.toInt()
                } catch (e: Exception) {
                    sbinding.inputStepNo.error = "部署编号为数字！"
                    -1
                }
                if (stepno >= 0) {
                    sbinding.inputStepNo.error = null
                    val stepname = sbinding.inputStepName.editText?.text.toString()
                    viewModel.addStep(stepno, stepname)
                    dialog.dismiss();
                }
            }
        }
        binding.btnAddStep.setOnClickListener { dialog.show() }
    }
}