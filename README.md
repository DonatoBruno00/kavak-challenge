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
- `InvalidMileageException` - Kilometraje inválido (nuevo <= actual)

**Repository Interfaces**
- `VehicleRepository`: findByLicensePlate(), existsByLicensePlate()
- `MaintenanceRepository`: findByVehicleId(), findByVehicleIdAndStatus()

**Database Schema**

```
┌─────────────────────────────┐
│         VEHICLES            │
├─────────────────────────────┤
│ id (BIGINT, PK, IDENTITY)   │
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
│ id (BIGINT, PK, IDENTITY)   │
│ vehicle_id (BIGINT, FK)     │
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
  "id": 1,
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

#### 2. Actualizar Kilometraje (PATCH /api/vehicles/{licensePlate}/mileage)

**Endpoint:** `PATCH /api/vehicles/{licensePlate}/mileage`

**Request:**
```json
{
  "currentMileage": 18500
}
```

**Response exitosa (200 OK):**
```json
{
  "id": 1,
  "licensePlate": "ABC-1234",
  "brand": "BMW",
  "model": "135i",
  "year": 2023,
  "currentMileage": 18500
}
```

**Regla de negocio:** El nuevo kilometraje debe ser **mayor** al actual (el kilometraje no puede retroceder).

**Errores posibles:**
- `404 Not Found` - Vehículo no existe
- `400 Bad Request` - Nuevo kilometraje <= actual

#### 3. Obtener Vehículo por Patente (GET /api/vehicles/{licensePlate})

**Endpoint:** `GET /api/vehicles/{licensePlate}`

**Response exitosa (200 OK):**
```json
{
  "id": 1,
  "licensePlate": "ABC-1234",
  "brand": "BMW",
  "model": "135i",
  "year": 2023,
  "currentMileage": 18500
}
```

**Errores posibles:**
- `404 Not Found` - Vehículo no existe

#### 4. Registrar Mantenimiento (POST /api/vehicles/{licensePlate}/maintenances)

**Endpoint:** `POST /api/vehicles/{licensePlate}/maintenances`

**Request:**
```json
{
  "type": "OIL_CHANGE",
  "description": "Regular oil change and filter replacement",
  "estimatedCost": 150.00
}
```

**Response exitosa (201 Created):**
```json
{
  "id": 1,
  "vehicleId": 1,
  "type": "OIL_CHANGE",
  "description": "Regular oil change and filter replacement",
  "creationDate": "2026-02-03T15:56:00",
  "status": "PENDING",
  "estimatedCost": 150.00,
  "finalCost": null
}
```

**Tipos de mantenimiento:** `OIL_CHANGE`, `BRAKES`, `ENGINE`, `TIRES`, `TRANSMISSION`, `GENERAL`

**Notas:**
- Estado inicial siempre `PENDING`
- `creationDate` se asigna automáticamente
- `finalCost` es null hasta completar el mantenimiento

**Errores posibles:**
- `404 Not Found` - Vehículo no existe
- `400 Bad Request` - Validación fallida

#### 5. Cambiar Estado de Mantenimiento (PATCH /api/maintenances/{maintenanceId}/status)

**Endpoint:** `PATCH /api/maintenances/{maintenanceId}/status`

**Request (cambiar a EN_PROGRESO):**
```json
{
  "newStatus": "IN_PROGRESS"
}
```

**Request (completar con costo final):**
```json
{
  "newStatus": "COMPLETED",
  "finalCost": 175.50
}
```

**Response exitosa (200 OK):**
```json
{
  "id": 1,
  "vehicleId": 1,
  "type": "OIL_CHANGE",
  "description": "Regular oil change and filter replacement",
  "creationDate": "2026-02-03T15:56:00",
  "status": "COMPLETED",
  "estimatedCost": 150.00,
  "finalCost": 175.50
}
```

**Transiciones válidas:**
- `PENDING` → `IN_PROGRESS`, `CANCELLED`
- `IN_PROGRESS` → `COMPLETED`, `CANCELLED`
- `COMPLETED` → (ninguna, estado final)
- `CANCELLED` → (ninguna, estado final)

**Reglas de negocio:**
- `finalCost` es **obligatorio** cuando se cambia a `COMPLETED`
- Estados `COMPLETED` y `CANCELLED` son finales (no permiten más cambios)

**Errores posibles:**
- `404 Not Found` - Mantenimiento no existe
- `400 Bad Request` - Transición de estado inválida o falta `finalCost`

#### 6. Obtener Mantenimientos de un Vehículo (GET /api/vehicles/{licensePlate}/maintenances)

**Endpoint:** `GET /api/vehicles/{licensePlate}/maintenances`

**Response exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "vehicleId": 1,
    "type": "OIL_CHANGE",
    "description": "Regular oil change",
    "creationDate": "2026-02-03T15:56:00",
    "status": "PENDING",
    "estimatedCost": 150.00,
    "finalCost": null
  },
  {
    "id": 2,
    "vehicleId": 1,
    "type": "BRAKES",
    "description": "Brake inspection",
    "creationDate": "2026-02-01T10:30:00",
    "status": "IN_PROGRESS",
    "estimatedCost": 300.00,
    "finalCost": null
  },
  {
    "id": 3,
    "vehicleId": 1,
    "type": "TIRES",
    "description": "Tire replacement",
    "creationDate": "2026-01-25T14:20:00",
    "status": "COMPLETED",
    "estimatedCost": 500.00,
    "finalCost": 520.00
  }
]
```

