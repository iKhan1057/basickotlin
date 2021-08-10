package com.e.myfeeds.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Posted_by :Serializable {
    @SerializedName("profile_pic")
    var profile_pic: String = ""
    @SerializedName("id")
    var id: Int = 1
    @SerializedName("name")
    var name: String = ""
}