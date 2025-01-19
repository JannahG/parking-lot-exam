package com.example.JavaCodingExam.ServiceTests;

import com.example.JavaCodingExam.Controller.Response.SuccessResponse;
import com.example.JavaCodingExam.Entity.ParkingLot;
import com.example.JavaCodingExam.Entity.Vehicle;
import com.example.JavaCodingExam.Entity.VehicleType;
import com.example.JavaCodingExam.Repository.ParkingLotRepository;
import com.example.JavaCodingExam.Repository.VehicleRepository;
import com.example.JavaCodingExam.Service.ParkingLotService;
import com.example.JavaCodingExam.Service.ParkingLotValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParkingLotServiceTests {
    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingLotValidator parkingLotValidator;

    @InjectMocks
    private ParkingLotService parkingLotService;

    private ParkingLot parkingLot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        parkingLotService = new ParkingLotService(parkingLotRepository, vehicleRepository, parkingLotValidator);

        parkingLot = new ParkingLot("lot123", "New York", 100, 40);
        Vehicle vehicle = new Vehicle("license-123", VehicleType.TRUCK, "Test owner");

        parkingLot.getVehicles().add(vehicle);
    }

    @Test
    void testRegisterParkingLot_Success() {
        // Arrange
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);

        // Act
        ParkingLot createdParkingLot = parkingLotService.registerParkingLot(parkingLot);

        // Assert
        assertNotNull(createdParkingLot);
        assertEquals("lot123", createdParkingLot.getLotid());
        assertEquals("New York", createdParkingLot.getLocation());
        assertEquals(100, createdParkingLot.getCapacity());
        assertEquals(40, createdParkingLot.getOccupiedSpaces());

        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    void testRegisterVehicle_Success() {
        // Arrange
        when(vehicleRepository.save(parkingLot.getVehicles().get(0))).thenReturn(parkingLot.getVehicles().get(0));

        // Act
        Vehicle createdVehicle = parkingLotService.registerVehicle(parkingLot.getVehicles().get(0));

        // Assert
        assertNotNull(createdVehicle);
        assertEquals("license-123", createdVehicle.getLicensePlate());
        assertEquals(VehicleType.TRUCK, createdVehicle.getVehicleType());
        assertEquals("Test owner", createdVehicle.getOwnerName());

        verify(vehicleRepository, times(1)).save(parkingLot.getVehicles().get(0));
    }

    @Test
    void testCheckInVehicle_Success() {
        // Arrange
        Vehicle vehicle = parkingLot.getVehicles().get(0);

        when(vehicleRepository.findById(vehicle.getLicensePlate())).thenReturn(Optional.of(vehicle));
        when(parkingLotRepository.findById(parkingLot.getLotid())).thenReturn(Optional.of(parkingLot));
        when(parkingLotValidator.ValidateCheckInVehicle(vehicle)).thenReturn(null);
        when(parkingLotValidator.ValidateParkingLotCheckInVehicle(parkingLot)).thenReturn(null);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);

        SuccessResponse expectedResponse = new SuccessResponse(200, "Vehicle successfully checked into parking lot", Optional.of(vehicle));

        // Act
        ResponseEntity<?> checkInVehicleResponse = parkingLotService.checkInVehicle(parkingLot.getLotid(), vehicle);

        // Assert
        assertNotNull(checkInVehicleResponse);
        assertEquals(HttpStatus.OK, checkInVehicleResponse.getStatusCode());
        assertEquals(expectedResponse, checkInVehicleResponse.getBody());

        verify(vehicleRepository, times(1)).save(vehicle);
        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    void testCheckOutVehicle_Success() {
        // Arrange
        Vehicle vehicle = parkingLot.getVehicles().get(0);

        when(vehicleRepository.findById(vehicle.getLicensePlate())).thenReturn(Optional.of(vehicle));
        when(parkingLotRepository.findById(parkingLot.getLotid())).thenReturn(Optional.of(parkingLot));
        when(parkingLotValidator.ValidateCheckOutVehicle(vehicle, parkingLot)).thenReturn(null);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);

        SuccessResponse expectedResponse = new SuccessResponse(200, "Vehicle successfully checked into parking lot", Optional.of(vehicle));

        // Act
        ResponseEntity<?> checkOutVehicleResponse = parkingLotService.checkOutVehicle(parkingLot.getLotid(), vehicle);

        // Assert
        assertNotNull(checkOutVehicleResponse);
        assertEquals(HttpStatus.OK, checkOutVehicleResponse.getStatusCode());

        verify(vehicleRepository, times(1)).save(vehicle);
        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    void testGetParkingLotAvailability_Success() {
        // Arrange
        when(parkingLotRepository.findById("lot1")).thenReturn(Optional.of(parkingLot));

        // Act
        ResponseEntity<SuccessResponse> response = parkingLotService.getParkingLotAvailability("lot1");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Available spaces: 60", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetParkingLotAvailability_ParkingLotNotFound() {
        // Arrange
        when(parkingLotRepository.findById("lot1")).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            parkingLotService.getParkingLotAvailability("lot1");
        });

        // Assert
        assertEquals("Parking lot not found", exception.getMessage());
    }

    @Test
    void testGetVehiclesInParkingLot_Success() {
        // Arrange
        Vehicle vehicle1 = new Vehicle("license-1", VehicleType.TRUCK, "Test owner");
        Vehicle vehicle2 = new Vehicle("license-2", VehicleType.TRUCK, "Test owner");
        parkingLot.getVehicles().add(vehicle1);
        parkingLot.getVehicles().add(vehicle2);

        when(parkingLotRepository.findById("lot123")).thenReturn(Optional.of(parkingLot));

        // Act
        List<Vehicle> vehicles = parkingLotService.getVehiclesInParkingLot("lot123");

        // Assert
        assertNotNull(vehicles);
        assertEquals(3, vehicles.size());
        assertTrue(vehicles.contains(vehicle1));
        assertTrue(vehicles.contains(vehicle2));
    }

    @Test
    void testGetVehiclesInParkingLot_ParkingLotNotFound() {
        // Arrange
        when(parkingLotRepository.findById("lot1")).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            parkingLotService.getVehiclesInParkingLot("lot1");
        });

        // Assert
        assertEquals("Parking lot not found", exception.getMessage());
    }
}
