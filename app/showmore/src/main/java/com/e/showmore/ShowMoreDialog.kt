package com.e.showmore

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class ShowMoreDialog : AppCompatTextView {
    private var textMaxLength = 160
    private var moreTextColor: Int = context.resources.getColor(R.color.showmore_color, null)
    private var orignalContent: String = ""
    private var collapsedTextSpannable: SpannableString? = null
    private var seeMore = "SeeMore"
    private var onTextClicked: onMoreClicked? = null
    private var collapsedTextWithSeeMoreButton: String? = null

    constructor(context: Context) : super(context) {
        initsetup(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initsetup(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initsetup(context, attrs, defStyleAttr)
    }

    private fun initsetup(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ShowMore,
            0, 0
        ).apply {
            try {
                orignalContent = getString(R.styleable.ShowMore_contenttext).toString()
                textMaxLength = getInteger(R.styleable.ShowMore_maxtextlength, 10)
                seeMore = getString(R.styleable.ShowMore_seemoretext).toString()
                moreTextColor = getInteger(R.styleable.ShowMore_morecolor, R.color.showmore_color)
                setUpContent()
            } finally {
                recycle()
            }
        }
    }

    fun setOnTextClicked(onTextClicked: onMoreClicked?) {
        this.onTextClicked = onTextClicked
    }

    //set max length of the string text
    fun setTextMaxLength(maxLength: Int) {
        this.textMaxLength = maxLength
        invalidate()
        setUpContent()
    }

    fun setMoreTextColor(color: Int) {
        this.moreTextColor = color
        invalidate()
        setUpContent()
    }


    fun setMoreText(seeMoreText: String) {
        seeMore = seeMoreText
        invalidate()
        setUpContent()
    }

    fun setContent(text: String) {
        this.orignalContent = text
        invalidate()
        setUpContent()
    }

    var clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onTextClicked!!.onMoreClicked(orignalContent)
            invalidate()
            setUpContent()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
            ds.color = (moreTextColor)
        }
    }

    fun setUpContent() {
        //show see more
        if (orignalContent.length >= textMaxLength) {
            collapsedTextWithSeeMoreButton =
                orignalContent.substring(0, textMaxLength) + "... " + seeMore
            //creating spannable strings
            collapsedTextSpannable = SpannableString(collapsedTextWithSeeMoreButton)

            collapsedTextSpannable!!.setSpan(
                clickableSpan,
                textMaxLength + 4,
                collapsedTextWithSeeMoreButton!!.length,
                0
            )
            collapsedTextSpannable!!.setSpan(
                StyleSpan(Typeface.NORMAL),
                textMaxLength + 4,
                collapsedTextWithSeeMoreButton!!.length,
                0
            )
            collapsedTextSpannable!!.setSpan(
                RelativeSizeSpan(.9f),
                textMaxLength + 4,
                collapsedTextWithSeeMoreButton!!.length,
                0
            )
            setText(collapsedTextSpannable)
        } else {
            //to do: don't show see more
            setText(orignalContent)
        }
        this.movementMethod = LinkMovementMethod.getInstance()
    }

}