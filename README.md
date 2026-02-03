# Sistema de Control de Mantenimiento de Vehículos - Desafío Kavak

API REST para gestionar operaciones de mantenimiento de autos usados, desarrollada con clean architecture y buenas prácticas.

## 🚀 Stack Tecnológico

- **Java 17**
- **Spring Boot 3.5.10**
- **PostgreSQL 15** (Docker)
- **Spring Data JPA**
- **Lombok**
- **Maven**
- **Bean Validation**

## 🏗️ Arquitectura

Este proyecto sigue **Clean Architecture** con separación explícita de capas:

```
presentation/     → Capa HTTP (Controllers, DTOs, Exception Handlers)
application/      → Capa de orquestación (Services, Use Cases, Mappers)
domain/           → Lógica de negocio (Entities, Enums, Repository interfaces, Domain Exceptions)
infrastructure/   → Implementación técnica (JPA repositories, Configs)
```

**Flujo:** `Controller → Service → UseCase → Repository → Database`

## 📋 Requisitos Previos

- Java 17 o superior
- Docker y Docker Compose
- Maven 3.6+ (o usar el Maven wrapper incluido)

## 🐳 Configuración e Inicio

**Opción 1: Todo con Docker (Recomendado)**

```bash
# Iniciar PostgreSQL + Spring Boot
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Detener
docker-compose down
```

**Opción 2: Solo DB en Docker + App Local**

```bash
# Iniciar solo PostgreSQL
docker-compose up -d postgres

# Ejecutar app localmente
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 📦 Estado de Implementación

### ✅ Fase 1: Docker & Database Setup
- PostgreSQL 15 en contenedor Docker
- Spring Boot configurado con JPA/Hibernate
- Multi-stage build optimizado
- Healthcheck de base de datos

### ✅ Fase 2: Domain Layer (Clean Architecture)

**Entities (Estructuras de datos puras)**
- `Vehicle` - id, licensePlate, brand, model, year, currentMileage
- `Maintenance` - id, vehicle, type, description, creationDate, status, estimatedCost, finalCost
- Sin lógica de negocio (movida a use cases)

**Enums**
- `MaintenanceStatus`: PENDING, IN_PROGRESS, COMPLETED, CANCELLED
- `MaintenanceType`: OIL_CHANGE, BRAKES, ENGINE, TIRES, TRANSMISSION, GENERAL

**Exceptions**
- `DomainException` - Base class
- `DuplicateLicensePlateException` - Patente duplicada
- `VehicleNotFoundException` - Vehículo no encontrado
- `MaintenanceNotFoundException` - Mantenimiento no encontrado
- `InvalidStateTransitionException` - Transición de estado inválida

**Repository Interfaces**
- `VehicleRepository`: findByLicensePlate(), existsByLicensePlate()
- `MaintenanceRepository`: findByVehicleId(), findByVehicleIdAndStatus()

**Database Schema**
```sql
-- vehicles: id (PK), license_plate (UNIQUE), brand, model, year, current_mileage
-- maintenances: id (PK), vehicle_id (FK), type, status, creation_date, estimated_cost, final_cost
-- Relationship: Vehicle (1) → Maintenance (N)
```

**Constraints**
- Unique constraint en `license_plate`
- Foreign key: `maintenances.vehicle_id` → `vehicles.id`
- Check constraints en enums (status, type)
- BigDecimal con precision=10, scale=2 para campos monetarios

### 🚧 Próximas Fases
- **Fase 3:** Use Cases (lógica de negocio)
- **Fase 4:** Services, Mappers, DTOs
- **Fase 5:** Controllers, Exception Handlers
- **Fase 6:** Tests

## 🎯 Decisiones de Diseño

**Clean Architecture:** Separación de responsabilidades, lógica de negocio independiente de frameworks. Domain layer sin dependencias externas.

**Entities como Data Structures:** Toda la lógica de negocio se implementa en Use Cases, no en entidades. Esto facilita testing y mantiene las entidades como POJOs simples.

**BigDecimal para dinero:** Precision=10, scale=2 para evitar errores de punto flotante en cálculos monetarios.

**Spring Data JPA:** Repository interfaces sin implementación manual, Spring genera el código automáticamente.

**Docker:** Entorno reproducible, setup simplificado (un solo comando), aplicación portable con multi-stage build optimizado.