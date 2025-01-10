package com.project.parking.repository;

import com.project.parking.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
    // Méthodes de requêtes personnalisées si nécessaire
}

