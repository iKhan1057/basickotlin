package com.basickotlin.recyclerv

import android.content.Context
import com.basickotlin.R


class Datasource(val context: Context) {
    fun getFlowerList(): Array<String> {

        // Return flower list from string resources
        return context.resources.getStringArray(R.array.flower_array)
    }
}