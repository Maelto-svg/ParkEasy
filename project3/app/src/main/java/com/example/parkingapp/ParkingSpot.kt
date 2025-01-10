package com.example.parkingapp

data class ParkingSpot(
    val id: Long,
    val spotNumber: String,
    val isOccupied: Boolean,
    val vehicle: String?,
    val lot: String
) {
    val status: SpotStatus
        get() = when {
            isOccupied && vehicle != null -> SpotStatus.OCCUPIED
            isOccupied && vehicle == null -> SpotStatus.RESERVED
            else -> SpotStatus.AVAILABLE
        }
}