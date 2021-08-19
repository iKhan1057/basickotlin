package com.gl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.showmore.ShowMore

class ShowMoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_more)

        val txt_showmore: ShowMore = findViewById(R.id.txt_showmore)
        txt_showmore.setContent("\n" +
                "What is Lorem Ipsum?\n" +
                "\n" +
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
        txt_showmore.setTextMaxLength(100)
        txt_showmore.setMoreText("More")
        txt_showmore.setLessText("Less")
        txt_showmore.setMoreTextColor(resources.getColor(R.color.black,null))
        txt_showmore.setLessTextColor(resources.getColor(R.color.blue,null))
    }
}