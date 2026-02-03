package com.kavak.vehicle_maintenance.repository;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, UUID> {
    
    List<Maintenance> findByVehicleId(UUID vehicleId);
    
    List<Maintenance> findByVehicleIdAndStatus(UUID vehicleId, MaintenanceStatus status);
}
