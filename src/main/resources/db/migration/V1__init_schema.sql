-- Vehicle Maintenance System - Initial Schema
-- Created: 2026-02-03
-- Purpose: Create tables for vehicles and maintenances with all constraints

-- Create vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGSERIAL PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INTEGER NOT NULL CHECK (year >= 1900 AND year <= 2100),
    current_mileage INTEGER NOT NULL CHECK (current_mileage >= 0)
);

-- Create index on license_plate for faster lookups
CREATE INDEX idx_vehicles_license_plate ON vehicles(license_plate);

-- Create maintenances table
CREATE TABLE IF NOT EXISTS maintenances (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('OIL_CHANGE', 'BRAKES', 'ENGINE', 'TIRES', 'TRANSMISSION', 'GENERAL')),
    description VARCHAR(500) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    estimated_cost DECIMAL(10, 2),
    final_cost DECIMAL(10, 2),
    CONSTRAINT fk_maintenances_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

-- Create indexes for common queries
CREATE INDEX idx_maintenances_vehicle_id ON maintenances(vehicle_id);
CREATE INDEX idx_maintenances_status ON maintenances(status);
CREATE INDEX idx_maintenances_vehicle_status ON maintenances(vehicle_id, status);

-- Add comments for documentation
COMMENT ON TABLE vehicles IS 'Stores vehicle information for fleet management';
COMMENT ON TABLE maintenances IS 'Stores maintenance records associated with vehicles';
COMMENT ON COLUMN vehicles.license_plate IS 'Unique license plate identifier';
COMMENT ON COLUMN maintenances.status IS 'Current maintenance status: PENDING, IN_PROGRESS, COMPLETED, CANCELLED';
