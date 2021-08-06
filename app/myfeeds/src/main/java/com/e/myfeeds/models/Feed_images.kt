package com.e.myfeeds.models

import com.google.gson.annotations.SerializedName

class Feed_images {
    @SerializedName("image")
    var image: String = ""
    @SerializedName("id")
    var id: Int = 1
    @SerializedName("time")
    var time: String = ""
}