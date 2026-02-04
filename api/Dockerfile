# Multi-stage build for api module
FROM gradle:8.14.3-jdk17 AS builder

WORKDIR /app

# Copy gradle files and settings for dependency caching
COPY settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./

# Copy api module source
COPY api/build.gradle.kts ./api/
COPY api/src ./api/src

# Build the JAR
RUN ./gradlew :api:bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/api/build/libs/*.jar app.jar

# Create non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Expose api service port
EXPOSE 9010

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:9010/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
