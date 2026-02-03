package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.exception.DuplicateLicensePlateException;
import com.kavak.vehicle_maintenance.testdata.VehicleTestData;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.kavak.vehicle_maintenance.testdata.VehicleTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private RegisterVehicleUseCase registerVehicleUseCase;

    @Test
    void shouldRegisterVehicleSuccessfully() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicle();
        when(vehicleRepository.existsByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        // Act
        Vehicle result = registerVehicleUseCase.execute(vehicle);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_LICENSE_PLATE, result.getLicensePlate());
        assertEquals(BRAND_BMW, result.getBrand());
        assertEquals(MODEL_135I, result.getModel());
        assertEquals(YEAR_2023, result.getYear());
        assertEquals(MILEAGE_15000, result.getCurrentMileage());
        
        verify(vehicleRepository, times(1)).existsByLicensePlate(VALID_LICENSE_PLATE);
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void shouldThrowExceptionWhenLicensePlateExists() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicle();
        when(vehicleRepository.existsByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(true);

        // Act & Assert
        DuplicateLicensePlateException exception = assertThrows(
            DuplicateLicensePlateException.class,
            () -> registerVehicleUseCase.execute(vehicle)
        );

        assertEquals("Vehicle with license plate '" + VALID_LICENSE_PLATE + "' already exists", exception.getMessage()); 
        verify(vehicleRepository, times(1)).existsByLicensePlate(VALID_LICENSE_PLATE);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void shouldReturnSavedVehicleWithId() {
        // Arrange
        Vehicle vehicleToSave = VehicleTestData.createAnotherValidVehicle();
        Vehicle savedVehicle = VehicleTestData.createAnotherValidVehicleWithId();
        
        when(vehicleRepository.existsByLicensePlate(ANOTHER_LICENSE_PLATE)).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);

        // Act
        Vehicle result = registerVehicleUseCase.execute(vehicleToSave);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(ANOTHER_LICENSE_PLATE, result.getLicensePlate());
        assertEquals(BRAND_AUDI, result.getBrand());
        assertEquals(MODEL_A4, result.getModel());
        verify(vehicleRepository, times(1)).save(vehicleToSave);
    }
    
    @Test
    void shouldProcessCustomVehicleData() {
        // Arrange
        String customPlate = "TEST-999";
        Vehicle customVehicle = VehicleTestData.createCustomVehicle(
            customPlate, "Mercedes-Benz", "C-Class", 2022, 30000
        );
        
        when(vehicleRepository.existsByLicensePlate(customPlate)).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(customVehicle);

        // Act
        Vehicle result = registerVehicleUseCase.execute(customVehicle);

        // Assert
        assertNotNull(result);
        assertEquals(customPlate, result.getLicensePlate());
        assertEquals("Mercedes-Benz", result.getBrand());
        assertEquals("C-Class", result.getModel());
        verify(vehicleRepository, times(1)).existsByLicensePlate(customPlate);
    }
}
