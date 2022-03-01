package fgd.showroom.ui.setting


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fgd.showroom.databinding.StepItemBinding
import fgd.showroom.logic.model.Step


class StepListAdapter(private val fragment: SettingFragment, private var stepList: List<Step>) : RecyclerView.Adapter<StepListAdapter.ViewHolder>() {
    inner class ViewHolder(binding: StepItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val stepNo: TextView = binding.stepNo
        val stepName: TextView = binding.stepName
    }

    fun setStepList(stepList: List<Step>) {
        this.stepList = stepList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StepItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val step = this.stepList[holder.adapterPosition]
            Log.w("StepListAdapter", step.toString())
            fragment.viewModel.refreshStepAction(step.stepno)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = stepList[position]
        holder.stepNo.text = step.stepno.toString()
        holder.stepName.text = step.stepname
    }

    override fun getItemCount() = stepList.size

}