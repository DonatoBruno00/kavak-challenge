package com.kavak.vehicle_maintenance.domain;

import com.kavak.vehicle_maintenance.domain.enums.MaintenanceStatus;
import com.kavak.vehicle_maintenance.domain.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String licensePlate;
    
    @Column(nullable = false, length = 50)
    private String brand;
    
    @Column(nullable = false, length = 50)
    private String model;
    
    @Column(nullable = false)
    private Integer year;
    
    @Column(nullable = false)
    private Integer currentMileage;
    
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Maintenance> maintenances = new ArrayList<>();
}
