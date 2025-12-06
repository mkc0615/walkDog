# WalkDog
A dog walking management and tracking API built with Spring Boot and Kotlin.

## Overview
WalkDog is a multi-module backend REST API that enables users to manage their dog profiles, track walks with GPS coordinates, and analyze walking statistics. 
The application features OAuth2-based authentication, real-time walk tracking, and comprehensive analytics for dog owners.

## Technologies
- **Language:** Kotlin 1.9.25 (JVM 17)
- **Framework:** Spring Boot 3.5.7
- **Security:** Spring Security 6.x with OAuth2
- **Database:** PostgreSQL
- **ORM:** JPA/Hibernate with QueryDSL
- **Build Tool:** Gradle (Multi-module)
- **API Documentation:** Swagger/OpenAPI 3.0

## Project Structure
```
walkdog/
├── auth/                    # Authentication & Authorization Service
│   └── src/main/kotlin/walkdog/auth/
│       ├── config/         # Security & OAuth2 configuration
│       ├── controller/     # JWT and auth endpoints
│       ├── service/        # User authentication services
│       └── domain/         # User, Client, Authorization entities
│
├── api/                     # Main API Service
│   └── src/main/kotlin/walkdog/api/
│       ├── config/         # Security, CORS, QueryDsl configuration
│       ├── controller/     # REST API endpoints
│       ├── service/        # Business logic (Command/Query pattern)
│       └── domain/         # Dog, Walk, Statistics entities
```

## Prerequisites
- Java 17 or higher
- PostgreSQL database
- Gradle (wrapper included)

## Running the Application
### Build the Project
```bash
./gradlew build
```

### Run Auth Service
```bash
./gradlew :auth:bootRun
```
The auth service will start on `http://localhost:9011`

### Run API Service
```bash
./gradlew :api:bootRun
```
The API service will start on `http://localhost:9010`

### Run Both Services
You can run both services in separate terminal windows simultaneously.

## API Documentation
Once the API service is running, access the Swagger UI documentation at:
```
http://localhost:9010/swagger-ui.html
```

## Authentication Flow
1. Register a new user via `/api/v1/users/register`
2. Obtain an access token from the auth service `/oauth2/token` using OAuth2 password grant
3. Include the JWT token in subsequent API requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

## Architecture Highlights
- **Multi-module Design:** Separation of authentication and business logic
- **CQRS-inspired:** Command/Query separation in service layer
- **JWT Security:** RSA-signed tokens with JWKS public key distribution
- **Custom Annotations:** `@LoginUserContext` for automatic user injection
- **Type-safe Queries:** QueryDSL integration for complex database operations

## Development
### Testing
```bash
./gradlew test
```
### Build JAR
```bash
./gradlew bootJar
```

Artifacts will be generated in:
- `auth/build/libs/auth-*.jar`
- `api/build/libs/api-*.jar`
