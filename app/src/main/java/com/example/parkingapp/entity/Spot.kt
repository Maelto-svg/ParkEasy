package com.example.parkingapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SPOT_DB")
data class Spot(
    @PrimaryKey(autoGenerate = true) val id:Long,
    val spotnumber:Long,
    val isoccupied:Boolean,
    val lot_id:Long
)

