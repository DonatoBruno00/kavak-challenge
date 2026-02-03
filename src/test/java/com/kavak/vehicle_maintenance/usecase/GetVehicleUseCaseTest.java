package com.kavak.vehicle_maintenance.usecase;

import com.kavak.vehicle_maintenance.domain.Vehicle;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetVehicleUseCaseTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private GetVehicleUseCase getVehicleUseCase;

    @Test
    void shouldReturnVehicleWhenExists() {
        // Arrange
        Vehicle vehicle = VehicleTestData.createValidVehicleWithId();
        when(vehicleRepository.findByLicensePlate(VALID_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));

        // Act
        Vehicle result = getVehicleUseCase.execute(VALID_LICENSE_PLATE);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_LICENSE_PLATE, result.getLicensePlate());
        assertEquals(BRAND_BMW, result.getBrand());
        assertEquals(MODEL_135I, result.getModel());
        verify(vehicleRepository, times(1)).findByLicensePlate(VALID_LICENSE_PLATE);
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        String unknownPlate = "UNKNOWN-999";
        when(vehicleRepository.findByLicensePlate(unknownPlate)).thenReturn(Optional.empty());

        // Act & Assert
        VehicleNotFoundException exception = assertThrows(
            VehicleNotFoundException.class,
            () -> getVehicleUseCase.execute(unknownPlate)
        );

        assertTrue(exception.getMessage().contains(unknownPlate));
        verify(vehicleRepository, times(1)).findByLicensePlate(unknownPlate);
    }
}
