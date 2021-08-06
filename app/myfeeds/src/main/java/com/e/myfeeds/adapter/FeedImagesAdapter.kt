package com.e.myfeeds.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.myfeeds.FeedsActivity
import com.e.myfeeds.R
import com.e.myfeeds.models.Feed_images

class FeedImagesAdapter(val activity: Activity, val images: List<Feed_images>) :
    RecyclerView.Adapter<FeedImagesAdapter.FeedImageViewHolder>() {

    class FeedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(activity: Activity, feedImages: Feed_images) {
            val posted_image: AppCompatImageView = itemView.findViewById(R.id.posted_image)
            Glide.with(activity)
                .load(feedImages.image)
                .into(posted_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_feed_images, parent, false)
        return FeedImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedImageViewHolder, position: Int) {
        holder.bind(activity,images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}