package com.soyvictorherrera.bdates.utils.extensions

import android.content.Context
import android.content.Intent
import com.soyvictorherrera.bdates.R
import kotlin.math.min

fun Context.shareText(sharedText: String, sharingTitle: String? = null) {
    val title = sharingTitle ?: getString(
        R.string.ellipsis_text,
        sharedText.substring(0, min(sharedText.length, 60))
    )
    val sendIntent: Intent = Intent()
        .setAction(Intent.ACTION_SEND)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_TITLE, title)
        .putExtra(Intent.EXTRA_TEXT, sharedText)
    val shareIntent = Intent.createChooser(sendIntent, title)
    startActivity(shareIntent)
}
