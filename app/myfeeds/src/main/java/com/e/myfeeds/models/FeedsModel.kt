package com.e.myfeeds.models

import com.google.gson.annotations.SerializedName

class FeedsModel {
    @SerializedName("feeds")
    val feeds: List<Feeds> = ArrayList()
}