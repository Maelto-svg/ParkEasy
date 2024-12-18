package com.example.parkingapp

import android.app.Application

class ParkingApplication : Application() {
    val database: ParkingDatabase by lazy { ParkingDatabase.getDatabase(this) }

}