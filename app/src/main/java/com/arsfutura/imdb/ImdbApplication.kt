package com.arsfutura.imdb

import android.app.Application
import com.arsfutura.imdb.di.applicationModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

@Suppress("unused")
class ImdbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ImdbApplication)
            modules(applicationModules)
        }
    }
}