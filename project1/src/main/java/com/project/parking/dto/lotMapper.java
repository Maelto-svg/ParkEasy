package com.project.parking.dto;

import com.project.parking.entity.ParkingLot;
import com.project.parking.entity.ParkingSpot;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class lotMapper {
    public static lot of(ParkingLot parkingLot) {
        List<String> spot = new ArrayList<String>().stream().toList();

        try {
            spot = parkingLot.getSpots().stream()
                    .map(sp -> sp.getSpotNumber())
                    .toList();
        }
        catch (Exception ignored){

        }

        return new lot(
                parkingLot.getId(),
                parkingLot.getName(),
                parkingLot.getLocation(),
                parkingLot.getCapacity(),
                spot
        );
    }
}
