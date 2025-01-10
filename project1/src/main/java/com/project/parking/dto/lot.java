package com.project.parking.dto;

import java.util.List;

public record lot(Long id, String name, String location, Integer capacity, List<String> spots) {
}
