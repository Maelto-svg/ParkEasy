package com.example.parkingapp

class ParkingRepository(private val api: ParkingApi) {
    suspend fun getAllLots(): Result<List<ParkingLot>> = runCatching {
        api.getAllLots()
    }

    suspend fun getSpotsByLot(parkingLotId: Long): Result<List<ParkingSpot>> = runCatching {
        api.getSpotsByLot(parkingLotId)
    }

    suspend fun reserveSpot(spotId: Long): Result<Unit> = runCatching {
        val response = api.reserveSpot(spotId)
        if (!response.isSuccessful) {
            throw Exception("Failed to reserve spot: ${response.code()}")
        }
    }
}