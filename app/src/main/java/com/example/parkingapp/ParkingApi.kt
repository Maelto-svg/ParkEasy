package com.example.parkingapp

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.Response

interface ParkingApi {
    @GET("lots")
    suspend fun getAllLots(): List<ParkingLot>

    @GET("{parkingLotId}/spots")
    suspend fun getSpotsByLot(@Path("parkingLotId") parkingLotId: Long): List<ParkingSpot>

    @GET("{parkingLotId}/available-spots")
    suspend fun getAvailableSpots(@Path("parkingLotId") parkingLotId: Long): List<ParkingSpot>

    @PUT("spots/{id}/reserve")
    suspend fun reserveSpot(@Path("id") spotId: Long): ParkingSpot
}