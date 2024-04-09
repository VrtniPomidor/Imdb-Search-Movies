package com.arsfutura.imdb.presentation.moviedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arsfutura.imdb.R
import com.arsfutura.imdb.databinding.ActivityMainBinding
import com.arsfutura.imdb.presentation.BaseActivity
import com.arsfutura.imdb.presentation.common.Constants

class MovieDetailsActivity : BaseActivity() {
    companion object {
        fun createIntent(context: Context, id: String): Intent {
            return Intent(context, MovieDetailsActivity::class.java).apply {
                putExtras(Bundle().apply { putString(Constants.idKey, id) })
            }
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigate()
        setAppBar()
    }

    private fun setAppBar() {
        setSupportActionBar(binding.toolbar.mainToolbar)
        supportActionBar?.setTitle(R.string.movie_details_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun getPlaceholderId(): Int {
        return R.id.fragment_placeholder
    }
}