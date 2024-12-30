package com.example.parkingapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParkingViewModel : ViewModel() {
    private val repository = ParkingRepository(RetrofitClient.parkingApi)

    private val _lots = MutableStateFlow<List<ParkingLot>>(emptyList())
    val lots: StateFlow<List<ParkingLot>> = _lots.asStateFlow()

    private val _spots = MutableStateFlow<List<ParkingSpot>>(emptyList())
    val spots: StateFlow<List<ParkingSpot>> = _spots.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                repository.getAllLots()
                    .onSuccess { lotsList ->
                        _lots.value = lotsList
                        if (lotsList.isNotEmpty()) {
                            loadSpotsByLot(lotsList.first().id)
                        }
                    }
                    .onFailure { exception ->
                        _error.value = "Failed to load lots: ${exception.message}"
                    }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    fun loadSpotsByLot(lotId: Long) {
        viewModelScope.launch {
            repository.getSpotsByLot(lotId)
                .onSuccess { spotsList ->
                    _spots.value = spotsList
                }
                .onFailure { exception ->
                    _error.value = "Failed to load spots: ${exception.message}"
                }
        }
    }

    fun updateSpotStatus(spotId: Long, isOccupied: Boolean) {
        viewModelScope.launch {
            repository.reserveSpot(spotId)
                .onSuccess { updatedSpot ->
                    val currentSpots = _spots.value.toMutableList()
                    val index = currentSpots.indexOfFirst { it.id == spotId }
                    if (index != -1) {
                        currentSpots[index] = updatedSpot
                        _spots.value = currentSpots
                    }
                }
                .onFailure { exception ->
                    _error.value = "Failed to update spot: ${exception.message}"
                }
        }
    }

    fun retryLoading() {
        _error.value = null
        loadInitialData()
    }
}