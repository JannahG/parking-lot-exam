package com.example.JavaCodingExam.Service;

import com.example.JavaCodingExam.Entity.ParkingLot;
import com.example.JavaCodingExam.Entity.Vehicle;
import com.example.JavaCodingExam.Repository.ParkingLotRepository;
import com.example.JavaCodingExam.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public ParkingLot registerParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void checkInVehicle(String lotId, Vehicle vehicle) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Parking lot not found"));

        if (!parkingLot.hasAvailableSpace()) {
            throw new RuntimeException("Parking lot is full");
        }

        if (vehicle.getParkingLot() != null) {
            throw new RuntimeException("Vehicle is already parked in another lot");
        }

        vehicle.setParkingLot(parkingLot);
        parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() + 1);

        vehicleRepository.save(vehicle);
        parkingLotRepository.save(parkingLot);
    }

    public void checkOutVehicle(String lotId, Vehicle vehicle) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Parking lot not found"));

        if (vehicle.getParkingLot() == null || !vehicle.getParkingLot().equals(parkingLot)) {
            throw new RuntimeException("Vehicle is not parked in this lot");
        }

        vehicle.setParkingLot(null);
        parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() - 1);

        vehicleRepository.save(vehicle);
        parkingLotRepository.save(parkingLot);
    }

    public String getParkingLotAvailability(String lotId) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Parking lot not found"));
        return "Available spaces: " + (parkingLot.getCapacity() - parkingLot.getOccupiedSpaces());
    }

    public List<Vehicle> getVehiclesInParkingLot(String lotId) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Parking lot not found"));
        return parkingLot.getVehicles();
    }
}
