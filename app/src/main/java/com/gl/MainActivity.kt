package com.gl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.gl.newuser.NewUserActivity
import com.gl.recyclerv.RecyclerVActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_seekbar: Button = findViewById(R.id.btn_seekbar)
        btn_seekbar.setOnClickListener {
            val intent = Intent(this@MainActivity, SeekArcActivity::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)
        }
        val login_click: Button = findViewById(R.id.btn_login)
        login_click.setOnClickListener {
            val intent = Intent(this@MainActivity, NewUserActivity::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)
        }

        val recycler_view: Button = findViewById(R.id.btn_recycler)
        recycler_view.setOnClickListener {
            val intent = Intent(this@MainActivity, RecyclerVActivity::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)
        }
    }


}