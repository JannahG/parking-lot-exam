package com.example.JavaCodingExam.ValidatorTests;

import com.example.JavaCodingExam.Controller.Response.ErrorResponse;
import com.example.JavaCodingExam.Entity.ParkingLot;
import com.example.JavaCodingExam.Entity.Vehicle;
import com.example.JavaCodingExam.Service.ParkingLotValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParkingLotValidatorTests {

    private ParkingLotValidator parkingLotValidator;

    @Mock
    private Vehicle vehicle;

    @Mock
    private ParkingLot parkingLot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks
        parkingLotValidator = new ParkingLotValidator();
    }

    @Test
    void testValidateCheckInVehicle_VehicleIsNull() {
        ErrorResponse errorResponse = parkingLotValidator.ValidateCheckInVehicle(null);
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getHttpStatusCode());
        assertEquals("Vehicle does not exist", errorResponse.getErrorMessage());
    }

    @Test
    void testValidateCheckInVehicle_VehicleAlreadyParked() {
        when(vehicle.getParkingLot()).thenReturn(parkingLot);

        ErrorResponse errorResponse = parkingLotValidator.ValidateCheckInVehicle(vehicle);
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getHttpStatusCode());
        assertEquals("Vehicle is already parked in another lot", errorResponse.getErrorMessage());
    }

    @Test
    void testValidateCheckInVehicle_Success() {
        when(vehicle.getParkingLot()).thenReturn(null);

        ErrorResponse errorResponse = parkingLotValidator.ValidateCheckInVehicle(vehicle);
        assertNull(errorResponse);
    }

    @Test
    void testValidateParkingLotCheckInVehicle_LotIsFull() {
        when(parkingLot.hasAvailableSpace()).thenReturn(false);

        ErrorResponse errorResponse = parkingLotValidator.ValidateParkingLotCheckInVehicle(parkingLot);
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getHttpStatusCode());
        assertEquals("Parking lot is full", errorResponse.getErrorMessage());
    }

    @Test
    void testValidateParkingLotCheckInVehicle_LotHasAvailableSpace() {
        when(parkingLot.hasAvailableSpace()).thenReturn(true);

        ErrorResponse errorResponse = parkingLotValidator.ValidateParkingLotCheckInVehicle(parkingLot);
        assertNull(errorResponse); // No error response expected
    }
}
