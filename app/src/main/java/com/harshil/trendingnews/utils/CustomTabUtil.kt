package com.harshil.trendingnews.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun Context.launchCustomTab(url: String) {
    CustomTabsIntent
        .Builder()
        .build()
        .launchUrl(this, Uri.parse(url))
}