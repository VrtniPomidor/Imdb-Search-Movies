package com.arsfutura.imdb.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arsfutura.imdb.presentation.common.Constants
import com.arsfutura.imdb.presentation.favorites.FavoritesActivity
import com.arsfutura.imdb.presentation.favorites.FavoritesFragment
import com.arsfutura.imdb.presentation.moviedetails.MovieDetailsActivity
import com.arsfutura.imdb.presentation.moviedetails.MovieDetailsFragment
import com.arsfutura.imdb.presentation.movielist.MovieListActivity
import com.arsfutura.imdb.presentation.movielist.MovieListFragment


abstract class BaseActivity : AppCompatActivity() {
    private val bundleKey: String = "bundle_key"

    fun navigate() {
        mapDestinations()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun navigate(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(getPlaceholderId(), fragment)
            .commit()
    }

    abstract fun getPlaceholderId(): Int

    private fun mapDestinations() {
        when (this) {
            is FavoritesActivity -> navigate(FavoritesFragment.newInstance())
            is MovieListActivity -> navigate(MovieListFragment.newInstance())
            is MovieDetailsActivity -> navigate(
                MovieDetailsFragment.newInstance(
                    intent!!.extras!!.getString(Constants.idKey)!!
                )
            )
            else -> throw RuntimeException("Unimplemented")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(bundleKey, intent.extras)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getBundle(bundleKey)?.let {
            intent.putExtras(it)
        }
    }
}