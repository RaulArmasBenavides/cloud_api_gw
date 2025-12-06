# Etapa de build: Maven + JDK 17 (Temurin)
FROM maven:3.9.10-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa de runtime: solo JDK 17 (Temurin)
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Ajusta el nombre del JAR si cambia
COPY --from=build /app/target/cloud-gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
