package com.project.parking.repository;

import com.project.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByLicensePlate(String licensePlate); // Rechercher un véhicule par plaque d'immatriculation
}
