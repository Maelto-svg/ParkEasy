package com.project.parking.repository;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.project.parking.entity.ParkingSpot;

import java.util.List;

@DataJpaTest
public class ParkingSpotRepositoryTest {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Test
    public void testFindByIsOccupied() {
        ParkingSpot spot = new ParkingSpot();
        spot.setId(1L);
        spot.setSpotNumber("A1");
        spot.setOccupied(false);
        parkingSpotRepository.save(spot);

        List<ParkingSpot> availableSpots = parkingSpotRepository.findByIsOccupied(false);
        assertFalse(availableSpots.isEmpty());
        assertEquals("A1", availableSpots.get(0).getSpotNumber());
    }
}
