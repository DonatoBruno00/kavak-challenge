package com.kavak.vehicle_maintenance.testdata;

import com.kavak.vehicle_maintenance.domain.Vehicle;

import java.util.ArrayList;

/**
 * Test data factory for Vehicle entities.
 * Provides common test data to avoid duplication across test classes.
 */
public class VehicleTestData {
    
    // Common test values
    public static final String VALID_LICENSE_PLATE = "ABC-1234";
    public static final String ANOTHER_LICENSE_PLATE = "XYZ-9999";
    public static final String BRAND_BMW = "BMW";
    public static final String BRAND_AUDI = "Audi";
    public static final String BRAND_MERCEDES = "Mercedes-Benz";
    public static final String MODEL_135I = "135i";
    public static final String MODEL_A4 = "A4";
    public static final String MODEL_C_CLASS = "C-Class";
    public static final Integer YEAR_2023 = 2023;
    public static final Integer YEAR_2024 = 2024;
    public static final Integer MILEAGE_15000 = 15000;
    public static final Integer MILEAGE_5000 = 5000;
    
    public static Vehicle createValidVehicle() {
        return Vehicle.builder()
                .licensePlate(VALID_LICENSE_PLATE)
                .brand(BRAND_BMW)
                .model(MODEL_135I)
                .year(YEAR_2023)
                .currentMileage(MILEAGE_15000)
                .maintenances(new ArrayList<>())
                .build();
    }
    
    public static Vehicle createValidVehicleWithId() {
        return Vehicle.builder()
                .id(1L)
                .licensePlate(VALID_LICENSE_PLATE)
                .brand(BRAND_BMW)
                .model(MODEL_135I)
                .year(YEAR_2023)
                .currentMileage(MILEAGE_15000)
                .maintenances(new ArrayList<>())
                .build();
    }
    
    public static Vehicle createAnotherValidVehicle() {
        return Vehicle.builder()
                .licensePlate(ANOTHER_LICENSE_PLATE)
                .brand(BRAND_AUDI)
                .model(MODEL_A4)
                .year(YEAR_2024)
                .currentMileage(MILEAGE_5000)
                .maintenances(new ArrayList<>())
                .build();
    }
    
    public static Vehicle createAnotherValidVehicleWithId() {
        return Vehicle.builder()
                .id(2L)
                .licensePlate(ANOTHER_LICENSE_PLATE)
                .brand(BRAND_AUDI)
                .model(MODEL_A4)
                .year(YEAR_2024)
                .currentMileage(MILEAGE_5000)
                .maintenances(new ArrayList<>())
                .build();
    }
    
    public static Vehicle createCustomVehicle(String licensePlate, String brand, String model, Integer year, Integer mileage) {
        return Vehicle.builder()
                .licensePlate(licensePlate)
                .brand(brand)
                .model(model)
                .year(year)
                .currentMileage(mileage)
                .maintenances(new ArrayList<>())
                .build();
    }
}
