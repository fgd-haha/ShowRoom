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
import fgd.showroom.databinding.FragmentSettingBinding
import fgd.showroom.logic.model.Step
import fgd.showroom.logic.model.StepAction


class SettingFragment : Fragment() {

    private lateinit var viewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null
    private var stepList = mutableListOf<Step>()
    private var stepActionList = mutableListOf<StepAction>()
    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[SettingViewModel::class.java]

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val steplayoutManager = LinearLayoutManager(activity)
        binding.stepListRecyclerView.layoutManager = steplayoutManager
        val stepAdapter = StepListAdapter(stepList)
        binding.stepListRecyclerView.adapter = stepAdapter
        viewModel.stepList.observe(viewLifecycleOwner) { items ->
            Log.d("SettingFragment", items.toString())
            stepList = items
            stepAdapter.setStepList(stepList)
            stepAdapter.notifyDataSetChanged()
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @Override
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(viewHolder.itemView.context)
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes") { _, _ ->
                        val position = viewHolder.adapterPosition
                        stepList.removeAt(position)
                        stepAdapter.setStepList(stepList)
                        stepAdapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("No") { _, _ ->
                        stepAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                    .create()
                    .show()
            }
        }).attachToRecyclerView(binding.stepListRecyclerView)

        val layoutManager = LinearLayoutManager(activity)
        binding.stepActionRecyclerView.layoutManager = layoutManager
        val adapter = StepActionAdapter(stepActionList)
        binding.stepActionRecyclerView.adapter = adapter
        viewModel.stepActionListLiveData.observe(viewLifecycleOwner, { result ->
            if (result.isSuccess) {
                stepActionList = result.getOrDefault(listOf()).toMutableList()
                adapter.setStepActionList(stepActionList)
                adapter.notifyDataSetChanged()
            }
        })
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @Override
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(viewHolder.itemView.context)
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes") { _, _ ->
                        val position = viewHolder.adapterPosition
                        stepActionList.removeAt(position)
                        adapter.setStepActionList(stepActionList)
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("No") { _, _ ->
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                    .create()
                    .show()
            }
        }).attachToRecyclerView(binding.stepActionRecyclerView)
        viewModel.refreshStepAction(2)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "步骤设置"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}