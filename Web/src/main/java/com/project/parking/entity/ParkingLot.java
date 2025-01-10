package com.project.parking.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "SP_PARKINGSLOT")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String location;

    @Column
    private Integer capacity;

    @OneToMany(mappedBy = "lot")
    private List<ParkingSpot> spots;

    public ParkingLot() {
    }

    public ParkingLot(String name, Long id, String location, Integer capacity) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.capacity = capacity;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public void setSpots(List<ParkingSpot> spots) {
        this.spots = spots;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
