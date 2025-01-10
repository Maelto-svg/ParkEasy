package com.project.parking.repository;

import com.project.parking.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findByIsOccupied(boolean isOccupied); // Rechercher les places libres ou occupées
}

