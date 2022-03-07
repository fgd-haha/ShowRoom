package fgd.showroom.ui.setting


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fgd.showroom.databinding.ActionItemBinding
import fgd.showroom.logic.model.StepAction
import fgd.showroom.ui.setting.action.ActionActivity


class StepActionAdapter(private val fragment: SettingFragment, private var stepActionList: List<StepAction>) :
    RecyclerView.Adapter<StepActionAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ActionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val stepidx: TextView = binding.stepidx
        val action: TextView = binding.action
    }

    fun setStepActionList(stepActionList: List<StepAction>) {
        this.stepActionList = stepActionList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val action = this.stepActionList[holder.adapterPosition]
            val intent = Intent(parent.context, ActionActivity::class.java).apply {
                putExtra("id", action.id)
                putExtra("stepno", action.stepno)
                putExtra("stepidx", action.stepidx)
                putExtra("devtype", action.devtype)
                putExtra("action", action.action)
                putExtra("devno", action.devno)
                putExtra("filename", action.filename)
                putExtra("intv", action.intv)
            }
            fragment.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stepAction = stepActionList[position]
        holder.stepidx.text = stepAction.stepidx.toString()
        holder.action.text = when (stepAction.action) {
            11 -> "${stepAction.devno} 号 【${fragment.viewModel.getDevName(stepAction.devtype, stepAction.devno)}】播放 【${stepAction.filename}】"
            12 -> stepAction.devno.toString() + "号【${fragment.viewModel.getDevName(stepAction.devtype, stepAction.devno)}】停止播放"
            13 -> stepAction.devno.toString() + "号【${fragment.viewModel.getDevName(stepAction.devtype, stepAction.devno)}】暂停"
            14 -> stepAction.devno.toString() + "号【${fragment.viewModel.getDevName(stepAction.devtype, stepAction.devno)}】恢复播放"
            21 -> stepAction.devno.toString() + "号【${fragment.viewModel.getDevName(stepAction.devtype, stepAction.devno)}】开机"
            22 -> stepAction.devno.toString() + "号【${fragment.viewModel.getDevName(stepAction.devtype, stepAction.devno)}】关机"
            31 -> stepAction.devno.toString() + "号【${fragment.viewModel.getDevName(stepAction.devtype, stepAction.devno)}】待机"
            32 -> stepAction.devno.toString() + "号【${fragment.viewModel.getDevName(stepAction.devtype, stepAction.devno)}】播放"
            41 -> stepAction.devno.toString() + "号【${
                fragment.viewModel.getDevName(
                    stepAction.devtype,
                    stepAction.devno
                )
            }】循环播放 【" + stepAction.filename + "】"
            42 -> stepAction.devno.toString() + "号【${
                fragment.viewModel.getDevName(
                    stepAction.devtype,
                    stepAction.devno
                )
            }】播放 【" + stepAction.filename + "】"
            55 -> "延时" + stepAction.intv.toString() + "秒"
            else -> "未知动作"
        }
    }

    override fun getItemCount() = stepActionList.size

}