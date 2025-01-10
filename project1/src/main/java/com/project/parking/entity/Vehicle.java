package com.project.parking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SP_VEHICLE")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate")
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    private VehicleType type; // Enum pour différencier les types de véhicules (voiture, moto, etc.)

    @OneToOne
    private ParkingSpot spot; // Association avec la place de parking occupée

    // Constructeurs
    public Vehicle() {}

    public Vehicle(String licensePlate, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.type = vehicleType;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public void setSpot(ParkingSpot spot) {
        this.spot = spot;
    }
}

