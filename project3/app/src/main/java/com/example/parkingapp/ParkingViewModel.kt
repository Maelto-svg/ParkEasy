package com.example.parkingapp

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

    private var currentLotId: Long = 1

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.getAllLots()
                .onSuccess { lotsList ->
                    _lots.value = lotsList
                    if (lotsList.isNotEmpty()) {
                        currentLotId = lotsList.first().id
                        loadSpotsByLot(currentLotId)
                    }
                }
                .onFailure { exception ->
                    _error.value = "Failed to load lots: ${exception.message}"
                }
        }
    }

    fun loadSpotsByLot(lotId: Long) {
        currentLotId = lotId
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

    fun reserveSpot(spotId: Long) {
        viewModelScope.launch {
            repository.reserveSpot(spotId)
                .onSuccess { _ ->
                    // Reload spots after successful reservation
                    loadSpotsByLot(currentLotId)
                }
                .onFailure { exception ->
                    _error.value = "Failed to reserve spot: ${exception.message}"
                }
        }
    }

    fun retryLoading() {
        _error.value = null
        loadInitialData()
    }
}