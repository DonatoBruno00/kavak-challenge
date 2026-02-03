package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.MaintenanceRepository;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import com.kavak.vehicle_maintenance.testdata.VehicleTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.kavak.vehicle_maintenance.testdata.VehicleTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterMaintenanceUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;
    
    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private RegisterMaintenanceUseCase registerMaintenanceUseCase;

    @Test
    void shouldRegisterMaintenanceSuccessfully() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        Maintenance maintenance = createTestMaintenance();
        Maintenance savedMaintenance = createTestMaintenanceWithId(vehicle);
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        when(maintenanceRepository.save(any(Maintenance.class))).thenReturn(savedMaintenance);

        // Act
        Maintenance result = registerMaintenanceUseCase.execute(VALID_LICENSE_PLATE, maintenance);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(MaintenanceType.OIL_CHANGE, result.getType());
        assertEquals(MaintenanceStatus.PENDING, result.getStatus());
        assertEquals(vehicle, result.getVehicle());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
        verify(maintenanceRepository, times(1)).save(maintenance);
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        String unknownPlate = "UNKNOWN-999";
        Maintenance maintenance = createTestMaintenance();
        when(vehicleRepository.findByLicensePlate(unknownPlate)).thenReturn(Optional.empty());

        // Act & Assert
        VehicleNotFoundException exception = assertThrows(
            VehicleNotFoundException.class,
            () -> registerMaintenanceUseCase.execute(unknownPlate, maintenance)
        );

        assertTrue(exception.getMessage().contains(unknownPlate));
        verify(vehicleRepository, times(1)).findByLicensePlate(unknownPlate);
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
    }
    
    private Maintenance createTestMaintenance() {
        return Maintenance.builder()
                .type(MaintenanceType.OIL_CHANGE)
                .description("Regular oil change")
                .estimatedCost(new BigDecimal("150.00"))
                .creationDate(LocalDateTime.now())
                .status(MaintenanceStatus.PENDING)
                .build();
    }
    
    private Maintenance createTestMaintenanceWithId(Vehicle vehicle) {
        return Maintenance.builder()
                .id(1L)
                .vehicle(vehicle)
                .type(MaintenanceType.OIL_CHANGE)
                .description("Regular oil change")
                .estimatedCost(new BigDecimal("150.00"))
                .creationDate(LocalDateTime.now())
                .status(MaintenanceStatus.PENDING)
                .build();
    }
}
