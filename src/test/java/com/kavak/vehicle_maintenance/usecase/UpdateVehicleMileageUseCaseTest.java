package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.exception.InvalidMileageException;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import com.kavak.vehicle_maintenance.testdata.VehicleTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.kavak.vehicle_maintenance.testdata.VehicleTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateVehicleMileageUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private UpdateVehicleMileageUseCase updateVehicleMileageUseCase;

    @Test
    void shouldUpdateMileageSuccessfully() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        Integer newMileage = MILEAGE_15000 + 5000; // 20000
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Vehicle result = updateVehicleMileageUseCase.execute(VALID_LICENSE_PLATE, newMileage);

        // Assert
        assertNotNull(result);
        assertEquals(newMileage, result.getCurrentMileage());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        String unknownPlate = "UNKNOWN-999";
        when(vehicleRepository.findByLicensePlate(unknownPlate)).thenReturn(Optional.empty());

        // Act & Assert
        VehicleNotFoundException exception = assertThrows(
            VehicleNotFoundException.class,
            () -> updateVehicleMileageUseCase.execute(unknownPlate, 20000)
        );

        assertTrue(exception.getMessage().contains(unknownPlate));
        verify(vehicleRepository, times(1)).findByLicensePlate(unknownPlate);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void shouldThrowExceptionWhenNewMileageIsLessThanCurrent() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        Integer lowerMileage = MILEAGE_15000 - 1000; // 14000
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));

        // Act & Assert
        InvalidMileageException exception = assertThrows(
            InvalidMileageException.class,
            () -> updateVehicleMileageUseCase.execute(VALID_LICENSE_PLATE, lowerMileage)
        );

        assertTrue(exception.getMessage().contains(VALID_LICENSE_PLATE));
        assertTrue(exception.getMessage().contains(String.valueOf(MILEAGE_15000)));
        assertTrue(exception.getMessage().contains(String.valueOf(lowerMileage)));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void shouldThrowExceptionWhenNewMileageEqualsCurrentMileage() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));

        // Act & Assert
        InvalidMileageException exception = assertThrows(
            InvalidMileageException.class,
            () -> updateVehicleMileageUseCase.execute(VALID_LICENSE_PLATE, MILEAGE_15000)
        );

        assertTrue(exception.getMessage().contains("must be greater than"));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
}
