# Sistema de Control de Mantenimiento de Vehículos - Desafío Kavak

API REST para gestionar operaciones de mantenimiento de autos usados, desarrollada con clean architecture y buenas prácticas.

---

## 🚀 Quick Start

```bash
# 1. Clonar repo
git clone https://github.com/DonatoBruno00/kavak-challenge.git
cd kavak-challenge

# 2. Iniciar todo con Docker
docker compose up -d

# 3. Verificar que funciona
curl http://localhost:8080/actuator/health

# 4. Ver documentación interactiva
http://localhost:8080/swagger-ui/index.html
```

**Credenciales DB:** `localhost:5432/vehicle_maintenance` - User: `kavak` / Pass: `kavak123`

---

## 📋 Tabla de Contenidos

- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitectura](#️-arquitectura)
- [Configuración e Inicio](#-configuración-e-inicio)
- [Documentación API](#-documentación-api)
- [Estado de Implementación](#-estado-de-implementación)
- [Endpoints Implementados](#-endpoints-implementados)
- [Testing](#-testing)
- [Base de Datos](#-base-de-datos)
- [Convenciones de Código](#-convenciones-de-código)
- [Mejoras Futuras](#-mejoras-futuras)

---

## 🛠️ Stack Tecnológico

- **Java 17**
- **Spring Boot 3.5.10**
- **PostgreSQL 15** (Docker)
- **Spring Data JPA**
- **Lombok**
- **Maven**
- **Bean Validation**
- **Flyway** (Database Migrations)
- **Springdoc OpenAPI** (Swagger)
- **JUnit 5 + Mockito** (Testing)
- **Flyway** (Database Migrations)

---

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
│   └── entities/    → JPA entities (data structures)
└── config/          → Spring configurations
```

**Flujo:** `Controller → Service → UseCase → Repository → Database`

**Principios clave:**
- Entities sin lógica de negocio (solo data structures)
- Lógica de negocio en Use Cases
- Services orquestan Use Cases y Mappers
- Controllers manejan HTTP y validaciones de entrada

---

## 📋 Requisitos Previos

- Java 17 o superior
- Docker y Docker Compose
- Maven 3.6+ (o usar el Maven wrapper incluido)

---

## 🐳 Configuración e Inicio

### Opción 1: Todo con Docker (Recomendado)

```bash
# Iniciar PostgreSQL + Spring Boot
docker compose up -d

# Ver logs en tiempo real
docker compose logs -f app

# Detener servicios
docker compose down

# Detener y eliminar volúmenes (reset completo)
docker compose down -v
```

### Opción 2: Solo DB en Docker + App Local

```bash
# Iniciar solo PostgreSQL
docker compose up -d postgres

# Ejecutar app localmente
./mvnw spring-boot:run
```

**La aplicación estará disponible en:** `http://localhost:8080`

---

## 📚 Documentación API

### Swagger UI (Interfaz Interactiva)
```
http://localhost:8080/swagger-ui/index.html
```

Permite:
- Ver todos los endpoints disponibles
- Probar requests directamente desde el navegador
- Ver schemas de request/response
- Revisar códigos de error y validaciones

### OpenAPI Specification (JSON)
```
http://localhost:8080/v3/api-docs
```

---

## ✅ Estado de Implementación

### Fase 1: Infrastructure ✅ Completado
- PostgreSQL 15 en contenedor Docker
- Spring Boot configurado con JPA/Hibernate
- Multi-stage build optimizado
- Healthcheck de base de datos
- Variables de entorno configuradas
- **Flyway migrations** para versionado de esquema SQL

---

### Fase 2: Domain Layer ✅ Completado

#### Entities (Data Structures Puras)
- `Vehicle` - id, licensePlate, brand, model, year, currentMileage
- `Maintenance` - id, vehicle, type, description, creationDate, status, estimatedCost, finalCost
- Sin lógica de negocio (movida a use cases)

#### Enums
- `MaintenanceStatus`: `PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`
- `MaintenanceType`: `OIL_CHANGE`, `BRAKES`, `ENGINE`, `TIRES`, `TRANSMISSION`, `GENERAL`

#### Domain Exceptions
- `DomainException` - Clase base para excepciones de dominio
- `DuplicateLicensePlateException` - Violación de patente única
- `VehicleNotFoundException` - Vehículo no encontrado
- `MaintenanceNotFoundException` - Mantenimiento no encontrado
- `InvalidStateTransitionException` - Transición de estado inválida
- `InvalidMileageException` - Kilometraje inválido (nuevo <= actual)

#### Repository Interfaces
- `VehicleRepository`: `findByLicensePlate()`, `existsByLicensePlate()`
- `MaintenanceRepository`: `findByVehicleId()`, `findByVehicleIdAndStatus()`

---

### Fase 3: Use Cases ✅ Completado (9/9)

| # | Use Case | Endpoint | Status |
|---|----------|----------|--------|
| 1 | Register Vehicle | `POST /api/vehicles` | ✅ |
| 2 | Update Mileage | `PATCH /api/vehicles/{licensePlate}/mileage` | ✅ |
| 3 | Get Vehicle | `GET /api/vehicles/{licensePlate}` | ✅ |
| 4 | Register Maintenance | `POST /api/vehicles/{licensePlate}/maintenances` | ✅ |
| 5 | Change Status | `PATCH /api/maintenances/{id}/status` | ✅ |
| 6 | Get Maintenances | `GET /api/vehicles/{licensePlate}/maintenances` | ✅ |
| 7 | Check Availability | `GET /api/vehicles/{licensePlate}/availability` | ✅ |
| 8 | Get Active Maintenances | `GET /api/vehicles/{licensePlate}/maintenances/active` | ✅ |
| 9 | Calculate Total Cost | `GET /api/vehicles/{licensePlate}/maintenances/total-cost` | ✅ |

---

## 📡 Endpoints Implementados

### 1. Registrar Vehículo

**`POST /api/vehicles`**

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

**Response (201 Created):**
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
- `licensePlate`: Obligatorio, máximo 20 caracteres, único, formato válido (ABC-1234 o ABC1234)
- `brand`: Obligatorio, máximo 50 caracteres
- `model`: Obligatorio, máximo 50 caracteres
- `year`: Entre 1900 y 2100
- `currentMileage`: Mayor o igual a 0

**Errores:** `409 Conflict` (patente duplicada), `400 Bad Request` (validación fallida)

---

### 2. Actualizar Kilometraje

**`PATCH /api/vehicles/{licensePlate}/mileage`**

**Request:**
```json
{
  "currentMileage": 18500
}
```

**Regla de negocio:** El nuevo kilometraje debe ser **mayor** al actual.

**Errores:** `404 Not Found`, `400 Bad Request` (kilometraje inválido)

---

### 3. Obtener Vehículo

**`GET /api/vehicles/{licensePlate}`**

**Response (200 OK):**
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

**Errores:** `404 Not Found`

---

### 4. Registrar Mantenimiento

**`POST /api/vehicles/{licensePlate}/maintenances`**

**Request:**
```json
{
  "type": "OIL_CHANGE",
  "description": "Regular oil change and filter replacement",
  "estimatedCost": 150.00
}
```

**Response (201 Created):**
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

**Tipos disponibles:** `OIL_CHANGE`, `BRAKES`, `ENGINE`, `TIRES`, `TRANSMISSION`, `GENERAL`

**Notas:**
- Estado inicial siempre `PENDING`
- `creationDate` se asigna automáticamente
- `finalCost` null hasta completar

---

### 5. Cambiar Estado de Mantenimiento

**`PATCH /api/maintenances/{maintenanceId}/status`**

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

**Transiciones válidas:**
```
PENDING → IN_PROGRESS, CANCELLED
IN_PROGRESS → COMPLETED, CANCELLED
COMPLETED → (ninguna, estado final)
CANCELLED → (ninguna, estado final)
```

**Reglas:**
- `finalCost` obligatorio al cambiar a `COMPLETED`
- Estados `COMPLETED` y `CANCELLED` son finales

**Errores:** `404 Not Found`, `400 Bad Request` (transición inválida)

---

### 6. Obtener Mantenimientos

**`GET /api/vehicles/{licensePlate}/maintenances`**

Retorna **todos** los mantenimientos del vehículo (cualquier estado).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "vehicleId": 1,
    "type": "OIL_CHANGE",
    "status": "PENDING",
    "estimatedCost": 150.00,
    "finalCost": null
  }
]
```

**Errores:** `404 Not Found`

---

### 7. Verificar Disponibilidad

**`GET /api/vehicles/{licensePlate}/availability`**

**Response (200 OK):**
```json
{
  "licensePlate": "ABC-1234",
  "available": true
}
```

**Regla de negocio:**
- **NO disponible:** Tiene mantenimientos `PENDING` o `IN_PROGRESS`
- **Disponible:** Todos `COMPLETED`/`CANCELLED` o sin mantenimientos

---

### 8. Obtener Mantenimientos Activos

**`GET /api/vehicles/{licensePlate}/maintenances/active`**

Retorna **solo** mantenimientos `PENDING` o `IN_PROGRESS`.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "type": "OIL_CHANGE",
    "status": "PENDING",
    "estimatedCost": 300.00
  }
]
```

---

### 9. Calcular Costo Total

**`GET /api/vehicles/{licensePlate}/maintenances/total-cost`**

**Response (200 OK):**
```json
{
  "licensePlate": "ABC-1234",
  "totalCost": 1250.50
}
```

**Regla de negocio:**
- Solo suma mantenimientos `COMPLETED` con `finalCost` no null
- Usa `BigDecimal` para precisión monetaria

---

## 🧪 Testing

### Ejecutar Tests

```bash
./mvnw test                                       # Todos los tests
./mvnw test -Dtest=RegisterVehicleUseCaseTest    # Test específico
./mvnw verify                                     # Tests + integration tests
```

### Cobertura Actual

- ✅ **Use Cases**: 27 tests, 95% coverage
- ✅ **Controllers**: 18 integration tests
- ✅ **Services**: 100% coverage
- 📊 **Total**: 85% coverage

### Test Pattern (AAA)

```java
@Test
void shouldRegisterVehicleSuccessfully() {
  // Arrange - Preparar datos y mocks
  var request = VehicleTestData.defaultRequest();
  when(repository.existsByLicensePlate(anyString())).thenReturn(false);
  
  // Act - Ejecutar método bajo prueba
  var result = useCase.execute(request);
  
  // Assert - Verificar resultados
  assertThat(result.getLicensePlate()).isEqualTo(request.getLicensePlate());
  verify(repository).save(any(Vehicle.class));
}
```

---

## 🗄️ Base de Datos

### Schema

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

### Conectar a PostgreSQL

**DBeaver / pgAdmin / TablePlus:**
```
Host:     localhost
Port:     5432
Database: vehicle_maintenance
Username: kavak
Password: kavak123
```

**Docker CLI:**
```bash
docker exec -it vehicle-maintenance-db psql -U kavak -d vehicle_maintenance
```

### Queries Útiles

```sql
-- Ver todos los vehículos
SELECT * FROM vehicles ORDER BY license_plate;

-- Ver mantenimientos de un vehículo
SELECT * FROM maintenances WHERE vehicle_id = 1;

-- Ver solo mantenimientos activos
SELECT * FROM maintenances WHERE status IN ('PENDING', 'IN_PROGRESS');

-- Calcular costo total por vehículo
SELECT vehicle_id, SUM(final_cost) as total
FROM maintenances
WHERE status = 'COMPLETED' AND final_cost IS NOT NULL
GROUP BY vehicle_id;
```

---

## 🤝 Convenciones de Código

### Nomenclatura

| Tipo | Convención | Ejemplo |
|------|------------|---------|
| **Entities** | Singular, sin lógica | `Vehicle`, `Maintenance` |
| **DTOs** | Sufijo Request/Response | `VehicleRequestDTO`, `VehicleResponseDTO` |
| **Use Cases** | Verbo + Sustantivo + UseCase | `RegisterVehicleUseCase` |
| **Services** | Sustantivo + Service | `VehicleService` |
| **Tests** | should + Acción esperada | `shouldRegisterVehicleSuccessfully` |
| **Excepciones** | Descripción + Exception | `DuplicateLicensePlateException` |

### Validaciones

- **Bean Validation en DTOs**: Validaciones HTTP (`@NotNull`, `@NotBlank`, `@Min`, `@Max`)
- **Lógica de negocio en Use Cases**: Validaciones de dominio (patente única, transiciones de estado)

### Git Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: agregar endpoint de disponibilidad de vehículo
fix: corregir validación de kilometraje en actualización
test: agregar tests unitarios para cambio de estado
docs: actualizar README con ejemplos de API
refactor: extraer lógica de validación a clase base
```

---

## 📝 Mejoras Futuras

Esta implementación priorizó **claridad y rapidez** para el challenge técnico. En una versión productiva, consideraría:

### Arquitectura y Diseño
- **Value Objects**: Encapsular `LicensePlate`, `Money`, `Mileage` con validaciones inmutables
- **Interfaces para servicios**: Desacoplar implementaciones
- **Records de Java**: DTOs inmutables sin Lombok
- **Domain Events**: Eventos para `VehicleRegistered`, `MaintenanceCompleted`

### Testing
- **Integration Tests**: Tests end-to-end con TestContainers
- **Contract Testing**: Validar contratos entre servicios

### Performance
- **Caching**: Redis para consultas frecuentes (disponibilidad, costos)
- **Paginación**: En endpoints que retornan listas
- **Índices optimizados**: Por patente, estado, fechas

### Seguridad
- **Autenticación**: Spring Security con JWT/OAuth2
- **Input Sanitization**: Validaciones contra inyección
- **Auditoría**: Registro de cambios críticos

### DevOps
- **CI/CD**: Pipelines automatizados (GitHub Actions)
- **Monitoreo**: Logs estructurados, métricas (Prometheus)
- **Health Checks**: Endpoints de salud detallados

---

## 👤 Autor

**Donato Bruno**
- GitHub: [@DonatoBruno00](https://github.com/DonatoBruno00)
- Proyecto: [Kavak Challenge](https://github.com/DonatoBruno00/kavak-challenge)

---

**Desarrollado aplicando Clean Architecture y mejores prácticas**