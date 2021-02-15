package com.gl.newuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gl.R
import java.util.ArrayList

class FlowerAdapter(
    val activity: NewUserActivity,
    var flowerList: ArrayList<DataModel>,
    val mcallback: Callback
) :
    RecyclerView.Adapter<FlowerAdapter.FlowerViewHolder>() {
    interface Callback {
        fun clicked(data: DataModel) {
        }
    }

    // Describes an item view and its place within the RecyclerView
    class FlowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flowerTextView: TextView = itemView.findViewById(R.id.flower_text)
        val frame_parent: RelativeLayout = itemView.findViewById(R.id.frame_parent);
        fun bind(word: String) {
            flowerTextView.text = word
        }
    }

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.flower_item_horizontal, parent, false)

        return FlowerViewHolder(view)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return flowerList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
        holder.bind(flowerList.get(position).name)
        holder.flowerTextView.isSelected = flowerList[position].selection
        holder.frame_parent.isSelected = flowerList[position].selection
        holder.frame_parent.setOnClickListener {
            mcallback.clicked(flowerList[position])
            for (item in flowerList)
                item.selection = false
            flowerList[position].selection = true
            notifyDataSetChanged()
        }
    }

    public fun getNameList(): ArrayList<DataModel> {
        return flowerList
    }

    fun updateData(flowerList1: ArrayList<DataModel>) {
        this.flowerList = flowerList1
        notifyDataSetChanged()
    }
}