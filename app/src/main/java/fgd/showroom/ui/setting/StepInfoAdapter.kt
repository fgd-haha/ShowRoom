package fgd.showroom.ui.setting


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fgd.showroom.databinding.ActionItemBinding
import fgd.showroom.logic.model.StepAction


class StepActionAdapter(private var stepActionList: List<StepAction>) : RecyclerView.Adapter<StepActionAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ActionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val stepidx: TextView = binding.stepidx
        val action: TextView = binding.action
    }

    fun setStepActionList(stepActionList: List<StepAction>) {
        this.stepActionList = stepActionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stepAction = stepActionList[position]
        holder.stepidx.text = stepAction.stepidx.toString()
        holder.action.text = when (stepAction.action) {
            11 -> stepAction.devno.toString() + "号机器播放 【" + stepAction.filename + "】"
            12 -> stepAction.devno.toString() + "号机器停止播放"
            13 -> stepAction.devno.toString() + "号机器暂停"
            14 -> stepAction.devno.toString() + "号机器恢复播放"
            21 -> stepAction.devno.toString() + "号投影仪开机"
            22 -> stepAction.devno.toString() + "号投影仪关机"
            31 -> stepAction.devno.toString() + "号LED机器待机"
            32 -> stepAction.devno.toString() + "号LED机器播放"
            41 -> stepAction.devno.toString() + "号融和机循环播放 【" + stepAction.filename + "】"
            42 -> stepAction.devno.toString() + "号融和机播放 【" + stepAction.filename + "】"
            55 -> "延时" + stepAction.intv.toString() + "秒"
            else -> "未知动作"
        }
    }

    override fun getItemCount() = stepActionList.size

}