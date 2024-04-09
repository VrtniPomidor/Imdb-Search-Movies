package com.arsfutura.imdb.db

import android.app.Application
import androidx.room.Room
import com.arsfutura.imdb.data.local.FavoriteDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
}

fun provideDataBase(application: Application): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "app-database")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideDao(dataBase: AppDatabase): FavoriteDao {
    return dataBase.favoriteDao()
}
