package com.example.JavaCodingExam.Entity;

import javax.validation.constraints.Pattern;

import javax.persistence.*;

@Entity
public class Vehicle {
    @Id
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "License plate can only contain letters, numbers, and dashes.")
    private String licensePlate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
    @Column(nullable = false)
    private String ownerName;

    @ManyToOne
    @JoinColumn(name = "parkinglot_id")
    private ParkingLot parkingLot;

    public Vehicle() {}

    public Vehicle(String licensePlate, VehicleType vehicleType, String ownerName) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.ownerName = ownerName;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
