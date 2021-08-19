package com.e.showmore

import android.content.Context
import android.graphics.Color
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


class ShowMore : AppCompatTextView {
    private var textMaxLength = 160
    private var moreTextColor: Int = context.resources.getColor(R.color.showmore_color, null)
    private var lessTextColor: Int = context.resources.getColor(R.color.showmore_color, null)


    private var collapsedTextWithSeeMoreButton: String? = null
    private var expandedTextWithSeeMoreButton: String? = null
    private var orignalContent: String = ""

    private var collapsedTextSpannable: SpannableString? = null
    private var expandedTextSpannable: SpannableString? = null

    private var isExpanded = false

    private var seeMore = "SeeMore"
    private var seeLess: String = "SeeLess"

    private var onTextClicked: onMoreClicked? = null

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
                seeLess = getString(R.styleable.ShowMore_seelesstext).toString()
                moreTextColor = getInteger(R.styleable.ShowMore_morecolor, R.color.showmore_color)
                lessTextColor = getInteger(R.styleable.ShowMore_lesscolor, R.color.showmore_color)
                setUpContent()
            } finally {
                recycle()
            }
        }
    }


    fun setOnTextClicked(onTextClicked: onMoreClicked?) {
        this.onTextClicked = onTextClicked
    }

    fun isExpanded(): Boolean {
        return isExpanded
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

    fun setLessTextColor(color: Int) {
        this.lessTextColor = color
        invalidate()
        setUpContent()
    }

    fun expandText(expand: Boolean) {
        if (expand) {
            isExpanded = true
            text = expandedTextSpannable
        } else {
            isExpanded = false
            text = collapsedTextSpannable
        }
    }

    fun setMoreText(seeMoreText: String) {
        seeMore = seeMoreText
        invalidate()
        setUpContent()
    }

    fun setLessText(seeLessText: String) {
        seeLess = seeLessText
        invalidate()
        setUpContent()
    }

    //toggle the state
    fun toggle() {
        if (isExpanded) {
            isExpanded = false
            text = collapsedTextSpannable
        } else {
            isExpanded = true
            text = expandedTextSpannable
        }
    }

    fun setContent(text: String) {
        this.orignalContent = text
        invalidate()
        setUpContent()
    }

    fun setUpContent() {
        this.movementMethod = LinkMovementMethod.getInstance()
        //show see more
        if (orignalContent.length >= textMaxLength) {
            collapsedTextWithSeeMoreButton =
                orignalContent.substring(0, textMaxLength) + "... " + seeMore
            expandedTextWithSeeMoreButton = "$orignalContent $seeLess"

            //creating spannable strings
            collapsedTextSpannable = SpannableString(collapsedTextWithSeeMoreButton)
            expandedTextSpannable = SpannableString(expandedTextWithSeeMoreButton)
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
            expandedTextSpannable!!.setSpan(
                clickableSpan,
                orignalContent.length + 1,
                expandedTextWithSeeMoreButton!!.length,
                0
            )
            expandedTextSpannable!!.setSpan(
                StyleSpan(Typeface.NORMAL),
                orignalContent.length + 1,
                expandedTextWithSeeMoreButton!!.length,
                0
            )
            expandedTextSpannable!!.setSpan(
                RelativeSizeSpan(.9f),
                orignalContent.length + 1,
                expandedTextWithSeeMoreButton!!.length,
                0
            )
            if (isExpanded) setText(expandedTextSpannable) else setText(collapsedTextSpannable)
        } else {
            //to do: don't show see more
            setText(orignalContent)
        }
        setOnClickListener {
            if (onTextClicked != null) {
                if (tag == null || tag != "spanClicked") {
                    onTextClicked!!.onMoreClicked()
                }
            }
            tag = "textClicked"
        }
        setOnLongClickListener {
            if (onTextClicked != null) {
                onTextClicked!!.onMoreLongClicked()
            }
            tag = "textLongClicked"
            false
        }
    }


    var clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            // to prevent toggle when long click on "show more/less"
            tag = if (tag == null || tag != "textLongClicked") {
                toggle()
                "spanClicked"
            } else {
                ""
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            if (isExpanded)
                ds.color = (lessTextColor)
            else
                ds.color = (moreTextColor)
        }
    }
}