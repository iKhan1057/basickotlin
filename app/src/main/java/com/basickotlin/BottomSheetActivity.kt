package com.basickotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.e.custombottomsheet.Callbacks
import com.e.custombottomsheet.CustomBottomSheet
import java.util.ArrayList

class BottomSheetActivity : AppCompatActivity(), Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)
    }

    fun opensheet(view: View) {
        val list: MutableList<String> = ArrayList()
        list.add("A")
        list.add("B")
        list.add("C")
        list.add("D")
        val bottomsheet = CustomBottomSheet(this@BottomSheetActivity)
        bottomsheet.setCallbacks(this@BottomSheetActivity)
        bottomsheet.title("USERNAME")
        bottomsheet.subtitle("Profile")
        bottomsheet.content(list)
        bottomsheet.show()
    }

    override fun closeClicked() {
        Toast.makeText(this, "Closed", Toast.LENGTH_LONG).show()
    }

}