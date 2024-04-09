package com.arsfutura.imdb.presentation.utils

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import com.arsfutura.imdb.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun loadPicasso(imageUrl: String, target: ImageView, loading: ProgressBar) {
    if (imageUrl.isNotBlank()) {
        loading.visibility = VISIBLE
        loading.bringToFront()
        Picasso.get().load(imageUrl)
            .error(android.R.drawable.ic_menu_report_image)
            .into(target, object : Callback {
                override fun onSuccess() {
                    loading.visibility = GONE
                }

                override fun onError(e: Exception?) {
                    loading.visibility = GONE
                }
            })
    }
}

@DrawableRes
fun getFavoriteIcon(isFavorite: Boolean): Int {
    return if (isFavorite) {
        R.drawable.baseline_favorite_24
    } else {
        R.drawable.baseline_favorite_border_24
    }
}
