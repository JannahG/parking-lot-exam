package com.example.JavaCodingExam.Controller;

import com.example.JavaCodingExam.Entity.ParkingLot;
import com.example.JavaCodingExam.Entity.Vehicle;
import com.example.JavaCodingExam.Service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
public class ParkingLotController {
    @Autowired
    ParkingLotService parkingLotService;

    @PostMapping
    public ResponseEntity<ParkingLot> createParkingLot(@RequestBody ParkingLot parkingLot) {
        ParkingLot createdParkingLot = parkingLotService.registerParkingLot(parkingLot);
        return new ResponseEntity<>(createdParkingLot, HttpStatus.OK);
    }

    @PostMapping("/{lotId}/check-in")
    public void checkInVehicle(@PathVariable String lotId, @RequestBody Vehicle vehicle) {
        parkingLotService.checkInVehicle(lotId, vehicle);
    }

    @PostMapping("/{lotId}/check-out")
    public void checkOutVehicle(@PathVariable String lotId, @RequestBody Vehicle vehicle) {
        parkingLotService.checkOutVehicle(lotId, vehicle);;
    }

    @GetMapping("/{lotId}/availability")
    public String getAvailableParkingLots(@PathVariable String lotId) {
        return parkingLotService.getParkingLotAvailability(lotId);
    }

    @GetMapping("/{lotId}/vehicles")
    public ResponseEntity<List<Vehicle>> getVehiclesInParkingLot(@PathVariable String lotId) {
        List<Vehicle> vehicles = parkingLotService.getVehiclesInParkingLot(lotId);
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }
}
