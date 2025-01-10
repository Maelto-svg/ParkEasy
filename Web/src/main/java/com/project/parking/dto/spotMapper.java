package com.project.parking.dto;

import com.project.parking.entity.ParkingSpot;

public class spotMapper {
    public static spot of(ParkingSpot parkingSpot) {
        String vhc = null;
        try{
            vhc = parkingSpot.getVehicle().getLicensePlate();
        } catch (Exception ignored) {

        }
        return new spot(
                parkingSpot.getId(),
                parkingSpot.getSpotNumber(),
                parkingSpot.getOccupied(),
                vhc,
                parkingSpot.getLot().getName()
        );
    }
}
