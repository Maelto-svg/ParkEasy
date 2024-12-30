package com.example.parkingapp

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ParkingRepository(private val api: ParkingApi) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    suspend fun getAllLots(): Result<List<ParkingLot>> = runCatching {
        api.getAllLots()
    }

    suspend fun getSpotsByLot(parkingLotId: Long): Result<List<ParkingSpot>> = runCatching {
        api.getSpotsByLot(parkingLotId)
    }

    suspend fun getAvailableSpots(parkingLotId: Long): Result<List<ParkingSpot>> = runCatching {
        api.getAvailableSpots(parkingLotId)
    }

    suspend fun reserveSpot(spotId: Long): Result<ParkingSpot> = runCatching {
        api.reserveSpot(spotId)
    }
}