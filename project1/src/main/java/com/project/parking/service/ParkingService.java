package com.project.parking.service;

import com.project.parking.dto.lot;
import com.project.parking.dto.lotMapper;
import com.project.parking.dto.spot;
import com.project.parking.dto.spotMapper;
import com.project.parking.entity.ParkingSpot;
import com.project.parking.entity.ParkingLot;
import com.project.parking.repository.ParkingLotRepository;
import com.project.parking.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ParkingService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    // Récupérer tous les parkings
    public List<lot> getAllParkingLots() {
        List<ParkingLot> pl = parkingLotRepository.findAll();
        return pl.stream().map(lotMapper::of).toList();
    }

    public List<spot> getParkingSpotsFromLot(Long parkingLotId) {
        Stream<ParkingSpot> sp = parkingSpotRepository.findAll().stream().filter(s -> s.getLot().getId().equals(parkingLotId));
        return sp.map(spotMapper::of).toList();
    }

    // Récupérer toutes les places libres dans un parking
    public List<spot> getAvailableSpots(Long parkingLotId) {
        List<ParkingSpot> sp = parkingSpotRepository.findByIsOccupied(false)
                .stream().filter(s -> Objects.equals(s.getLot().getId(), parkingLotId)).toList();
        return sp.stream().map(spotMapper::of).toList();
    }

    // Réserver une place pour un véhicule
    public Optional<ParkingSpot> reserveSpot(Long parkingSpotId) {
        Optional<ParkingSpot> spot = parkingSpotRepository.findById(parkingSpotId);
        spot.ifPresent(s -> s.setOccupied(true));
        spot.ifPresent(parkingSpotRepository::save);
        return spot;
    }

    // Libérer une place
    public void releaseSpot(Long parkingSpotId) {
        Optional<ParkingSpot> spot = parkingSpotRepository.findById(parkingSpotId);
        spot.ifPresent(s -> s.setOccupied(false));
        spot.ifPresent(parkingSpotRepository::save);
    }

    public List<spot> findAll() {
        List<ParkingSpot> sp = parkingSpotRepository.findAll();
        return sp.stream().map(spotMapper::of).toList();
    }
}

