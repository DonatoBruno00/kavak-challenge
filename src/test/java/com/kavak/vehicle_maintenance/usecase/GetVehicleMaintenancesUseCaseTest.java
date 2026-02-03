package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
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
class GetVehicleMaintenancesUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private GetVehicleMaintenancesUseCase getVehicleMaintenancesUseCase;

    @Test
    void shouldReturnAllMaintenancesSuccessfully() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        List<Maintenance> maintenances = createTestMaintenances(vehicle);
        vehicle.setMaintenances(maintenances);
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE))
                .thenReturn(Optional.of(vehicle));

        // Act
        List<Maintenance> result = getVehicleMaintenancesUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(MaintenanceType.OIL_CHANGE, result.get(0).getType());
        assertEquals(MaintenanceStatus.PENDING, result.get(0).getStatus());
        assertEquals(MaintenanceType.BRAKES, result.get(1).getType());
        assertEquals(MaintenanceStatus.IN_PROGRESS, result.get(1).getStatus());
        assertEquals(MaintenanceType.TIRES, result.get(2).getType());
        assertEquals(MaintenanceStatus.COMPLETED, result.get(2).getStatus());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }

    @Test
    void shouldReturnEmptyListWhenNoMaintenances() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        vehicle.setMaintenances(new ArrayList<>());
        
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE))
                .thenReturn(Optional.of(vehicle));

        // Act
        List<Maintenance> result = getVehicleMaintenancesUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
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
                () -> getVehicleMaintenancesUseCase.execute(unknownPlate)
        );

        assertTrue(exception.getMessage().contains(unknownPlate));
        verify(vehicleRepository, times(1)).findByLicensePlate(unknownPlate);
    }
    
    private List<Maintenance> createTestMaintenances(Vehicle vehicle) {
        Maintenance m1 = Maintenance.builder()
                .id(1L)
                .vehicle(vehicle)
                .type(MaintenanceType.OIL_CHANGE)
                .description("Oil change")
                .creationDate(LocalDateTime.now().minusDays(5))
                .status(MaintenanceStatus.PENDING)
                .estimatedCost(new BigDecimal("150.00"))
                .build();
        
        Maintenance m2 = Maintenance.builder()
                .id(2L)
                .vehicle(vehicle)
                .type(MaintenanceType.BRAKES)
                .description("Brake inspection")
                .creationDate(LocalDateTime.now().minusDays(3))
                .status(MaintenanceStatus.IN_PROGRESS)
                .estimatedCost(new BigDecimal("300.00"))
                .build();
        
        Maintenance m3 = Maintenance.builder()
                .id(3L)
                .vehicle(vehicle)
                .type(MaintenanceType.TIRES)
                .description("Tire replacement")
                .creationDate(LocalDateTime.now().minusDays(10))
                .status(MaintenanceStatus.COMPLETED)
                .estimatedCost(new BigDecimal("500.00"))
                .finalCost(new BigDecimal("520.00"))
                .build();
        
        return List.of(m1, m2, m3);
    }
}
