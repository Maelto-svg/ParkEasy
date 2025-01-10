package com.project.parking.dto;

import com.project.parking.entity.ParkingSpot;
import com.project.parking.entity.VehicleType;

public record vehicle(Long id, String licensePlate, VehicleType type, String spot) {
}
