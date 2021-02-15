package com.gl.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gl.R
import com.gl.confirmation.ConfirmationActivity
import com.gl.recyclerv.RecyclerVActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    lateinit var et_pass: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        et_pass = findViewById(R.id.et_pass)

        val txt_name: TextView = findViewById(R.id.txt_name)
        val name: String = intent.getStringExtra("Key")!!
        txt_name.text = name
        val forgotpassword: TextView = findViewById(R.id.forgotpassword)
        forgotpassword.setOnClickListener {
            Toast.makeText(this, "FORGOT", Toast.LENGTH_LONG).show()
        }

        val img_submit: ImageView = findViewById(R.id.img_submit)
        img_submit.setOnClickListener {
            if (et_pass.text.toString().isNotEmpty()) {
                val intent = Intent(this@LoginActivity, ConfirmationActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}