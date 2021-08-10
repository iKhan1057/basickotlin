package com.e.myfeeds.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Feeds : Serializable{
    @SerializedName("id")
    var id: Int = 1
    @SerializedName("time")
    var time: String = Date().toString()
    @SerializedName("posted_time")
    var posted_time: String = "Just now"
    @SerializedName("posted_by")
    var posted_by: Posted_by = Posted_by()
    @SerializedName("feed_images")
    var feed_images: MutableList<Feed_images> = ArrayList()
    @SerializedName("feed_interactions")
    var feed_interactions: Feed_interactions = Feed_interactions()

}