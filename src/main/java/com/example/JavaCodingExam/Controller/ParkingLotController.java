package com.example.JavaCodingExam.Controller;

import com.example.JavaCodingExam.Controller.Response.SuccessResponse;
import com.example.JavaCodingExam.Entity.ParkingLot;
import com.example.JavaCodingExam.Entity.Vehicle;
import com.example.JavaCodingExam.Service.ParkingLotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
public class ParkingLotController {
    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping
    public ResponseEntity<ParkingLot> createParkingLot(@RequestBody ParkingLot parkingLot) {
        ParkingLot createdParkingLot = parkingLotService.registerParkingLot(parkingLot);
        return new ResponseEntity<>(createdParkingLot, HttpStatus.OK);
    }

    @PostMapping("/{lotId}/check-in")
    public ResponseEntity<?> checkInVehicle(@PathVariable String lotId, @RequestBody Vehicle vehicle) {
        return parkingLotService.checkInVehicle(lotId, vehicle);
    }

    @PostMapping("/{lotId}/check-out")
    public ResponseEntity<?> checkOutVehicle(@PathVariable String lotId, @RequestBody Vehicle vehicle) {
        return parkingLotService.checkOutVehicle(lotId, vehicle);
    }

    @GetMapping("/{lotId}/availability")
    public ResponseEntity<SuccessResponse> getAvailableParkingLots(@PathVariable String lotId) {
        return parkingLotService.getParkingLotAvailability(lotId);
    }

    @GetMapping("/{lotId}/vehicles")
    public ResponseEntity<List<Vehicle>> getVehiclesInParkingLot(@PathVariable String lotId) {
        List<Vehicle> vehicles = parkingLotService.getVehiclesInParkingLot(lotId);
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }
}
