package com.e.myfeeds.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeedsModel : Serializable {
    @SerializedName("feeds")
    var feeds: MutableList<Feeds> = ArrayList()
}