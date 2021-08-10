package com.e.myfeeds.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Feed_images : Serializable {
    @SerializedName("image")
    var image: String = ""
    @SerializedName("id")
    var id: Int = 1
    @SerializedName("time")
    var time: String = ""
}