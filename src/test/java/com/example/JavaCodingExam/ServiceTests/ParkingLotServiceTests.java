package com.example.JavaCodingExam.ServiceTests;

import com.example.JavaCodingExam.Controller.Response.SuccessResponse;
import com.example.JavaCodingExam.Entity.ParkingLot;
import com.example.JavaCodingExam.Entity.Vehicle;
import com.example.JavaCodingExam.Entity.VehicleType;
import com.example.JavaCodingExam.Repository.ParkingLotRepository;
import com.example.JavaCodingExam.Repository.VehicleRepository;
import com.example.JavaCodingExam.Service.ParkingLotService;
import com.example.JavaCodingExam.Service.ParkingValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ParkingLotServiceTests {
    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingValidator parkingValidator;

    private ParkingLotService parkingLotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        parkingLotService = new ParkingLotService(parkingLotRepository, vehicleRepository, parkingValidator);
    }

    @Test
    void testRegisterParkingLot_Success() {
        ParkingLot parkingLot = new ParkingLot("lot123", "New York", 100, 0);
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);

        ParkingLot createdParkingLot = parkingLotService.registerParkingLot(parkingLot);

        assertNotNull(createdParkingLot);
        assertEquals("lot123", createdParkingLot.getLotid());
        assertEquals("New York", createdParkingLot.getLocation());
        assertEquals(100, createdParkingLot.getCapacity());
        assertEquals(0, createdParkingLot.getOccupiedSpaces());

        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    void testRegisterVehicle_Success() {
        Vehicle vehicle = new Vehicle("license-123", VehicleType.TRUCK, "Test owner");
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle createdVehicle = parkingLotService.registerVehicle(vehicle);

        assertNotNull(createdVehicle);
        assertEquals("license-123", createdVehicle.getLicensePlate());
        assertEquals(VehicleType.TRUCK, createdVehicle.getVehicleType());
        assertEquals("Test owner", createdVehicle.getOwnerName());

        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void testCheckInVehicle_Success() {
        Vehicle vehicle = new Vehicle("license-123", VehicleType.TRUCK, "Test owner");
        ParkingLot parkingLot = new ParkingLot("lot123", "New York", 100, 0);

        when(vehicleRepository.findById(vehicle.getLicensePlate())).thenReturn(Optional.of(vehicle));
        when(parkingLotRepository.findById(parkingLot.getLotid())).thenReturn(Optional.of(parkingLot));
        when(parkingValidator.ValidateCheckInVehicle(vehicle)).thenReturn(null);
        when(parkingValidator.ValidateParkingLotCheckInVehicle(parkingLot)).thenReturn(null);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);

        SuccessResponse expectedResponse = new SuccessResponse(200, "Vehicle successfully checked into parking lot", Optional.of(vehicle));

        ResponseEntity<?> checkInVehicleResponse = parkingLotService.checkInVehicle(parkingLot.getLotid(), vehicle);

        assertNotNull(checkInVehicleResponse);
        assertEquals(HttpStatus.OK, checkInVehicleResponse.getStatusCode());
        assertEquals(expectedResponse, checkInVehicleResponse.getBody());

        verify(vehicleRepository, times(1)).save(vehicle);
        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    void testCheckOutVehicle_Success() {
        Vehicle vehicle = new Vehicle("license-123", VehicleType.TRUCK, "Test owner");
        ParkingLot parkingLot = new ParkingLot("lot123", "New York", 100, 0);

        parkingLot.getVehicles().add(vehicle);

        when(vehicleRepository.findById(vehicle.getLicensePlate())).thenReturn(Optional.of(vehicle));
        when(parkingLotRepository.findById(parkingLot.getLotid())).thenReturn(Optional.of(parkingLot));
        when(parkingValidator.ValidateCheckOutVehicle(vehicle, parkingLot)).thenReturn(null);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);

        SuccessResponse expectedResponse = new SuccessResponse(200, "Vehicle successfully checked into parking lot", Optional.of(vehicle));

        ResponseEntity<?> checkOutVehicleResponse = parkingLotService.checkOutVehicle(parkingLot.getLotid(), vehicle);

        assertNotNull(checkOutVehicleResponse);
        assertEquals(HttpStatus.OK, checkOutVehicleResponse.getStatusCode());
        assertEquals(expectedResponse, checkOutVehicleResponse.getBody());

        verify(vehicleRepository, times(1)).save(vehicle);
        verify(parkingLotRepository, times(1)).save(parkingLot);
    }
}
