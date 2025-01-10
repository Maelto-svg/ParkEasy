package com.project.parking.controller;

import com.project.parking.dto.lot;
import com.project.parking.dto.spot;
import com.project.parking.entity.ParkingLot;
import com.project.parking.entity.ParkingSpot;
import com.project.parking.repository.ParkingSpotRepository;
import com.project.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    // Endpoint pour obtenir tous les parkings
    @GetMapping("/lots")
    public List<lot> getAllParkingLots() {
        return parkingService.getAllParkingLots();
    }

    // Endpoint pour obtenir les places disponibles d'un parking
    @GetMapping("/{parkingLotId}/available-spots")
    public List<spot> getAvailableSpots(@PathVariable Long parkingLotId) {
        return parkingService.getAvailableSpots(parkingLotId);
    }

    @GetMapping("/{parkingLotId}/spots")
    public List<spot> getTheSpotsFromOneLot(@PathVariable Long parkingLotId) {
        return parkingService.getParkingSpotsFromLot(parkingLotId);
    }

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @GetMapping("/spots")
    public List<spot> getAllParkingSpots() {
        return parkingService.findAll();
    }

    @PutMapping("/spots/{id}/reserve")
    public ResponseEntity<?> reserveSpot(@PathVariable Long id) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        if (spot.getOccupied()) {
            return ResponseEntity.badRequest().body("Spot is already reserved or occupied.");
        }

        spot.setOccupied(true); // Mark spot as occupied
        parkingSpotRepository.save(spot);

        return ResponseEntity.ok("Spot reserved successfully");
    }

    @PutMapping("/spots/{id}/occupy")
    public ResponseEntity<?> occupySpot(@PathVariable Long id) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        if (spot.getOccupied()) {
            return ResponseEntity.badRequest().body("Spot is already reserved or occupied.");
        }

        spot.setOccupied(true); // Mark spot as occupied
        parkingSpotRepository.save(spot);

        return ResponseEntity.ok("Spot occupied successfully");
    }

    @PutMapping("/spots/{id}/free")
    public ResponseEntity<?> freeSpot(@PathVariable Long id) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        if (!spot.getOccupied()) {
            return ResponseEntity.badRequest().body("Spot is already free");
        }

        spot.setOccupied(false); // Mark spot as occupied
        parkingSpotRepository.save(spot);

        return ResponseEntity.ok("Spot freed successfully");
    }
}

