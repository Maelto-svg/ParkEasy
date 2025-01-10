package com.project.parking.dto;

import com.project.parking.entity.Vehicle;

public class vehicleMapper {
    public static vehicle of(Vehicle vhcl) {
        return new vehicle(
                vhcl.getId(),
                vhcl.getLicensePlate(),
                vhcl.getType(),
                vhcl.getSpot().getSpotNumber()
        );
    }
}
