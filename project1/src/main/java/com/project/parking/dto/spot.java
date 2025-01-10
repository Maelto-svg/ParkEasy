package com.project.parking.dto;

import com.project.parking.entity.ParkingLot;
import com.project.parking.entity.Vehicle;

public record spot(Long id, String spotNumber, Boolean isOccupied, String vehicle, String lot) {
}
