package com.gl.recyclerv

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gl.R
import com.gl.login.LoginActivity

class FlowerAdapter(val activity: RecyclerVActivity, val flowerList: Array<String>) :
    RecyclerView.Adapter<FlowerAdapter.FlowerViewHolder>() {

    // Describes an item view and its place within the RecyclerView
    class FlowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flowerTextView: TextView = itemView.findViewById(R.id.flower_text)
        public val frame_parent: RelativeLayout = itemView.findViewById(R.id.frame_parent);
        fun bind(word: String) {
            flowerTextView.text = word
        }
    }

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.flower_item, parent, false)

        return FlowerViewHolder(view)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return flowerList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
        holder.bind(flowerList[position])
        holder.frame_parent
        holder.frame_parent.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("Key", flowerList[position])
            activity.startActivity(intent)
        }
    }
}