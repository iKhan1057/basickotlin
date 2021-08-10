package com.e.myfeeds

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.myfeeds.adapter.FeedsAdapter
import com.e.myfeeds.models.Feeds
import com.e.myfeeds.models.FeedsModel
import com.e.myfeeds.models.Posted_by
import com.e.myfeeds.parser.AssetParser
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class FeedsActivity : AppCompatActivity() {
    lateinit var feedadapter: FeedsAdapter
    var feeddata = FeedsModel()
    private val intentfilter = IntentFilter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeds)
        initviews()
        intentfilter.addAction("NEW_FEED_ADDED")
        intentfilter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(broadCastReceiver, intentfilter)
    }


    private fun initviews() {
        val img_add_new: CircleImageView = findViewById(R.id.img_add_new)
        img_add_new.setOnClickListener {
            val intent = Intent(this, AddFeedActivity::class.java)
            startActivity(intent)
        }

        val json = AssetParser.loadJSONFromAssets(this, "", "feeds")
        feeddata = Gson().fromJson(json, FeedsModel::class.java)

        val recy_feeds: RecyclerView = findViewById(R.id.recy_feeds)
        recy_feeds.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recy_feeds.setLayoutManager(layoutManager)
        recy_feeds.setItemAnimator(DefaultItemAnimator())
        for (i in feeddata.feeds.indices) {
            val feeds: Feeds = feeddata.feeds[i]
            Date().also { feeds.time = it.toString() }
        }
        feedadapter = FeedsAdapter(this, feeddata.feeds)
        recy_feeds.adapter = feedadapter
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_TIME_TICK -> feedadapter.updateTime()
                "NEW_FEED_ADDED" -> addnewdata(intent)
            }
        }
    }

    private fun addnewdata(intent: Intent) {
        val feeds: Feeds = intent.getSerializableExtra("FEEDDATA") as Feeds
        val feedpostecby = Posted_by()
        feedpostecby.id = 5
        feedpostecby.name = "Imran"
        feedpostecby.profile_pic =
            "https://i.pinimg.com/originals/4f/f9/70/4ff9702a296085183901ec2e67fc6c95.jpg"
        feeds.posted_by = feedpostecby
        feedadapter.feedsOfAdapter().add(feeds)
        feedadapter.updateTime()
    }

    override fun onResume() {
        super.onResume()

    }

//    override fun onPause() {
//        super.onPause()
//        unregisterReceiver(broadCastReceiver);
//    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReceiver);
    }
}