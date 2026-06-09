# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

# Copy parent POM first (for dependency caching)
COPY ../pom.xml /build/parent-pom.xml

# Copy module POM and source
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests — tests run in CI before image build)
RUN apk add --no-cache maven \
    && mvn -f parent-pom.xml install -N -q \
    && mvn package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the built JAR
COPY --from=builder /build/target/*.jar app.jar

# Switch to non-root user
USER appuser

EXPOSE 8080

ARG SPRING_PROFILES_ACTIVE=docker
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

ENTRYPOINT ["java", "-jar", "app.jar"]
