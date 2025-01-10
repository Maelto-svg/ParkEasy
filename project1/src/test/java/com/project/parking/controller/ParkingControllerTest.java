package com.project.parking.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;

import com.project.parking.entity.ParkingLot;
import com.project.parking.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingService parkingService;

    @Test
    public void testGetAllParkingLots() throws Exception {
        // Configuration du mock pour retourner une liste vide
        when(parkingService.getAllParkingLots()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/parking/lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(parkingService, times(1)).getAllParkingLots();
    }
}
