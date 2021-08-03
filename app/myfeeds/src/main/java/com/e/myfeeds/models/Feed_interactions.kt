package com.e.myfeeds.models

import com.google.gson.annotations.SerializedName

class Feed_interactions {
    @SerializedName("like")
    val like: Int = 30
    @SerializedName("comments")
    val comments: Int = 20
    @SerializedName("share")
    val share: Int = 1
}