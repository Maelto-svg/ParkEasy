package com.example.parkingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.dao.SpotDao
import com.example.parkingapp.entity.Spot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpotViewModel(private val spotDao: SpotDao) : ViewModel() {

    fun insertSpot(spot: Spot) {
        viewModelScope.launch(Dispatchers.IO) {
            spotDao.save(spot)
        }
    }

    fun updateSpot(spot: Spot) {
        viewModelScope.launch(Dispatchers.IO) {
            spotDao.update(spot)
        }
    }

    fun deleteSpot(spot: Spot) {
        viewModelScope.launch(Dispatchers.IO) {
            spotDao.delete(spot)
        }
    }

    suspend fun getAllSpots(): List<Spot> = spotDao.findAll()
    suspend fun getSpotById(id: Long): Spot? = spotDao.findById(id)
}
