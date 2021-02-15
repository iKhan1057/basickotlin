package com.gl.confirmation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gl.R
import com.gl.recyclerv.RecyclerVActivity
import com.gl.termscondition.TermsAndConditionsActivity

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val txt_done: TextView = findViewById(R.id.txt_done)
        txt_done.setOnClickListener {
            Toast.makeText(this,"Done",Toast.LENGTH_LONG).show()
        }

        val txt_terms_and_condition: TextView = findViewById(R.id.txt_terms_and_condition)
        txt_terms_and_condition.setOnClickListener {
            val intent = Intent(this@ConfirmationActivity, TermsAndConditionsActivity::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)
        }

        val txt_credit_score: TextView = findViewById(R.id.txt_credit_score)
        txt_credit_score.setOnClickListener {
            Toast.makeText(this,"Credit Score",Toast.LENGTH_LONG).show()
        }
    }
}