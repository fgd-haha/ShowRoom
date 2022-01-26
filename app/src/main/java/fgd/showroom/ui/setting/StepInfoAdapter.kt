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
        val holder = ViewHolder(binding)
//        holder.itemView.setOnClickListener {
//            val position = holder.bindingAdapterPosition
//            val step = placeList[position]
//            val activity = fragment.activity
//            if (activity is WeatherActivity) {
//                activity.binding.drawerLayout.closeDrawers()
//                activity.viewModel.locationLng = step.location.lng
//                activity.viewModel.locationLat = step.location.lat
//                activity.viewModel.placeName = step.name
//                activity.refreshWeather()
//            } else {
//                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
//                    putExtra("location_lng", step.location.lng)
//                    putExtra("location_lat", step.location.lat)
//                    putExtra("place_name", step.name)
//                }
//                fragment.startActivity(intent)
//                activity?.finish()
//            }
//            fragment.viewModel.savePlace(step)
//        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stepAction = stepActionList[position]
        holder.stepidx.text = stepAction.stepidx.toString()
        holder.action.text = stepAction.action.toString()
    }

    override fun getItemCount() = stepActionList.size

}