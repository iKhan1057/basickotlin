package com.e.myfeeds

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.e.imagepicker.MediaActivity
import com.e.myfeeds.adapter.FeedImagesAdapter
import com.e.myfeeds.adapter.FeedsAdapter
import com.e.myfeeds.models.Feed_images
import com.e.myfeeds.models.Feeds
import com.e.myfeeds.models.FeedsModel
import com.e.pageindicator.PageIndicator
import java.util.*
import kotlin.collections.ArrayList

class AddFeedActivity : AppCompatActivity() {

    var feeds = Feeds()
    var feed_images: MutableList<Feed_images> = ArrayList()
    var feedimageAdapter = FeedImagesAdapter(this, feed_images)
    lateinit var recy_added_image: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_feed)

        recy_added_image = findViewById(R.id.recy_added_image)
        val recy_page_indicator: PageIndicator = findViewById(R.id.recy_page_indicator)
        val linearSnapHelper: LinearSnapHelper = FeedsAdapter.SnapHelperOneByOne()

        recy_added_image.setHasFixedSize(true)
        recy_added_image.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        recy_added_image.setItemAnimator(DefaultItemAnimator())
        recy_added_image.adapter = feedimageAdapter
        recy_page_indicator.attachTo(recy_added_image)
        linearSnapHelper.attachToRecyclerView(recy_added_image)
    }

    var launchSomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val feedimages = Feed_images()
                    val path = data.getStringExtra("get_image_path")
                    val uri = data.getStringExtra("image_uri")
                    feedimages.image = path.toString()
                    feed_images.add(feedimages)
                    feedimageAdapter.notifyDataSetChanged()
                    recy_added_image.scrollToPosition((feed_images.size - 1))
                }
            }
        }

    fun openGallery(view: View) {
        val intent = Intent(this, MediaActivity::class.java)
        intent.putExtra("action", "1")
        intent.putExtra("gallery", "0")
        intent.putExtra("camera", "1")
        launchSomeActivity.launch(intent)
    }

    fun openCamera(view: View) {
        val intent = Intent(this, MediaActivity::class.java)
        intent.putExtra("action", "1")
        intent.putExtra("camera", "0")
        intent.putExtra("gallery", "1")
        launchSomeActivity.launch(intent)
    }

    fun addFeed(view: View) {
        feeds.posted_time = Date().toString()
        feeds.time = Date().toString()
        feeds.feed_images = feed_images
        val intent = Intent()
        intent.putExtra("FEEDDATA", feeds)
        intent.action = "NEW_FEED_ADDED"
        sendBroadcast(intent)
        finish()
    }

}