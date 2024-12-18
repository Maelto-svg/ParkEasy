package com.example.parkingapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.parkingapp.entity.Lot
import com.example.parkingapp.entity.Spot

@Dao
interface LotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLot(lot: Lot)

    @Query("SELECT * FROM LOT_DB")
    suspend fun findAll(): List<Lot>

    @Query("SELECT * FROM LOT_DB WHERE id = :id")
    suspend fun findById(id: Long): Lot?

    @Update
    suspend fun updateLot(lot: Lot)

    @Delete
    suspend fun deleteLot(lot: Lot)
}
