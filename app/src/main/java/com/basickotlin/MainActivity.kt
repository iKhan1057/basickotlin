package com.basickotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.e.myfeeds.FeedsActivity
import com.basickotlin.newuser.NewUserActivity
import com.basickotlin.recyclerv.RecyclerVActivity
import com.e.custombottomsheet.CustomBottomSheet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn_bottom_sheet: Button = findViewById(R.id.btn_bottom_sheet)
        btn_bottom_sheet.setOnClickListener {
            val intent = Intent(this@MainActivity, BottomSheetActivity::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)
        }

        val btn_showmore: Button = findViewById(R.id.btn_showmore)
        btn_showmore.setOnClickListener {
            val intent = Intent(this@MainActivity, ShowMoreActivity::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)
        }

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

        val btn_feeds: Button = findViewById(R.id.btn_feeds)
        btn_feeds.setOnClickListener {
            val intent = Intent(this@MainActivity, FeedsActivity::class.java)
            intent.putExtra("key", "Kotlin")
            startActivity(intent)
        }


    }


}