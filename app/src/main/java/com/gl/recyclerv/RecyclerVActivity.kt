package com.gl.recyclerv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gl.R

class RecyclerVActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_v)

        val recy_user: RecyclerView = findViewById(R.id.recy_user)
        val flowerList = Datasource(this).getFlowerList()
        recy_user.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recy_user.setLayoutManager(layoutManager)
        recy_user.setItemAnimator(DefaultItemAnimator())
        recy_user.adapter = FlowerAdapter(this, flowerList)
    }
}