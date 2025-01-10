package com.project.parking.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.project.parking.entity.ParkingSpot;
import com.project.parking.entity.Vehicle;
import com.project.parking.repository.ParkingSpotRepository;
import com.project.parking.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Test
    public void testAddVehicleToSpot() {
        ParkingSpot spot = new ParkingSpot();
        spot.setId(1L);
        spot.setOccupied(false);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("ABC123");

        when(parkingSpotRepository.findById(1L)).thenReturn(Optional.of(spot));

        vehicleService.addVehicleToSpot(vehicle, 1L);

        assertTrue(spot.getOccupied());
        verify(vehicleRepository, times(1)).save(vehicle);
        verify(parkingSpotRepository, times(1)).save(spot);
    }

    @Test
    public void testRemoveVehicleFromSpot() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("ABC123");

        ParkingSpot spot = new ParkingSpot();
        spot.setVehicle(vehicle);
        spot.setOccupied(true);

        vehicle.setSpot(spot);

        when(vehicleRepository.findByLicensePlate("ABC123")).thenReturn(vehicle);

        vehicleService.removeVehicleFromSpot("ABC123");

        assertNull(spot.getVehicle());
        assertFalse(spot.getOccupied());
        verify(vehicleRepository, times(1)).delete(vehicle);
        verify(parkingSpotRepository, times(1)).save(spot);
    }
}
