package com.basickotlin.newuser

import android.content.Context
import com.basickotlin.R
import java.util.ArrayList


class Datasource(val context: Context) {
    fun getFlowerList(): ArrayList<DataModel> {
      val flowerlist:  Array<String> = context.resources.getStringArray(R.array.flower_array)
        // Return flower list from string resources
        val lit: ArrayList<DataModel> = ArrayList()
        for (element in flowerlist) {
            lit.add(DataModel(element, false))
        }
        return lit
    }
    fun getCompanyList(): ArrayList<DataModel> {
        val flowerlist:  Array<String> = context.resources.getStringArray(R.array.company_array)
        // Return flower list from string resources
        val lit: ArrayList<DataModel> = ArrayList()
        for (element in flowerlist) {
            lit.add(DataModel(element, false))
        }
        return lit
    }
}