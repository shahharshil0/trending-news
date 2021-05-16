package com.harshil.trendingnews.utils

import android.graphics.Color
import android.text.*
import android.text.style.ClickableSpan
import android.view.View

operator fun Spannable.plus(other: Spannable): Spannable {
    return SpannableStringBuilder(this).append(other)
}

/**
 * Append clickable span  to the actual text
 */
fun appendClickableString(
    actualString: String,
    appendString: String,
    block: () -> Unit
): Spannable {
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            block()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = Color.BLUE
            ds.isUnderlineText = false
        }
    }

    val actualSpannable = SpannableString("$actualString ")
    //make appended string clickable
    val appendSpannable = SpannableString(appendString)
    appendSpannable.setSpan(
        clickableSpan,
        0,
        appendString.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return actualSpannable + appendSpannable
}