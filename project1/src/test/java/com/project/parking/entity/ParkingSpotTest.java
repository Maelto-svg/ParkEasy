package com.project.parking.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ParkingSpotTest {

    @Test
    public void testParkingSpotCreation() {
        ParkingSpot spot = new ParkingSpot();
        spot.setSpotNumber("A1");
        spot.setOccupied(false);

        assertEquals("A1", spot.getSpotNumber());
        assertFalse(spot.getOccupied());
    }
}
