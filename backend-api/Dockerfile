# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Najpierw kopiuj pom.xml — cache warstwy zależności
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Potem kopiuj kod
COPY src ./src
RUN mvn clean package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Utwórz katalog na logi (ten sam co w logback-spring.xml)
RUN mkdir -p /tmp/log

# POPRAWIONA nazwa artefaktu — zgodna z artifactId i version z pom.xml
COPY --from=build /app/target/sma-backend-api-1.0.0.jar app.jar

EXPOSE 8080

# Healthcheck na poziomie kontenera
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health/liveness || exit 1

ENTRYPOINT ["java", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-docker}", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]