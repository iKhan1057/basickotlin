package com.e.myfeeds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider
import com.bumptech.glide.Glide
import com.e.myfeeds.FeedsActivity
import com.e.myfeeds.R
import com.e.myfeeds.models.Feed_images
import com.e.myfeeds.models.Feed_interactions
import com.e.myfeeds.models.Feeds
import com.e.myfeeds.models.Posted_by
import com.e.pageindicator.PageIndicator
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class FeedsAdapter(val activity: FeedsActivity, val feeddata: List<Feeds>) :
    RecyclerView.Adapter<FeedsAdapter.FeedsViewHolder>() {
    var isUpdateTime: Boolean = false

    class FeedsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(feeds: Feeds, activity: FeedsActivity, isUpdateTime: Boolean) {
            when {
                !isUpdateTime -> {
                    postedby(feeds.posted_by, activity)
                    postedImages(feeds.feed_images, activity)
                    postInteraction(feeds.feed_interactions, activity)
                    val feed_options: AppCompatImageView = itemView.findViewById(R.id.feed_options)
                    feed_options.setOnClickListener {
                        Toast.makeText(activity, feeds.id.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            val posted_by_time: TextView = itemView.findViewById(R.id.posted_by_time)
            feeds.posted_time.also { posted_by_time.text = it }
        }

        private fun postInteraction(feedInteractions: Feed_interactions, activity: FeedsActivity) {
            val feed_like_count: TextView = itemView.findViewById(R.id.feed_like_count)
            feedInteractions.like.also { " $it".also { feed_like_count.text = it } }

            val feed_comment_count: TextView = itemView.findViewById(R.id.feed_comment_count)
            feedInteractions.comments.also { " $it".also { feed_comment_count.text = it } }

            val feed_share_count: TextView = itemView.findViewById(R.id.feed_share_count)
            feedInteractions.share.also { " $it".also { feed_share_count.text = it } }
        }

        private fun postedImages(feedImages: List<Feed_images>, activity: FeedsActivity) {
            val recy_page_indicator: PageIndicator = itemView.findViewById(R.id.recy_page_indicator)
            val recy_posted_image: RecyclerView = itemView.findViewById(R.id.recy_posted_image)
            val linearSnapHelper: LinearSnapHelper = SnapHelperOneByOne()

            recy_posted_image.setHasFixedSize(true)
            recy_posted_image.setLayoutManager(
                LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            recy_posted_image.setItemAnimator(DefaultItemAnimator())
            val feedimageAdapter = FeedImagesAdapter(activity, feedImages)
            recy_posted_image.adapter = feedimageAdapter
            recy_page_indicator.attachTo(recy_posted_image)
            linearSnapHelper.attachToRecyclerView(recy_posted_image)
        }

        private fun postedby(postedBy: Posted_by, activity: FeedsActivity) {
            val posted_by_image: CircleImageView = itemView.findViewById(R.id.posted_by_image)
            Glide.with(activity)
                .load(postedBy.profile_pic)
                .into(posted_by_image)

            val posted_by_name: TextView = itemView.findViewById(R.id.posted_by_name)
            postedBy.name.also { posted_by_name.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_feed, parent, false)
        return FeedsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedsViewHolder, position: Int) {
        holder.bind(feeddata[position], activity, isUpdateTime)
    }

    override fun getItemCount(): Int {
        return feeddata.size
    }

    fun updateTime() {
        isUpdateTime = true
        for (i in feeddata.indices) {
            val feeds: Feeds = feeddata[i]
            feeds.posted_time = Date(feeds.time).getTimeAgo()
            notifyDataSetChanged()
        }
    }

    fun Date.getTimeAgo(): String {
        val calendar = Calendar.getInstance()
        calendar.time = this

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val currentCalendar = Calendar.getInstance()

        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)

        return if (year < currentYear) {
            val interval = currentYear - year
            if (interval == 1) "$interval year ago" else "$interval years ago"
        } else if (month < currentMonth) {
            val interval = currentMonth - month
            if (interval == 1) "$interval month ago" else "$interval months ago"
        } else if (day < currentDay) {
            val interval = currentDay - day
            if (interval == 1) "$interval day ago" else "$interval days ago"
        } else if (hour < currentHour) {
            val interval = currentHour - hour
            if (interval == 1) "$interval hour ago" else "$interval hours ago"
        } else if (minute < currentMinute) {
            val interval = currentMinute - minute
            if (interval == 1) "$interval minute ago" else "$interval minutes ago"
        } else {
            "a moment ago"
        }
    }

    class SnapHelperOneByOne : LinearSnapHelper() {
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager,
            velocityX: Int,
            velocityY: Int
        ): Int {
            if (layoutManager !is ScrollVectorProvider) {
                return RecyclerView.NO_POSITION
            }
            val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
            val myLayoutManager = layoutManager as LinearLayoutManager
            val position1 = myLayoutManager.findFirstVisibleItemPosition()
            val position2 = myLayoutManager.findLastVisibleItemPosition()
            var currentPosition = layoutManager.getPosition(currentView)
            if (velocityX > 400) {
                currentPosition = position2
            } else if (velocityX < 400) {
                currentPosition = position1
            }
            return if (currentPosition == RecyclerView.NO_POSITION) {
                RecyclerView.NO_POSITION
            } else currentPosition
        }
    }

}