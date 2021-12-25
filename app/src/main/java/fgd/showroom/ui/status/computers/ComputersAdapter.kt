package fgd.showroom.ui.status.computers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fgd.showroom.R
import fgd.showroom.logic.model.DevStatus


class ComputersAdapter(private var computerList: List<DevStatus>) : RecyclerView.Adapter<ComputersAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val computerStatusImage: ImageView = view.findViewById(R.id.computerStatusImage)
        val computerNo: TextView = view.findViewById(R.id.computerNo)
    }

    fun setComputerList(computerList: List<DevStatus>) {
        this.computerList = computerList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_computer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val devStatus = computerList[position]
        val image = if (devStatus.state == 0) {
            R.drawable.icon_red_72_135
        } else R.drawable.icon_green_72_135

        holder.computerStatusImage.setImageResource(image)
        holder.computerNo.text = devStatus.devno.toString()
    }

    override fun getItemCount() = computerList.size
}