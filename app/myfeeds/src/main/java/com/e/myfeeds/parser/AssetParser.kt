package com.e.myfeeds.parser

import android.content.Context

object AssetParser {

    fun loadJSONFromAssets(mContext: Context, dir: String, name: String): String {
        var json = ""
        try {
            val inputStream = mContext.assets.open("$dir$name.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer).also { json = it }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return json
    }
}