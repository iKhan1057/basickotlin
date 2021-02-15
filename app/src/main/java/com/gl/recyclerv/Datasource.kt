package com.gl.recyclerv

import android.content.Context
import com.gl.R


class Datasource(val context: Context) {
    fun getFlowerList(): Array<String> {

        // Return flower list from string resources
        return context.resources.getStringArray(R.array.flower_array)
    }
}