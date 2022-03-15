package fgd.showroom.ui.setting

import android.annotation.SuppressLint
import android.content.Intent
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
import fgd.showroom.databinding.AddStepDialogBinding
import fgd.showroom.databinding.CopyStepDialogBinding
import fgd.showroom.databinding.FragmentSettingBinding
import fgd.showroom.logic.model.Step
import fgd.showroom.logic.model.StepAction
import fgd.showroom.ui.observeStepRpInfo
import fgd.showroom.ui.observeWizardRpInfo
import fgd.showroom.ui.setting.action.ActionActivity
import fgd.showroom.ui.setting.stepPos.StepPosActivity


class SettingFragment : Fragment() {

    val viewModel by lazy { ViewModelProvider(this)[SettingViewModel::class.java] }
    private var _binding: FragmentSettingBinding? = null
    private var _addstepbinding: AddStepDialogBinding? = null
    private var _copystepbinding: CopyStepDialogBinding? = null

    private var stepList = mutableListOf<Step>()
    private var stepActionList = mutableListOf<StepAction>()
    private val addstepbinding get() = _addstepbinding!!
    private val copystepbinding get() = _copystepbinding!!
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
        _addstepbinding = AddStepDialogBinding.inflate(inflater, container, false)
        _copystepbinding = CopyStepDialogBinding.inflate(inflater, container, false)

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
            viewModel.refreshStepAction(-1)
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
        observeStepRpInfo(requireActivity(), requireActivity(), viewModel.copystepRp, viewModel)

        addStepDialog()
        copyStepDialog()


//      action list
        val layoutManager = LinearLayoutManager(activity)
        binding.stepActionRecyclerView.layoutManager = layoutManager
        val adapter = StepActionAdapter(this, stepActionList)
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
                        viewModel.deleWizard(stepActionList[position].id!!)
                    }
                    .setNegativeButton("取消") { _, _ ->
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                    .create()
                    .show()
            }
        }).attachToRecyclerView(binding.stepActionRecyclerView)

        observeWizardRpInfo(requireActivity(), requireActivity(), viewModel.deleWizardRp, viewModel)

        binding.btnAddAction.setOnClickListener {
            val intent = Intent(activity, ActionActivity::class.java).apply {
                putExtra("stepno", viewModel.nowStepno)
            }
            this.startActivity(intent)
        }

        binding.btnStepPos.setOnClickListener {
            val intent = Intent(requireContext(), StepPosActivity::class.java)
            this.startActivity(intent)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "步骤设置"
        viewModel.refreshStepList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _addstepbinding = null
    }


    private fun addStepDialog() {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setView(addstepbinding.root)
            .setNeutralButton("取消") { _, _ -> }
            .setPositiveButton("保存") { _, _ -> }
            .create()
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val stepnoStr = addstepbinding.inputStepNo.editText?.text.toString()
                val stepno = try {
                    stepnoStr.toInt()
                } catch (e: Exception) {
                    addstepbinding.inputStepNo.error = "部署编号为数字！"
                    -1
                }
                if (stepno >= 0) {
                    addstepbinding.inputStepNo.error = null
                    val stepname = addstepbinding.inputStepName.editText?.text.toString()
                    viewModel.addStep(stepno, stepname)
                    dialog.dismiss();
                }
            }
        }
        binding.btnAddStep.setOnClickListener { dialog.show() }
    }


    private fun copyStepDialog() {
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setView(copystepbinding.root)
            .setNeutralButton("取消") { _, _ -> }
            .setPositiveButton("复制") { _, _ -> }
            .create()
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val stepnoStr = copystepbinding.inputStepNo.editText?.text.toString()
                val stepno = try {
                    stepnoStr.toInt()
                } catch (e: Exception) {
                    copystepbinding.inputStepNo.error = "部署编号为数字！"
                    -1
                }
                if (stepno >= 0) {
                    copystepbinding.inputStepNo.error = null
                    viewModel.copystep(stepno, viewModel.nowStepno)
                    dialog.dismiss()
                }
            }
        }
        binding.btnCopyStep.setOnClickListener { dialog.show() }
    }
}