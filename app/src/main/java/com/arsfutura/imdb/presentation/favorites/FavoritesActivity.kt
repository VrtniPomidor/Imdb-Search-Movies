package com.arsfutura.imdb.presentation.favorites

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arsfutura.imdb.R
import com.arsfutura.imdb.databinding.ActivityMainBinding
import com.arsfutura.imdb.presentation.BaseActivity

class FavoritesActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigate()
        setAppBar()
    }

    private fun setAppBar() {
        setSupportActionBar(binding.toolbar.mainToolbar)
        supportActionBar?.setTitle(R.string.favorites_title)
    }

    override fun getPlaceholderId(): Int {
        return R.id.fragment_placeholder
    }
}