package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import com.kavak.vehicle_maintenance.dto.response.VehicleMaintenanceCostResponseDTO;
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
class CalculateTotalMaintenanceCostUseCaseTest {
    
    @Mock
    private VehicleRepository vehicleRepository;
    
    @InjectMocks
    private CalculateTotalMaintenanceCostUseCase calculateTotalMaintenanceCostUseCase;
    
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
    void shouldCalculateTotalCostOfCompletedMaintenances() {
        // Arrange
        Maintenance completed1 = createMaintenance(1L, MaintenanceStatus.COMPLETED, new BigDecimal("100.00"));
        Maintenance completed2 = createMaintenance(2L, MaintenanceStatus.COMPLETED, new BigDecimal("250.50"));
        Maintenance pending = createMaintenance(3L, MaintenanceStatus.PENDING, null);
        Maintenance inProgress = createMaintenance(4L, MaintenanceStatus.IN_PROGRESS, null);
        Maintenance cancelled = createMaintenance(5L, MaintenanceStatus.CANCELLED, null);
        
        vehicle.getMaintenances().addAll(List.of(completed1, completed2, pending, inProgress, cancelled));
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        VehicleMaintenanceCostResponseDTO result = calculateTotalMaintenanceCostUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(VALID_LICENSE_PLATE, result.getLicensePlate());
        assertEquals(new BigDecimal("350.50"), result.getTotalCost());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldReturnZeroWhenNoCompletedMaintenances() {
        // Arrange
        Maintenance pending = createMaintenance(1L, MaintenanceStatus.PENDING, null);
        Maintenance inProgress = createMaintenance(2L, MaintenanceStatus.IN_PROGRESS, null);
        
        vehicle.getMaintenances().addAll(List.of(pending, inProgress));
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        VehicleMaintenanceCostResponseDTO result = calculateTotalMaintenanceCostUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(VALID_LICENSE_PLATE, result.getLicensePlate());
        assertEquals(BigDecimal.ZERO, result.getTotalCost());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldReturnZeroWhenVehicleHasNoMaintenances() {
        // Arrange
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        VehicleMaintenanceCostResponseDTO result = calculateTotalMaintenanceCostUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(VALID_LICENSE_PLATE, result.getLicensePlate());
        assertEquals(BigDecimal.ZERO, result.getTotalCost());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldIgnoreCompletedMaintenancesWithNullFinalCost() {
        // Arrange
        Maintenance completed1 = createMaintenance(1L, MaintenanceStatus.COMPLETED, new BigDecimal("100.00"));
        Maintenance completed2 = createMaintenance(2L, MaintenanceStatus.COMPLETED, null); // Sin finalCost
        
        vehicle.getMaintenances().addAll(List.of(completed1, completed2));
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        VehicleMaintenanceCostResponseDTO result = calculateTotalMaintenanceCostUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(VALID_LICENSE_PLATE, result.getLicensePlate());
        assertEquals(new BigDecimal("100.00"), result.getTotalCost());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldCalculateCorrectTotalWithMultipleCompletedMaintenances() {
        // Arrange
        Maintenance completed1 = createMaintenance(1L, MaintenanceStatus.COMPLETED, new BigDecimal("150.00"));
        Maintenance completed2 = createMaintenance(2L, MaintenanceStatus.COMPLETED, new BigDecimal("300.75"));
        Maintenance completed3 = createMaintenance(3L, MaintenanceStatus.COMPLETED, new BigDecimal("50.25"));
        
        vehicle.getMaintenances().addAll(List.of(completed1, completed2, completed3));
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));
        
        // Act
        VehicleMaintenanceCostResponseDTO result = calculateTotalMaintenanceCostUseCase.execute(VALID_LICENSE_PLATE);
        
        // Assert
        assertNotNull(result);
        assertEquals(VALID_LICENSE_PLATE, result.getLicensePlate());
        assertEquals(new BigDecimal("501.00"), result.getTotalCost());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }
    
    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            calculateTotalMaintenanceCostUseCase.execute(VALID_LICENSE_PLATE);
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
