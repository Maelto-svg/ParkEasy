package com.example.parkingapp

data class ParkingLot(
    val id: Long,
    val name: String,
    val location: String,
    val capacity: Int,
    val spots: List<String>
)