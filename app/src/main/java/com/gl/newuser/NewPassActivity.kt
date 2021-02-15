package com.gl.newuser

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
import com.gl.R
import com.gl.confirmation.ConfirmationActivity
import com.gl.views.RoundedBottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.util.regex.Pattern

class NewPassActivity : AppCompatActivity() {
    private val regOneLetter = ".*[a-zA-Z]+.*";
    private val regOneNo = ".*\\d.*"
    var pattern = Pattern.compile(
        ".*([A-Za-z0-9~!@#$%^&*()_/|`-])\\1{2}.*",
        Pattern.CASE_INSENSITIVE
    )
    lateinit var et_pass: TextInputEditText
    lateinit var lin_suggested_pass_error: LinearLayout
    lateinit var txt_pass_error: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pass)

        val txt_pass_guideline: TextView = findViewById(R.id.txt_pass_guideline)
        txt_pass_guideline.setOnClickListener {
            bottomSheet()
        }
        txt_pass_error = findViewById(R.id.txt_pass_error)
        lin_suggested_pass_error = findViewById(R.id.lin_suggested_pass_error)
        et_pass = findViewById(R.id.et_pass)
        et_pass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkPass()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPass()
            }
        })

        val img_submit: ImageView = findViewById(R.id.img_submit)
        img_submit.setOnClickListener {
            if (onTextChanging().isNotEmpty()) {
                checkPass()
            } else {
                val intent = Intent(this@NewPassActivity, ConfirmationActivity::class.java)
                startActivity(intent)
            }
        }

        val scroll_pass_parent: ScrollView = findViewById(R.id.scroll_pass_parent)
        isKeyBoardOpen(scroll_pass_parent)
    }

    private fun checkPass() {
        if (onTextChanging().isNotEmpty()) {
            txt_pass_error.text = onTextChanging()
            txt_pass_error.visibility = View.VISIBLE
        } else {
            txt_pass_error.visibility = View.GONE
        }
    }

    private fun onTextChanging(): String {
        val user_id: String = intent.getStringExtra("USERID").toString()
        val password = et_pass.getText().toString()

        return if (password.isEmpty())
            "Enter a password"
        else if (password.length < 6)
            getString(R.string.char_count)
        else if (!password.matches(regOneLetter.toRegex()))
            getString(R.string.alphabet)
        else if (!password.matches(regOneNo.toRegex()))
            getString(R.string.num)
        else if (pattern.matcher(password).find() || password.contains("\\\\\\"))
            getString(R.string.no_repeat)
        else if ((user_id.length >= 5) && (user_id.contains(password)))
            getString(R.string.diff_no_user)
        else
            ""
    }

    private fun isKeyBoardOpen(viewGroup: ViewGroup?): Boolean {
        viewGroup?.viewTreeObserver?.addOnGlobalLayoutListener {
            val rect = Rect()
            viewGroup.getWindowVisibleDisplayFrame(rect)
            val screenHeight = viewGroup.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            if (keypadHeight > screenHeight * 0.15) {
                lin_suggested_pass_error.visibility = View.VISIBLE
            } else {
                lin_suggested_pass_error.visibility = View.GONE
            }
        }
        return false
    }

    private fun bottomSheet() {
        val mBottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.dialog_pass_guidelines, null)
        mBottomSheetDialog.setContentView(sheetView)
        createPassGuideLine(sheetView)
        val btn_close: Button = sheetView.findViewById(R.id.btn_close)
        btn_close.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }
        mBottomSheetDialog.show()
    }

    private fun createPassGuideLine(sheetView: View) {
        val lin_pass_guidelines: LinearLayout = sheetView.findViewById(R.id.lin_pass_guidelines)
        val list: MutableList<String> =
            ArrayList()
        list.add(getString(R.string.char_count))
        list.add(getString(R.string.no_space))
        list.add(getString(R.string.alphabet))
        list.add(getString(R.string.num))
        list.add(getString(R.string.no_icon))
        list.add(getString(R.string.no_repeat))
        list.add(getString(R.string.diff_no_user))
        list.add(getString(R.string.special_chars))

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

