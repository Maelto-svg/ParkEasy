package com.project.parking.service;

import com.project.parking.entity.ParkingSpot;
import com.project.parking.entity.Vehicle;
import com.project.parking.repository.ParkingSpotRepository;
import com.project.parking.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    // Ajouter un véhicule à une place
    public void addVehicleToSpot(Vehicle vehicle, Long parkingSpotId) {
        ParkingSpot spot = parkingSpotRepository.findById(parkingSpotId)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        if (!spot.getOccupied()) {
            spot.setOccupied(true);
            spot.setVehicle(vehicle); // Liaison bidirectionnelle si nécessaire
            vehicle.setSpot(spot);
            vehicleRepository.save(vehicle);
            parkingSpotRepository.save(spot);
        } else {
            throw new RuntimeException("Parking spot is already occupied");
        }
    }

    // Retirer un véhicule d'une place
    public void removeVehicleFromSpot(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicle != null && vehicle.getSpot() != null) {
            ParkingSpot spot = vehicle.getSpot();
            spot.setOccupied(false);
            spot.setVehicle(null);
            vehicle.setSpot(null);
            parkingSpotRepository.save(spot);
            vehicleRepository.delete(vehicle);
        } else {
            throw new RuntimeException("Vehicle or parking spot not found");
        }
    }
}

