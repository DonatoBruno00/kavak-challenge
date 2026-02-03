package com.kavak.vehicle_maintenance.repository;

import com.kavak.vehicle_maintenance.domain.Maintenance;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    
    List<Maintenance> findByVehicleId(Long vehicleId);
    
    List<Maintenance> findByVehicleIdAndStatus(Long vehicleId, MaintenanceStatus status);
}
