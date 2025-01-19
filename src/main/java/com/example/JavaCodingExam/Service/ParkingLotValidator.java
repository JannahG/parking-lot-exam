package com.example.JavaCodingExam.Service;

import com.example.JavaCodingExam.Controller.Response.ErrorResponse;
import com.example.JavaCodingExam.Entity.ParkingLot;
import com.example.JavaCodingExam.Entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class ParkingLotValidator {
    public ErrorResponse ValidateCheckInVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return new ErrorResponse(404, "Vehicle does not exist");
        }

        if (vehicle.getParkingLot() != null) {
            return new ErrorResponse(400, "Vehicle is already parked in another lot");
        }

        return null;
    }

    public ErrorResponse ValidateParkingLotCheckInVehicle(ParkingLot parkingLot) {
        if (!parkingLot.hasAvailableSpace()) {
            return new ErrorResponse(400, "Parking lot is full");
        }

        return null;
    }

    public ErrorResponse ValidateCheckOutVehicle(Vehicle vehicle, ParkingLot parkingLot) {
        if (vehicle.getParkingLot() == null || !vehicle.getParkingLot().equals(parkingLot)) {
            throw new RuntimeException("Vehicle is not parked in this lot");
        }

        return null;
    }
}
