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

## 🎯 Decisiones de Diseño

**Clean Architecture:** Separación de responsabilidades, lógica de negocio independiente de frameworks, hace foco en el dominio incentivando la creación de Value Objects que encapsulan reglas y validaciones.

**Docker:** Entorno reproducible, setup simplificado (un solo comando), aplicación portable con multi-stage build optimizado.