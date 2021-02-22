package com.gl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.myseekbar.ArcSeekBar

class SeekArcActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)

        val arcseek: ArcSeekBar = findViewById(R.id.arcseek)
        arcseek.progress = 35
    }
}