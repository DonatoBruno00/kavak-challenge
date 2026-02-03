package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import com.kavak.vehicle_maintenance.dto.response.VehicleAvailabilityResponseDTO;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import com.kavak.vehicle_maintenance.testdata.VehicleTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kavak.vehicle_maintenance.testdata.VehicleTestData.VALID_LICENSE_PLATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckVehicleAvailabilityUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private CheckVehicleAvailabilityUseCase checkVehicleAvailabilityUseCase;

    @Test
    void shouldReturnTrueWhenNoMaintenances() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        vehicle.setMaintenances(new ArrayList<>());
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE))
                .thenReturn(Optional.of(vehicle));

        // Act
        VehicleAvailabilityResponseDTO result = checkVehicleAvailabilityUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_LICENSE_PLATE, result.getLicensePlate());
        assertTrue(result.isAvailable());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }

    @Test
    void shouldReturnTrueWhenOnlyCompletedMaintenances() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        List<Maintenance> maintenances = List.of(
                createMaintenance(1L, vehicle, MaintenanceStatus.COMPLETED),
                createMaintenance(2L, vehicle, MaintenanceStatus.COMPLETED)
        );
        vehicle.setMaintenances(maintenances);
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE))
                .thenReturn(Optional.of(vehicle));

        // Act
        VehicleAvailabilityResponseDTO result = checkVehicleAvailabilityUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertTrue(result.isAvailable());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }

    @Test
    void shouldReturnTrueWhenOnlyCancelledMaintenances() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        List<Maintenance> maintenances = List.of(
                createMaintenance(1L, vehicle, MaintenanceStatus.CANCELLED),
                createMaintenance(2L, vehicle, MaintenanceStatus.CANCELLED)
        );
        vehicle.setMaintenances(maintenances);
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE))
                .thenReturn(Optional.of(vehicle));

        // Act
        VehicleAvailabilityResponseDTO result = checkVehicleAvailabilityUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertTrue(result.isAvailable());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }

    @Test
    void shouldReturnFalseWhenHasPendingMaintenance() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        List<Maintenance> maintenances = List.of(
                createMaintenance(1L, vehicle, MaintenanceStatus.PENDING),
                createMaintenance(2L, vehicle, MaintenanceStatus.COMPLETED)
        );
        vehicle.setMaintenances(maintenances);
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE))
                .thenReturn(Optional.of(vehicle));

        // Act
        VehicleAvailabilityResponseDTO result = checkVehicleAvailabilityUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertFalse(result.isAvailable());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }

    @Test
    void shouldReturnFalseWhenHasInProgressMaintenance() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        List<Maintenance> maintenances = List.of(
                createMaintenance(1L, vehicle, MaintenanceStatus.IN_PROGRESS),
                createMaintenance(2L, vehicle, MaintenanceStatus.COMPLETED)
        );
        vehicle.setMaintenances(maintenances);
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE))
                .thenReturn(Optional.of(vehicle));

        // Act
        VehicleAvailabilityResponseDTO result = checkVehicleAvailabilityUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertFalse(result.isAvailable());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }

    @Test
    void shouldReturnFalseWhenHasBothPendingAndInProgress() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        List<Maintenance> maintenances = List.of(
                createMaintenance(1L, vehicle, MaintenanceStatus.PENDING),
                createMaintenance(2L, vehicle, MaintenanceStatus.IN_PROGRESS),
                createMaintenance(3L, vehicle, MaintenanceStatus.COMPLETED)
        );
        vehicle.setMaintenances(maintenances);
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE))
                .thenReturn(Optional.of(vehicle));

        // Act
        VehicleAvailabilityResponseDTO result = checkVehicleAvailabilityUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertFalse(result.isAvailable());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        String unknownPlate = "UNKNOWN-999";
        when(vehicleRepository.findByLicensePlate(unknownPlate))
                .thenReturn(Optional.empty());

        // Act & Assert
        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class,
                () -> checkVehicleAvailabilityUseCase.execute(unknownPlate)
        );

        assertTrue(exception.getMessage().contains(unknownPlate));
        verify(vehicleRepository, times(1)).findByLicensePlate(unknownPlate);
    }
    
    private Maintenance createMaintenance(Long id, Vehicle vehicle, MaintenanceStatus status) {
        return Maintenance.builder()
                .id(id)
                .vehicle(vehicle)
                .type(MaintenanceType.OIL_CHANGE)
                .description("Test maintenance")
                .creationDate(LocalDateTime.now())
                .status(status)
                .estimatedCost(new BigDecimal("150.00"))
                .build();
    }
}
