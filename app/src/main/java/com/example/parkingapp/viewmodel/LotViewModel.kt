package com.example.parkingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.dao.LotDao
import com.example.parkingapp.entity.Lot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LotViewModel(private val lotDao: LotDao) : ViewModel() {

    // StateFlow to observe list of lots
    private val _lots = MutableStateFlow<List<Lot>>(emptyList())
    val lots: StateFlow<List<Lot>> get() = _lots

    // Load all lots
    fun loadLots() {
        viewModelScope.launch {
            _lots.value = lotDao.findAll()
        }
    }

    // Add a new Lot
    fun insertLot(lot: Lot) {
        viewModelScope.launch {
            lotDao.insertLot(lot)
            loadLots() // Refresh data after insert
        }
    }

    // Update a Lot
    fun updateLot(lot: Lot) {
        viewModelScope.launch {
            lotDao.updateLot(lot)
            loadLots() // Refresh data after update
        }
    }

    // Delete a Lot
    fun deleteLot(lot: Lot) {
        viewModelScope.launch {
            lotDao.deleteLot(lot)
            loadLots() // Refresh data after delete
        }
    }

    // Find a specific Lot by ID
    suspend fun findLotById(id: Long): Lot? {
        return lotDao.findById(id)
    }

    companion object {
        class Factory(private val lotDao: LotDao) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LotViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LotViewModel(lotDao) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
