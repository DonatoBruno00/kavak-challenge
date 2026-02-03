package com.kavak.vehicle_maintenance.domain.repository;

import com.kavak.vehicle_maintenance.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    boolean existsByLicensePlate(String licensePlate);
}
