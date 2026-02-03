package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.Vehicle;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import com.kavak.vehicle_maintenance.exception.InvalidStateTransitionException;
import com.kavak.vehicle_maintenance.exception.MaintenanceNotFoundException;
import com.kavak.vehicle_maintenance.repository.MaintenanceRepository;
import com.kavak.vehicle_maintenance.testdata.VehicleTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeMaintenanceStatusUseCaseTest {

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private ChangeMaintenanceStatusUseCase changeMaintenanceStatusUseCase;

    @Test
    void shouldChangePendingToInProgress() {
        // Arrange
        Maintenance maintenance = createMaintenance(MaintenanceStatus.PENDING);
        when(maintenanceRepository.findById(maintenance.getId())).thenReturn(Optional.of(maintenance));
        when(maintenanceRepository.save(any(Maintenance.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Maintenance result = changeMaintenanceStatusUseCase.execute(
                maintenance.getId(), MaintenanceStatus.IN_PROGRESS, null);

        // Assert
        assertEquals(MaintenanceStatus.IN_PROGRESS, result.getStatus());
        verify(maintenanceRepository, times(1)).save(maintenance);
    }

    @Test
    void shouldChangePendingToCancelled() {
        // Arrange
        Maintenance maintenance = createMaintenance(MaintenanceStatus.PENDING);
        when(maintenanceRepository.findById(maintenance.getId())).thenReturn(Optional.of(maintenance));
        when(maintenanceRepository.save(any(Maintenance.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Maintenance result = changeMaintenanceStatusUseCase.execute(
                maintenance.getId(), MaintenanceStatus.CANCELLED, null);

        // Assert
        assertEquals(MaintenanceStatus.CANCELLED, result.getStatus());
    }

    @Test
    void shouldChangeInProgressToCompletedWithFinalCost() {
        // Arrange
        Maintenance maintenance = createMaintenance(MaintenanceStatus.IN_PROGRESS);
        BigDecimal finalCost = new BigDecimal("175.50");
        when(maintenanceRepository.findById(maintenance.getId())).thenReturn(Optional.of(maintenance));
        when(maintenanceRepository.save(any(Maintenance.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Maintenance result = changeMaintenanceStatusUseCase.execute(
                maintenance.getId(), MaintenanceStatus.COMPLETED, finalCost);

        // Assert
        assertEquals(MaintenanceStatus.COMPLETED, result.getStatus());
        assertEquals(finalCost, result.getFinalCost());
    }

    @Test
    void shouldThrowExceptionWhenCompletingWithoutFinalCost() {
        // Arrange
        Maintenance maintenance = createMaintenance(MaintenanceStatus.IN_PROGRESS);
        when(maintenanceRepository.findById(maintenance.getId())).thenReturn(Optional.of(maintenance));

        // Act & Assert
        assertThrows(InvalidStateTransitionException.class,
                () -> changeMaintenanceStatusUseCase.execute(
                        maintenance.getId(), MaintenanceStatus.COMPLETED, null));
        
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
    }

    @Test
    void shouldThrowExceptionWhenTransitionFromCompletedToAny() {
        // Arrange
        Maintenance maintenance = createMaintenance(MaintenanceStatus.COMPLETED);
        when(maintenanceRepository.findById(maintenance.getId())).thenReturn(Optional.of(maintenance));

        // Act & Assert
        assertThrows(InvalidStateTransitionException.class,
                () -> changeMaintenanceStatusUseCase.execute(
                        maintenance.getId(), MaintenanceStatus.IN_PROGRESS, null));
        
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
    }

    @Test
    void shouldThrowExceptionWhenTransitionFromCancelledToCompleted() {
        // Arrange
        Maintenance maintenance = createMaintenance(MaintenanceStatus.CANCELLED);
        when(maintenanceRepository.findById(maintenance.getId())).thenReturn(Optional.of(maintenance));

        // Act & Assert
        assertThrows(InvalidStateTransitionException.class,
                () -> changeMaintenanceStatusUseCase.execute(
                        maintenance.getId(), MaintenanceStatus.COMPLETED, new BigDecimal("100")));
        
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
    }

    @Test
    void shouldThrowExceptionWhenMaintenanceNotFound() {
        // Arrange
        Long unknownId = 999L;
        when(maintenanceRepository.findById(unknownId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MaintenanceNotFoundException.class,
                () -> changeMaintenanceStatusUseCase.execute(
                        unknownId, MaintenanceStatus.IN_PROGRESS, null));
    }

    private Maintenance createMaintenance(MaintenanceStatus status) {
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        return Maintenance.builder()
                .id(1L)
                .vehicle(vehicle)
                .type(MaintenanceType.OIL_CHANGE)
                .description("Test maintenance")
                .estimatedCost(new BigDecimal("150.00"))
                .creationDate(LocalDateTime.now())
                .status(status)
                .build();
    }
}
