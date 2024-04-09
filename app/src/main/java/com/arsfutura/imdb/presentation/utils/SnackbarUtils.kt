package com.arsfutura.imdb.presentation.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.arsfutura.imdb.R
import com.google.android.material.snackbar.Snackbar

fun showErrorSnackbar(view: View, context: Context, error: String) {
    val msg = error.ifBlank { context.getString(R.string.something_went_wrong) }
    val snackBar =
        Snackbar.make(
            view,
            msg,
            Snackbar.LENGTH_SHORT
        )
    snackBar.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
    snackBar.setBackgroundTint(ContextCompat.getColor(context, android.R.color.black))
    snackBar.show()
}