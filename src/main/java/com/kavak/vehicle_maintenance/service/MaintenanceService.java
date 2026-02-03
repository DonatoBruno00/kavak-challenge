package com.kavak.vehicle_maintenance.service;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.dto.request.ChangeStatusRequestDTO;
import com.kavak.vehicle_maintenance.dto.request.MaintenanceRequestDTO;
import com.kavak.vehicle_maintenance.dto.response.MaintenanceResponseDTO;
import com.kavak.vehicle_maintenance.mapper.MaintenanceMapper;
import com.kavak.vehicle_maintenance.usecase.ChangeMaintenanceStatusUseCase;
import com.kavak.vehicle_maintenance.usecase.RegisterMaintenanceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service layer for Maintenance operations.
 * Orchestrates between controllers and use cases.
 */
@Service
@RequiredArgsConstructor
public class MaintenanceService {
    
    private final RegisterMaintenanceUseCase registerMaintenanceUseCase;
    private final ChangeMaintenanceStatusUseCase changeMaintenanceStatusUseCase;
    private final MaintenanceMapper maintenanceMapper;
    
    public MaintenanceResponseDTO registerMaintenance(String licensePlate, MaintenanceRequestDTO requestDTO) {
        Maintenance maintenance = maintenanceMapper.toEntity(requestDTO);
        Maintenance savedMaintenance = registerMaintenanceUseCase.execute(licensePlate, maintenance);
        return maintenanceMapper.toResponseDTO(savedMaintenance);
    }
    
    public MaintenanceResponseDTO changeStatus(Long maintenanceId, ChangeStatusRequestDTO requestDTO) {
        Maintenance updatedMaintenance = changeMaintenanceStatusUseCase.execute(
                maintenanceId, 
                requestDTO.getNewStatus(), 
                requestDTO.getFinalCost()
        );
        return maintenanceMapper.toResponseDTO(updatedMaintenance);
    }
}
