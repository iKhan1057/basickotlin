package com.e.custombottomsheet

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.ArrayList


class CustomBottomSheet(context: Context) : BottomSheetDialog(context) {

    private var callbacks: Callbacks? = null
    private var btn_close: Button
    private var txt_sub_title: AppCompatTextView
    private var txt_title: AppCompatTextView
    private var lin_content: LinearLayout
    var list: MutableList<String> = ArrayList()

    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        this.context.setTheme(R.style.SheetDialog)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.bottomsheet_dialog, null, false)
        this.setContentView(contentView)

        txt_title = contentView.findViewById(R.id.txt_title)

        txt_sub_title = contentView.findViewById(R.id.txt_sub_title)

        lin_content = contentView.findViewById(R.id.lin_content)
        setUpContent()

        btn_close = contentView.findViewById(R.id.btn_close)
        btn_close.setOnClickListener {
            callbacks!!.closeClicked()
            dismiss()
        }
    }


    fun title(title: String) {
        txt_title.text = title
    }

    fun subtitle(subtitle: String) {
        txt_sub_title.text = subtitle
    }

    fun setCallbacks(callback: Callbacks) {
        this.callbacks = callback
    }

    fun content(listContent: MutableList<String>) {
        this.list = listContent
        if (lin_content.childCount > 0)
            lin_content.removeAllViews()
        setUpContent()
    }

    private fun setUpContent() {
        if (list.size == 0)
            return

        for (i in list.indices) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.item_bottom_sheet_content, null)
            // In order to get the view we have to use the new view with text_layout in it
            val textView: TextView = rowView.findViewById(R.id.txt_bottom_sheet_txt)
            ((i+1).toString() + ") " + list[i]).also { textView.text = it }
            // Add the new row before the add field button.
            lin_content.addView(rowView, i)
        }
    }
}