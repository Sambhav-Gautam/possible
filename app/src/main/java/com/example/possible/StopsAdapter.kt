package com.example.possible

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Stop(val name: String, val visaRequired: Boolean, val distance: Double)

class StopsAdapter(private var stops: List<Stop>, private var currentStopIndex: Int) : RecyclerView.Adapter<StopsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stopText: TextView = view.findViewById(R.id.stopText)
        val visaRequirementText: TextView = view.findViewById(R.id.visaRequirementText)
        val distanceText: TextView = view.findViewById(R.id.distanceText)
        val stopContainer: View = view.findViewById(R.id.stopContainer) // Make sure stop_item.xml has a root layout with this ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stop_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stop = stops[position]
        holder.stopText.text = stop.name
        holder.visaRequirementText.text = "Visa Required: ${if (stop.visaRequired) "Yes" else "No"}"
        holder.distanceText.text = "Distance: ${stop.distance} KM"


        if (position == currentStopIndex) {
            holder.stopContainer.setBackgroundColor(Color.parseColor("#FFDDC1"))
        } else {
            holder.stopContainer.setBackgroundColor(Color.TRANSPARENT)
        }


        holder.itemView.setOnClickListener {
            if (currentStopIndex != position) {
                val previousIndex = currentStopIndex
                currentStopIndex = position


                notifyItemChanged(previousIndex)
                notifyItemChanged(currentStopIndex)
            }
        }
    }
    fun updateCurrentStopIndex(newIndex: Int) {
        val previousIndex = currentStopIndex
        currentStopIndex = newIndex
        notifyItemChanged(previousIndex)
        notifyItemChanged(currentStopIndex)
    }


    override fun getItemCount() = stops.size
}