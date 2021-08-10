package com.e.myfeeds.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Feed_interactions : Serializable {
    @SerializedName("like")
    var like: Int = 30
    @SerializedName("comments")
    var comments: Int = 20
    @SerializedName("share")
    var share: Int = 1
}