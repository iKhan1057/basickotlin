package com.e.myfeeds

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.myfeeds.adapter.FeedsAdapter
import com.e.myfeeds.models.Feeds
import com.e.myfeeds.models.FeedsModel
import com.e.myfeeds.parser.AssetParser
import com.google.gson.Gson
import java.util.*

class FeedsActivity : AppCompatActivity() {
    lateinit var feedadapter: FeedsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeds)
        initviews()
    }

    private fun initviews() {
        val json = AssetParser.loadJSONFromAssets(this, "", "feeds")
        val feeddata = Gson().fromJson(json, FeedsModel::class.java)

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
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadCastReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadCastReceiver);
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReceiver)
    }

}