# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

RUN apk add --no-cache maven

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /build/target/*.jar app.jar

USER appuser

EXPOSE 8080

ARG SPRING_PROFILES_ACTIVE=docker
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

ENTRYPOINT ["java", "-jar", "app.jar"]
