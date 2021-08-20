package com.basickotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.e.showmore.ShowMore
import com.e.showmore.ShowMoreDialog
import com.e.showmore.onMoreClicked

class ShowMoreActivity : AppCompatActivity(), onMoreClicked {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_more)

        showmore()
        showmoreDialog()
    }

    private fun showmore() {
        val txt_showmore: ShowMore = findViewById(R.id.txt_showmore)
        txt_showmore.setContent(
            "\n" +
                    "What is Lorem Ipsum?\n" +
                    "\n" +
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
        )
        txt_showmore.setTextMaxLength(100)
        txt_showmore.setMoreText("More")
        txt_showmore.setLessText("Less")
        txt_showmore.setMoreTextColor(resources.getColor(R.color.black, null))
        txt_showmore.setLessTextColor(resources.getColor(R.color.blue, null))
    }

    private fun showmoreDialog() {
        val showmore_dialog: ShowMoreDialog = findViewById(R.id.showmore_dialog)
        showmore_dialog.setContent(
            "\n" +
                    "What is Lorem Ipsum?\n" +
                    "\n" +
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
        )
        showmore_dialog.setTextMaxLength(100)
        showmore_dialog.setMoreText("More")
        showmore_dialog.setMoreTextColor(resources.getColor(R.color.black, null))
        showmore_dialog.setOnTextClicked(this@ShowMoreActivity)
    }

    override fun onMoreClicked(original: String) {
        Toast.makeText(this@ShowMoreActivity, original, Toast.LENGTH_LONG).show()
    }

    override fun onMoreLongClicked() {
    }

}



