package com.example.parkingapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.parkingapp.entity.Spot

@Dao
interface SpotDao {
    @Insert
    suspend fun insertSpot(spot: Spot)

    @Query("SELECT * FROM SPOT_DB")
    suspend fun findAll(): List<Spot>

    @Query("SELECT * FROM SPOT_DB WHERE id = :id")
    suspend fun findById(id: Long): Spot?

    @Query("UPDATE SPOT_DB SET isoccupied = :status WHERE id = :id")
    suspend fun updateSpotStatus(id: Long, status: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(spot: Spot)

    @Update
    suspend fun update(spot: Spot)

    @Delete
    suspend fun delete(spot: Spot)
}