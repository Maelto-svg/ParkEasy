package com.example.parkingapp

import android.content.Context
import androidx.room.Room

class AppDatabaseProvider(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "app-database"
    ).build()

    val spotDao = db.spotDao()
}

