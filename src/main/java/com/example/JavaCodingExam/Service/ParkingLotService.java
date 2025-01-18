package com.example.JavaCodingExam.Service;

import com.example.JavaCodingExam.Controller.Response.ErrorResponse;
import com.example.JavaCodingExam.Controller.Response.SuccessResponse;
import com.example.JavaCodingExam.Entity.ParkingLot;
import com.example.JavaCodingExam.Entity.Vehicle;
import com.example.JavaCodingExam.Repository.ParkingLotRepository;
import com.example.JavaCodingExam.Repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotService {
    private final ParkingLotRepository parkingLotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingValidator parkingValidator;

    public ParkingLotService(ParkingLotRepository parkingLotRepository, VehicleRepository vehicleRepository, ParkingValidator parkingValidator) {
        this.parkingLotRepository = parkingLotRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingValidator = parkingValidator;
    }

    public ParkingLot registerParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public ResponseEntity<?> checkInVehicle(String lotId, Vehicle vehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(vehicle.getLicensePlate()).orElse(null);
        ErrorResponse vehicleError = parkingValidator.ValidateCheckInVehicle(existingVehicle);
        if (vehicleError != null) {
            return new ResponseEntity<>(vehicleError, HttpStatus.valueOf(vehicleError.getHttpStatusCode()));
        }

        ParkingLot parkingLot = parkingLotRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Parking lot not found"));
        ErrorResponse parkingLotError = parkingValidator.ValidateParkingLotCheckInVehicle(parkingLot);
        if (parkingLotError != null) {
            return new ResponseEntity<>(parkingLotError, HttpStatus.valueOf(parkingLotError.getHttpStatusCode()));
        }

        existingVehicle.setParkingLot(parkingLot);
        parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() + 1);

        vehicleRepository.save(existingVehicle);
        parkingLotRepository.save(parkingLot);

        SuccessResponse successResponse = new SuccessResponse(200, "Vehicle successfully checked into parking lot", Optional.of(existingVehicle));
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> checkOutVehicle(String lotId, Vehicle vehicle) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Parking lot not found"));
        Vehicle existingVehicle = vehicleRepository.findById(vehicle.getLicensePlate()).orElse(null);
        ErrorResponse vehicleError = parkingValidator.ValidateCheckOutVehicle(existingVehicle, parkingLot);
        if (vehicleError != null) {
            return new ResponseEntity<>(vehicleError, HttpStatus.valueOf(vehicleError.getHttpStatusCode()));
        }

        existingVehicle.setParkingLot(null);
        parkingLot.getVehicles().remove(existingVehicle);
        parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() - 1);

        vehicleRepository.save(existingVehicle);
        parkingLotRepository.save(parkingLot);

        SuccessResponse successResponse = new SuccessResponse(200, "Vehicle successfully checked out the parking lot", Optional.of(existingVehicle));
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public ResponseEntity<SuccessResponse> getParkingLotAvailability(String lotId) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Parking lot not found"));
        SuccessResponse successResponse = new SuccessResponse(200, "Available spaces: " + (parkingLot.getCapacity() - parkingLot.getOccupiedSpaces()), null);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public List<Vehicle> getVehiclesInParkingLot(String lotId) {
        ParkingLot parkingLot = parkingLotRepository.findById(lotId).orElseThrow(() -> new RuntimeException("Parking lot not found"));
        return parkingLot.getVehicles();
    }
}
