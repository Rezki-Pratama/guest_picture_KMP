package com.sloth.proactify.di

import androidx.room.Room
import com.sloth.proactify.data.local.room.AppDatabase
import org.koin.dsl.module

val dataModule = module {
    // Realm instance
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "proactify"
        ).build()
    }

    single { get<AppDatabase>().getTaskDao() }
}