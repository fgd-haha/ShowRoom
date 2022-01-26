package fgd.showroom.ui.setting


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fgd.showroom.databinding.StepItemBinding
import fgd.showroom.logic.model.Step


class StepListAdapter(private var stepList: List<Step>) : RecyclerView.Adapter<StepListAdapter.ViewHolder>() {
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
        val step = stepList[position]
        holder.stepNo.text = step.stepno.toString()
        holder.stepName.text = step.stepname
    }

    override fun getItemCount() = stepList.size

}