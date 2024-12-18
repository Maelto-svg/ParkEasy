package com.example.parkingapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LOT_DB")
data class Lot(
    @PrimaryKey(autoGenerate = true) var id:Long,
    val name:String,
    val location:String,
    val capacity:Long
)