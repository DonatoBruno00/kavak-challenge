package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import com.kavak.vehicle_maintenance.exception.VehicleNotFoundException;
import com.kavak.vehicle_maintenance.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetActiveMaintenancesUseCaseTest {
    
    @Mock
    private VehicleRepository vehicleRepository;
    
    @InjectMocks
    private GetActiveMaintenancesUseCase getActiveMaintenancesUseCase;
    
    private static final String VALID_LICENSE_PLATE = "ABC-1234";
    private Vehicle vehicle;
    
    @BeforeEach
    void setUp() {
        vehicle = Vehicle.builder()
                .id(1L)
                .licensePlate(VALID_LICENSE_PLATE)
                .brand("BMW")
                .model("135i")
                .year(2013)
                .currentMileage(50000)
                .maintenances(new ArrayList<>())
                .build();
    }
    
    @Test
    void shouldReturnOnlyActiveMaintenances() {
        // Arrange
        Maintenance pending = createMaintenance(1L, MaintenanceStatus.PENDING, null);
        Maintenance inProgress = createMaintenance(2L, MaintenanceStatus.IN_PROGRESS, null);
        Maintenance completed = createMaintenance(3L, MaintenanceStatus.COMPLETED, new BigDecimal("500.00"));
        Maintenance cancelled = createMaintenance(4L, MaintenanceStatus.CANCELLED, null);
        
        vehicle.getMaintenances().addAll(List.of(pending, inProgress, completed, cancelled));
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        List<Maintenance> result = getActiveMaintenancesUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getStatus() == MaintenanceStatus.PENDING));
        assertTrue(result.stream().anyMatch(m -> m.getStatus() == MaintenanceStatus.IN_PROGRESS));
        assertFalse(result.stream().anyMatch(m -> m.getStatus() == MaintenanceStatus.COMPLETED));
        assertFalse(result.stream().anyMatch(m -> m.getStatus() == MaintenanceStatus.CANCELLED));
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldReturnEmptyListWhenNoActiveMaintenances() {
        // Arrange
        Maintenance completed = createMaintenance(1L, MaintenanceStatus.COMPLETED, new BigDecimal("500.00"));
        Maintenance cancelled = createMaintenance(2L, MaintenanceStatus.CANCELLED, null);
        
        vehicle.getMaintenances().addAll(List.of(completed, cancelled));
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        List<Maintenance> result = getActiveMaintenancesUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldReturnEmptyListWhenVehicleHasNoMaintenances() {
        // Arrange
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        List<Maintenance> result = getActiveMaintenancesUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldReturnOnlyPendingWhenNoInProgress() {
        // Arrange
        Maintenance pending = createMaintenance(1L, MaintenanceStatus.PENDING, null);
        
        vehicle.getMaintenances().add(pending);
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        List<Maintenance> result = getActiveMaintenancesUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(MaintenanceStatus.PENDING, result.get(0).getStatus());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldReturnOnlyInProgressWhenNoPending() {
        // Arrange
        Maintenance inProgress = createMaintenance(1L, MaintenanceStatus.IN_PROGRESS, null);
        
        vehicle.getMaintenances().add(inProgress);
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        List<Maintenance> result = getActiveMaintenancesUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(MaintenanceStatus.IN_PROGRESS, result.get(0).getStatus());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            getActiveMaintenancesUseCase.execute(VALID_LICENSE_PLATE);
        });
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    private Maintenance createMaintenance(Long id, MaintenanceStatus status, BigDecimal finalCost) {
        return Maintenance.builder()
                .id(id)
                .vehicle(vehicle)
                .type(MaintenanceType.OIL_CHANGE)
                .description("Test maintenance")
                .creationDate(LocalDateTime.now())
                .estimatedCost(new BigDecimal("300.00"))
                .status(status)
                .finalCost(finalCost)
                .build();
    }
}
