package com.example.parkingapp

import androidx.room.Database
import androidx.room.RoomDatabase

import android.content.Context
import androidx.room.Room
import com.example.parkingapp.dao.SpotDao
import com.example.parkingapp.entity.Spot

@Database(entities = [Spot::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun spotDao(): SpotDao
}

fun getExternalDatabase(context: Context): AppDatabase {
    // Get the path for your internal storage
    val dbFile = context.getDatabasePath("database_parking.db.sql") // Use your actual database file name here

    // If the database doesn't exist, copy it from assets to internal storage
    if (!dbFile.exists()) {
        context.assets.open("database.db").use { inputStream ->
            dbFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    return Room.databaseBuilder(context, AppDatabase::class.java, dbFile.absolutePath)
        .createFromFile(dbFile)  // This ensures Room uses the existing file
        .build()
}
