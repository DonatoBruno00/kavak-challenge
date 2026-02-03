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
- **Springdoc OpenAPI** (Swagger)
- **JUnit 5 + Mockito** (Testing)

## 🏗️ Arquitectura

Este proyecto sigue principios de **Clean Architecture** con separación explícita de capas:

```
com.kavak.vehicle_maintenance/
├── controller/      → REST Controllers
├── service/         → Orchestration layer
├── usecase/         → Business logic (Use Cases)
├── mapper/          → DTO ↔ Entity mappers
├── dto/
│   ├── request/     → Request DTOs
│   └── response/    → Response DTOs
├── repository/      → Spring Data JPA repositories
├── exception/       → Custom exceptions + Global handler
├── domain/          → Core domain
│   ├── enums/       → Domain enums
│   └── entities     → JPA entities (data structures)
└── config/          → Spring configurations
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
docker compose up -d

# Ver logs
docker compose logs -f app

# Detener
docker compose down
```

**Opción 2: Solo DB en Docker + App Local**

```bash
# Iniciar solo PostgreSQL
docker compose up -d postgres

# Ejecutar app localmente
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 📚 Documentación API

### Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON
```
http://localhost:8080/v3/api-docs
```

---

## ✅ Estado de Implementación

### Fase 1: Docker & Database Setup ✓
- PostgreSQL 15 en contenedor Docker
- Spring Boot configurado con JPA/Hibernate
- Multi-stage build optimizado
- Healthcheck de base de datos

### Fase 2: Domain Layer ✓

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

```
┌─────────────────────────────┐
│         VEHICLES            │
├─────────────────────────────┤
│ id (UUID, PK)               │
│ license_plate (VARCHAR, UQ) │
│ brand (VARCHAR)             │
│ model (VARCHAR)             │
│ year (INTEGER)              │
│ current_mileage (INTEGER)   │
└─────────────────────────────┘
         │ 1
         │
         │ has many
         │
         │ *
┌─────────────────────────────┐
│       MAINTENANCES          │
├─────────────────────────────┤
│ id (UUID, PK)               │
│ vehicle_id (UUID, FK)       │
│ type (VARCHAR)              │
│ description (TEXT)          │
│ creation_date (TIMESTAMP)   │
│ status (VARCHAR)            │
│ estimated_cost (DECIMAL)    │
│ final_cost (DECIMAL)        │
└─────────────────────────────┘
```

### Fase 3: Use Cases Implementados ✓

#### 1. Registrar Vehículo (POST /api/vehicles)

**Endpoint:** `POST /api/vehicles`

**Request:**
```json
{
  "licensePlate": "ABC-1234",
  "brand": "BMW",
  "model": "135i",
  "year": 2023,
  "currentMileage": 15000
}
```

**Response exitosa (201 Created):**
```json
{
  "id": "201bde63-33ac-449e-a2f7-547cc154af84",
  "licensePlate": "ABC-1234",
  "brand": "BMW",
  "model": "135i",
  "year": 2023,
  "currentMileage": 15000
}
```

**Validaciones:**
- `licensePlate`: Obligatorio, máximo 20 caracteres, único
- `brand`: Obligatorio, máximo 50 caracteres
- `model`: Obligatorio, máximo 50 caracteres
- `year`: Obligatorio, entre 1900 y 2100
- `currentMileage`: Obligatorio, mayor o igual a 0

**Errores posibles:**

**409 Conflict** - Patente duplicada:
```json
{
  "timestamp": "2026-02-03T13:59:26.640868923",
  "status": 409,
  "error": "Conflict",
  "message": "Vehicle with license plate 'ABC-1234' already exists"
}
```

**400 Bad Request** - Validación fallida:
```json
{
  "timestamp": "2026-02-03T13:59:28.192734340",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "details": [
    "licensePlate: License plate is required",
    "year: Year must be greater than or equal to 1900"
  ]
}
```

**Componentes:**
- `VehicleController`: REST endpoint
- `VehicleService`: Orchestration
- `RegisterVehicleUseCase`: Business logic
- `VehicleMapper`: DTO ↔ Entity mapping
- `GlobalExceptionHandler`: Unified error handling

---

## 🧪 Testing

### Ejecutar todos los tests
```bash
./mvnw test
```
## 🗄️ Conectar a la base de datos

**DBeaver / pgAdmin:**
```
Host: localhost
Port: 5432
Database: vehicle_maintenance
Username: kavak
Password: kavak123
```

**Ver datos insertados:**
```sql
SELECT * FROM vehicles ORDER BY license_plate;
SELECT * FROM maintenances WHERE vehicle_id = '<uuid>';
```
---

## 📝 Notas de Desarrollo
- Cada use case se implementa en su propia branch
- Clean commits siguiendo Conventional Commits
- Tests obligatorios para cada use case
---

## 🤝 Convenciones de Código
- **Entities**: Singular, sin lógica
- **DTOs**: `*RequestDTO`, `*ResponseDTO`
- **Use Cases**: `*UseCase` con verbo + sustantivo
- **Tests**: Un método por escenario, nombres descriptivos
- **Validaciones**: Bean Validation en DTOs, lógica de negocio en Use Cases
