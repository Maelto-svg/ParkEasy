package com.example.parkingapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.parkingapp.dao.LotDao
import com.example.parkingapp.dao.SpotDao
import com.example.parkingapp.entity.Lot
import com.example.parkingapp.entity.Spot


@Database(entities = [Spot::class, Lot::class], version = 4, exportSchema = false)
abstract class ParkingDatabase : RoomDatabase() {
    abstract fun spotDao(): SpotDao
    abstract fun lotDao(): LotDao // Added LotDao

    companion object {
        @Volatile
        private var INSTANCE: ParkingDatabase? = null

        fun getDatabase(context: Context): ParkingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParkingDatabase::class.java,
                    "parking_database"
                )
                    .fallbackToDestructiveMigration() // Clears old data on schema change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
