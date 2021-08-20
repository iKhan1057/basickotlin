package com.basickotlin.newuser

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basickotlin.R
import com.basickotlin.views.RoundedBottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

class NewUserActivity : AppCompatActivity(), FlowerAdapter.Callback {
    private var isSelected: Boolean = false
    lateinit var dataselected: DataModel
    lateinit var recy_user: RecyclerView
    lateinit var recy_adapter: FlowerAdapter
    val flowerList = ArrayList<DataModel>()

    lateinit var lin_suggested_user: LinearLayout
    lateinit var lin_suggested_user_error: LinearLayout
    lateinit var et_user_name: TextInputEditText
    lateinit var txt_already_taken_error: TextView
    lateinit var lin_parent: ScrollView

    private val regOneLetter = ".*[a-zA-Z]+.*";
    private val regOneNo = ".*\\d.*"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        lin_parent = findViewById(R.id.lin_parent)
        txt_already_taken_error = findViewById(R.id.txt_already_taken_error)
        et_user_name = findViewById(R.id.et_user_name)
        val txt_user_guideline: TextView = findViewById(R.id.txt_user_guideline)
        txt_user_guideline.setOnClickListener {
            bottomSheet()
        }
        et_user_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkForChange()
                checkUser()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkForChange()
                checkUser()
            }
        })
        lin_suggested_user_error = findViewById(R.id.lin_suggested_user_error)
        lin_suggested_user = findViewById(R.id.lin_suggested_user)

        recy_user = findViewById(R.id.recy_user)

        recy_user.setHasFixedSize(true)
        recy_user.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        recy_user.setItemAnimator(DefaultItemAnimator())
        recy_adapter = FlowerAdapter(this, flowerList, this)
        recy_user.adapter = recy_adapter

        val img_submit: ImageView = findViewById(R.id.img_submit)
        img_submit.setOnClickListener {
            checkUser()
        }

        val txt_password: TextView = findViewById(R.id.txt_password)
        txt_password.setOnClickListener {
            val intent = Intent(this, NewPassActivity::class.java)
            intent.putExtra("USERID", et_user_name.text.toString())
            startActivity(intent)
        }
        isKeyBoardOpen(lin_parent)
    }

    private fun checkUser() {
        if (checkUserid().isNotEmpty()) {
            txt_already_taken_error.text = checkUserid()
            txt_already_taken_error.visibility = View.VISIBLE
        } else {
            if (et_user_name.text?.trim().toString().equals("abc123", ignoreCase = true)) {
                flowerList.clear()
                flowerList.addAll(Datasource(this).getFlowerList())
                recy_adapter.updateData(flowerList)
                txt_already_taken_error.visibility = View.VISIBLE
                lin_suggested_user.visibility = View.VISIBLE
                lin_suggested_user_error.visibility = View.VISIBLE
            } else if (et_user_name.text?.trim().toString().equals("xyz123", ignoreCase = true)) {
                flowerList.clear()
                flowerList.addAll(Datasource(this).getCompanyList())
                recy_adapter.updateData(flowerList)
                txt_already_taken_error.visibility = View.VISIBLE
                lin_suggested_user.visibility = View.VISIBLE
                lin_suggested_user_error.visibility = View.VISIBLE
            } else {
                txt_already_taken_error.visibility = View.GONE
            }
        }
    }

    private fun checkForChange() {
        if (isSelected && !et_user_name.text.toString().equals(dataselected.name)) {
            isSelected = false
            val updatedlist = recy_adapter.getNameList()
            val pos = updatedlist.indexOf(dataselected)
            dataselected.selection = false
            updatedlist[pos] = dataselected
            recy_adapter.notifyItemChanged(pos)
        }
    }

    override fun clicked(data: DataModel) {
        super.clicked(data)
        et_user_name.setText(data.name)
        et_user_name.setSelection(et_user_name.text?.trim().toString().length)
        isSelected = true
        dataselected = data
    }

    private fun isKeyBoardOpen(viewGroup: ViewGroup?): Boolean {
        viewGroup?.viewTreeObserver?.addOnGlobalLayoutListener {
            val rect = Rect()
            viewGroup.getWindowVisibleDisplayFrame(rect)
            val screenHeight = viewGroup.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            if (keypadHeight > screenHeight * 0.15)
                lin_suggested_user_error.visibility = View.VISIBLE
            else
                lin_suggested_user_error.visibility = View.GONE
        }
        return false
    }


    private fun checkUserid(): String {
        val userid = et_user_name.text.toString()
        return if (userid.isEmpty())
            "Enter a user id"
        else if (userid.length < 5)
            getString(R.string.char_count_user)
        else if (!userid.matches(regOneLetter.toRegex()))
            getString(R.string.alphabet)
        else if (!userid.matches(regOneNo.toRegex()))
            getString(R.string.num)
        else
            ""
    }


    private fun bottomSheet() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.dialog_pass_guidelines, null)
        mBottomSheetDialog.setContentView(sheetView)
        createUserGuideLine(sheetView)
        val btn_close: Button = sheetView.findViewById(R.id.btn_close)
        btn_close.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }
        mBottomSheetDialog.show()
    }


    private fun createUserGuideLine(sheetView: View) {
        val lin_pass_guidelines: LinearLayout = sheetView.findViewById(R.id.lin_pass_guidelines)
        val list: MutableList<String> =
            java.util.ArrayList()
        list.add(getString(R.string.char_count_user))
        list.add(getString(R.string.no_space))
        list.add(getString(R.string.composition_user))
        list.add(getString(R.string.no_ssn))

        for (i in list.indices) {
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.item_pass_guideline, null)
            // In order to get the view we have to use the new view with text_layout in it
            val textView: TextView = rowView.findViewById(R.id.text)
            textView.text = list[i]
            // Add the new row before the add field button.
            lin_pass_guidelines.addView(rowView, i)
        }
    }
}