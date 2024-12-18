package com.example.parkingapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "your_table_name")
data class YourEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val age: Int
)