**Casos especiales:**
- Retorna lista vacía `[]` si el vehículo no tiene mantenimientos
- Retorna todos los mantenimientos (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)

**Errores posibles:**
- `404 Not Found` - Vehículo no existe

#### 7. Verificar Disponibilidad del Vehículo (GET /api/vehicles/{licensePlate}/availability)

**Endpoint:** `GET /api/vehicles/{licensePlate}/availability`

**Response exitosa (200 OK) - Vehículo disponible:**
```json
{
  "licensePlate": "ABC-1234",
  "available": true
}
```

**Response exitosa (200 OK) - Vehículo NO disponible:**
```json
{
  "licensePlate": "ABC-1234",
  "available": false
}
```

**Regla de negocio:**
- Un vehículo **NO está disponible** si tiene al menos un mantenimiento con estado `PENDING` o `IN_PROGRESS`
- Un vehículo **está disponible** si:
  - No tiene mantenimientos, O
  - Todos sus mantenimientos están en estado `COMPLETED` o `CANCELLED`

**Errores posibles:**
- `404 Not Found` - Vehículo no existe

#### 8. Obtener Mantenimientos Activos (GET /api/vehicles/{licensePlate}/maintenances/active)

**Endpoint:** `GET /api/vehicles/{licensePlate}/maintenances/active`

**Response exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "vehicleId": 1,
    "type": "OIL_CHANGE",
    "description": "Cambio de aceite preventivo",
    "creationDate": "2024-01-15T10:30:00",
    "status": "PENDING",
    "estimatedCost": 300.00,
    "finalCost": null
  },
  {
    "id": 2,
    "vehicleId": 1,
    "type": "BRAKES",
    "description": "Revisión de frenos",
    "creationDate": "2024-01-20T14:00:00",
    "status": "IN_PROGRESS",
    "estimatedCost": 500.00,
    "finalCost": null
  }
]
```

**Regla de negocio:**
- Retorna **solo** mantenimientos con estado `PENDING` o `IN_PROGRESS`
- Filtra automáticamente los mantenimientos `COMPLETED` y `CANCELLED`
- Retorna lista vacía si no hay mantenimientos activos

**Errores posibles:**
- `404 Not Found` - Vehículo no existe

#### 9. Calcular Costo Total de Mantenimiento (GET /api/vehicles/{licensePlate}/maintenances/total-cost)

**Endpoint:** `GET /api/vehicles/{licensePlate}/maintenances/total-cost`

**Response exitosa (200 OK):**
```json
{
  "licensePlate": "ABC-1234",
  "totalCost": 1250.50
}
```

**Regla de negocio:**
- **Solo considera** mantenimientos con estado `COMPLETED` que tengan `finalCost`
- Ignora mantenimientos `PENDING`, `IN_PROGRESS` y `CANCELLED`
- Retorna `0.00` si no hay mantenimientos completados
- Suma precisa usando `BigDecimal` para valores monetarios

**Errores posibles:**
- `404 Not Found` - Vehículo no existe

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

## 📝 Consideraciones para Producción

Esta implementación priorizó **claridad y rapidez** para el challenge técnico. En un entorno productivo, consideraría las siguientes mejoras:

### Arquitectura y Diseño
- **Value Objects**: Encapsular conceptos del dominio (LicensePlate, Money, Mileage) con validaciones inmutables
- **Interfaces para servicios**: Desacoplar implementaciones para facilitar testing y cambios futuros
- **Records de Java**: Usar records para DTOs inmutables en lugar de clases con Lombok
- **Domain Events**: Publicar eventos para acciones críticas (VehicleRegistered, MaintenanceCompleted)

### Testing y Calidad
- **Integration Tests**: Tests end-to-end con TestContainers y base de datos real

### Performance y Escalabilidad
- **Caching**: Redis para consultas frecuentes (disponibilidad, costos totales)
- **Paginación**: Implementar en endpoints que retornan listas
- **Índices de base de datos**: Optimizar búsquedas por patente y estado

### Seguridad
- **Autenticación/Autorización**: Spring Security con JWT/OAuth2
- **Input Sanitization**: Validaciones adicionales contra inyección
- **Auditoría**: Registro de cambios críticos con fecha/usuario

### DevOps
- **CI/CD**: Pipelines automatizados (GitHub Actions/Jenkins)

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
