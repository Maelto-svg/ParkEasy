package com.example.parkingapp

import retrofit2.Response
import retrofit2.http.*

interface ParkingApi {
    @GET("lots")
    suspend fun getAllLots(): List<ParkingLot>

    @GET("{parkingLotId}/spots")
    suspend fun getSpotsByLot(@Path("parkingLotId") parkingLotId: Long): List<ParkingSpot>

    @PUT("spots/{id}/reserve")
    suspend fun reserveSpot(@Path("id") spotId: Long): Response<Void>
}